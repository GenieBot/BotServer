package pw.sponges.botserver;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import pw.sponges.botserver.bridge.BridgeManager;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.event.events.*;
import pw.sponges.botserver.event.framework.EventManager;
import pw.sponges.botserver.event.framework.Listener;
import pw.sponges.botserver.framework.Network;
import pw.sponges.botserver.framework.Room;
import pw.sponges.botserver.framework.User;
import pw.sponges.botserver.messages.CmdResponseMessage;
import pw.sponges.botserver.messages.KickUserMessage;
import pw.sponges.botserver.messages.SendRawMessage;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomSettings;
import pw.sponges.botserver.storage.Setting;
import pw.sponges.botserver.util.Msg;
import pw.sponges.botserver.util.StringUtils;

import java.util.List;

/**
 * Listener class for bot events
 */
public class BotListener implements Listener {

    private final Bot bot;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final PermissionsManager permissions;
    private final Database database;

    // TODO instance for statistics instead of variables
    private static int chatMessages, serverMessages, commandRuns;

    /**
     * Instantiating the variables in the constructor
     * @param bot the Bot instance
     * @param permissions the PermissionsManager instance
     * @param database
     */
    public BotListener(Bot bot, PermissionsManager permissions, Database database) {
        this.bot = bot;
        this.permissions = permissions;
        this.database = database;

        this.eventManager = bot.getEventManager();
        this.commandHandler = bot.getCommandHandler();
    }

    /**
     * The total number of chat messages that have been received
     * @return chatMessages
     */
    public static int getChatMessages() {
        return chatMessages;
    }

    /**
     * The total number of server messages that have been received
     * @return serverMessages
     */
    public static int getServerMessages() {
        return serverMessages;
    }

    /**
     * The total number of times a command has been ran
     * @return commandRuns
     */
    public static int getCommandRuns() {
        return commandRuns;
    }

    /**
     * When the server gets (json message) input from a client
     * @param event ClientInputEvent instance
     */
    @Override
    public void onClientInput(ClientInputEvent event) {
        // Adding to the total server messages count
        serverMessages++;

        // Checking to see if the message received is actually a valid message
        if (!event.getInput().contains("{")) {
            Msg.warning("[Client input] " + event.getInput());
            return;
        }

        // Parsing input as JSON
        JSONObject object = new JSONObject(event.getInput());
        Msg.debug("[Client input] " + object.toString());

        String type = object.getString("type").toUpperCase();

        // Checking the type of message sent
        switch (type) {
            case "CONNECT": {
                String clientId = object.getString("client-id");
                Client client = new ClientImpl(clientId, event.getWrapper());
                eventManager.handle(new ConnectEvent(client));
                break;
            }

            case "CHAT": {
                String clientId = object.getString("client-id");
                Client client = bot.getClient(clientId);

                String networkId = object.getJSONObject("network").getString("id");
                Network network = client.getNetworkManager().getOrCreateNetwork(networkId);

                JSONObject roomObject = object.getJSONObject("room");
                String roomId = roomObject.getString("id");
                String topic = roomObject.getString("topic");
                Room room = network.getRoomManager().getOrCreateRoom(roomId, topic);

                JSONObject userObject = object.getJSONObject("user");
                String userId = userObject.getString("id");
                String username = userObject.getString("username");
                String displayName = userObject.getString("display-name");
                String role = userObject.getString("role");
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                User user = room.getOrCreateUser(userId, username, displayName, userRole);

                String message = object.getString("message");
                message = StringEscapeUtils.escapeJson(message);

                eventManager.handle(new ChatMessageEvent(client, network, room, user, message));
                break;
            }

            case "JOIN": {
                String clientId = object.getString("client-id");
                Client client = bot.getClient(clientId);

                String networkId = object.getJSONObject("network").getString("id");
                Network network = client.getNetworkManager().getOrCreateNetwork(networkId);

                JSONObject roomObject = object.getJSONObject("room");
                String roomId = roomObject.getString("id");
                String topic = roomObject.getString("topic");
                Room room = network.getRoomManager().getOrCreateRoom(roomId, topic);

                JSONObject userObject = object.getJSONObject("user");
                String userId = userObject.getString("id");
                String username = userObject.getString("username");
                String displayName = userObject.getString("display-name");
                String role = userObject.getString("role");
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                User user = room.getOrCreateUser(userId, username, displayName, userRole);

                eventManager.handle(new UserJoinEvent(user));
                break;
            }

            default: {
                Msg.warning("[Client input] Unknown message type! " + type);
                Msg.debug(object.toString());
                break;
            }
        }
    }

    /**
     * When a client connects
     * @param event ConnectEvent instance
     */
    @Override
    public void onConnect(ConnectEvent event) {
        String id = event.getClient().getId();

        // Check if the client is already connected
        if (bot.getClients().containsKey(id)) {
            Msg.warning("[Connect event] There is already a client connected with the ID " + id + "! Removing old instance from map.");
            // TODO send already connected message?
            //event.getClient().getWrapper().disconnect();
            bot.getClients().remove(id);
        }

        // Setup the connection
        Msg.log("[Connect event] Client identified as " + id + "!");
        bot.getClients().put(event.getClient().getId(), event.getClient());
    }

