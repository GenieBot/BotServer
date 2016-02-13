package io.sponges.bot.server;

import io.sponges.bot.server.event.events.*;
import io.sponges.bot.server.event.framework.EventManager;
import io.sponges.bot.server.messages.SendRawMessage;
import io.sponges.bot.server.bridge.BridgeManager;
import io.sponges.bot.server.cmd.framework.CommandHandler;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.event.framework.Listener;
import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;
import io.sponges.bot.server.internal.Server;
import io.sponges.bot.server.messages.KickUserMessage;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.util.Msg;
import io.sponges.bot.server.util.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.List;

/**
 * Listener class for bot events
 */
public class BotListener implements Listener {

    private final Bot bot;
    private final Server server;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final Database database;

    // TODO instance for statistics instead of variables
    private static volatile int chatMessages, serverMessages, commandRuns;

    public BotListener(Bot bot, Server server, Database database) {
        this.bot = bot;
        this.server = server;
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
        System.out.println("TYPE=" + type);

        // Checking the type of message sent
        switch (type) {
            case "CONNECT": {
                System.out.println("CONNECT");
                String clientId = object.getString("client-id");
                String channel = object.getString("channel");
                Client client = new ClientImpl(clientId, channel, server);
                eventManager.handle(new ConnectEvent(client));
                break;
            }

            case "CHAT": {
                String clientId = object.getString("client-id");
                Client client = bot.getClients().get(clientId);

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
                Client client = bot.getClients().get(clientId);

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
        System.out.println("CHAT MESSAGE");

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
        RoomData data = room.getRoomData();

        // TODO prefix resetting
        /*if (message.toLowerCase().contains("spongybot prefix")) {
            client.sendMessage(new CmdResponseMessage(user, "Prefix for this room: " + settings.get(Setting.PREFIX)));

            if (message.toLowerCase().contains("reset")) {
                if (group.getId().equals("admin") || group.getId().equals("op")) {
                    settings.set(Setting.PREFIX, "$");
                    database.save(room);
                    client.sendMessage(new CmdResponseMessage(user, "Reset the prefix of this room to " + settings.get(Setting.PREFIX)));
                } else {
                    client.sendMessage(new CmdResponseMessage(user, "no perms fam. Needs admin!"));
                }
            }
        }*/

        // Is the message a command?
        System.out.println("checking if command");
        if (CommandHandler.isCommandRequest(room, message)) {
            System.out.println("IS COMMAND!!!!");
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
            if ((boolean) data.get(Setting.LINK_PARSING)) {
                List<String> links = StringUtils.extractUrls(Jsoup.parse(message).text());

                if (links.size() > 0) {
                    if ((boolean) data.get(Setting.DEBUG)) {
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

        RoomData data = room.getRoomData();

        {
            @SuppressWarnings("unchecked")
            List<String> banned = (List<String>) data.get(Setting.BANNED_USERS);

            if (banned.contains(user.getId())) {
                client.sendMessage(new SendRawMessage(client, room.getNetwork().getId(), room.getId(),
                        "You are banned " + user.getDisplayName() + "(" + user.getId() + ")! To unban a user, use the 'unban' command."));
                client.sendMessage(new KickUserMessage(user));
            }
        }
    }
}