    /**
     * When a chat message is sent
     * @param event ChatMessageEvent instance
     */
    @Override
    public void onChatMessage(ChatMessageEvent event) {
        // Increment chat messages count
        chatMessages++;

        Client client = event.getClient();
        String clientId = client.getId();

        Room room = event.getRoom();
        String roomId = room.getId();
        String topic = room.getTopic();

        User user = event.getUser();
        String userId = user.getId();
        String username = user.getUsername();
        String displayName = user.getDisplayName();
        UserRole role = user.getRole();

        String message = event.getMessage();

        Msg.log("[" + clientId + "] [" + room.getTopic() + "] " + username + ": " + message);

        // TODO rework room data storage
        RoomSettings settings = database.getData(roomId).getSettings();
        boolean simplePerms = (boolean) settings.get(Setting.SIMPLE_PERMS);

        // Loading the perms for that room
        PermissionGroups groups = permissions.getGroups(roomId);

        // If the user does not have a group
        if (!groups.isSetup(userId)) {
            if (simplePerms) groups.setup(userId, role);
            else groups.setup(userId);
        }
        Group group = groups.getUserGroup(userId);

        if (simplePerms) {
            if ((role == UserRole.OP && !group.getId().equalsIgnoreCase("op"))
                    || (role == UserRole.USER && !group.getId().equalsIgnoreCase("default"))
                    || (role == UserRole.ADMIN && !group.getId().equalsIgnoreCase("admin"))) {
                groups.setup(userId, role);
            }
        }

        if (message.toLowerCase().contains("spongybot prefix")) {
            client.sendMessage(new CmdResponseMessage(user, "Prefix for this room: " + settings.get(Setting.PREFIX)));

            if (message.toLowerCase().contains("reset")) {
                if (group.getId().equals("admin") || group.getId().equals("op")) {
                    settings.set(Setting.PREFIX, "$");
                    database.save(roomId);
                    client.sendMessage(new CmdResponseMessage(user, "Reset the prefix of this room to " + settings.get(Setting.PREFIX)));
                } else {
                    client.sendMessage(new CmdResponseMessage(user, "no perms fam. Needs admin!"));
                }
            }
        }

        // Is the message a command?
        if (CommandHandler.isCommandRequest(roomId, message)) {
            eventManager.handle(new CommandRequestEvent(new CommandRequest(user, message)));
        }

        BridgeManager bridgeManager = client.getBridgeManager();

        // TODO redo bridging
        // Is the client's chat bridged
        /*if (bridgeManager.isBridged(roomId)) {
            Bridge bridge = bridgeManager.getBridge(roomId);
            String targetClient = bridge.getTargetClient();
            Client tClient = bot.getClient(targetClient);
            String tRoom = bridge.getTargetRoom();


            // Send message to the bridged target room
            //tClient.sendMessage(new ChatMessage(user, tRoom2, message));
            Msg.debug("[Bridged Chat] Sent bridge message " + message + " to client " + targetClient + " room " + tRoom + " from " + clientId + " room " + room);
        }*/

        {
            // Link parsing
            if ((boolean) settings.get(Setting.LINK_PARSING)) {
                List<String> links = StringUtils.extractUrls(Jsoup.parse(message).text());

                if (links.size() > 0) {
                    if ((boolean) settings.get(Setting.DEBUG)) {
                        for (String s : links) {
                            client.sendMessage(new SendRawMessage(client, room.getNetwork().getId(), room.getId(), "Found " + s));
                        }
                    }

                    String link = links.get(0);
                    Msg.debug("[Link parsing] got " + link + " from " + links);
                    String parsed = bot.getParserManager().handle(link);

                    if (parsed != null) {
                        client.sendMessage(new SendRawMessage(client, room.getNetwork().getId(), room.getId(), parsed));
                    }
                }
            }
        }
    }

    /**
     * When a command is requested
     * @param event CommandRequestEvent instance
     */
    @Override
    public void onCommandRequest(CommandRequestEvent event) {
        // Increment command run counter
        commandRuns++;

        // Handle the command
        commandHandler.handle(event.getCommandRequest());
    }

    @Override
    public void onUserJoin(UserJoinEvent event) {
        User user = event.getUser();
        Room room = user.getRoom();
        Client client = room.getClient();

        RoomSettings settings = database.getData(room.getId()).getSettings();

        {
            @SuppressWarnings("unchecked")
            List<String> banned = (List<String>) settings.get(Setting.BANNED_USERS);

            if (banned.contains(user)) {
                client.sendMessage(new SendRawMessage(client, room.getNetwork().getId(), room.getId(),
                        "You are banned " + user.getDisplayName() + "(" + user.getId() + ")! To unban a user, use the 'unban' command."));
                client.sendMessage(new KickUserMessage(user));
            }
        }
    }
}
