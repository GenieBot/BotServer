package pw.sponges.botserver;

import org.json.JSONObject;
import pw.sponges.botserver.bridge.Bridge;
import pw.sponges.botserver.bridge.BridgeManager;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.event.events.*;
import pw.sponges.botserver.event.framework.EventManager;
import pw.sponges.botserver.event.framework.Listener;
import pw.sponges.botserver.messages.ChatMessage;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.Setting;
import pw.sponges.botserver.util.Msg;

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
            Msg.warning("Got non json message! " + event.getInput());
            return;
        }

        // Parsing input as JSON
        JSONObject object = new JSONObject(event.getInput());
        Msg.debug(object.toString());

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
                String userId = object.getString("userid");
                String username = object.getString("username");
                String room = object.getString("room");
                String name = object.getString("name");
                String message = object.getString("message");
                String role = object.getString("role");
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                eventManager.handle(new ChatMessageEvent(client, userId, username, room, name, message, userRole));
                break;
            }

            case "LINK": {
                String clientId = object.getString("client-id");
                Client client = bot.getClient(clientId);
                String clientRoom = object.getString("room");
                String targetId = object.getString("target-client");
                String targetRoom = object.getString("target-room");
                eventManager.handle(new LinkRequestEvent(client, clientRoom, targetId, targetRoom));
                break;
            }

            default: {
                Msg.warning("Unknown message type! " + type);
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
            Msg.warning("There is already a client connected with the ID " + id + "! Removing old instance from map.");
            // TODO send already connected message?
            //event.getClient().getWrapper().disconnect();
            bot.getClients().remove(id);
        }

        // Setup the connection
        Msg.log("Client identified as " + id + "!");
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
        String room = event.getRoom();
        String userId = event.getUserId();
        String username = event.getUsername();
        String message = event.getMessage();
        UserRole role = event.getRole();

        Msg.log("[" + clientId + "] [" + event.getRoomName() + "] " + username + ": " + message);

        boolean simplePerms = (boolean) database.getData(room).getSettings().get(Setting.SIMPLE_PERMS);

        // Loading the perms for that room
        PermissionGroups groups = permissions.getGroups(room);

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

        // Is the message a command?
        if (CommandHandler.isCommandRequest(room, message)) {
            eventManager.handle(new CommandRequestEvent(new CommandRequest(client, userId, room, message, group)));
        }

        BridgeManager bridgeManager = client.getBridgeManager();

        // Is the client's chat bridged
        if (bridgeManager.isBridged(room)) {
            Bridge bridge = bridgeManager.getBridge(room);
            String targetClient = bridge.getTargetClient();
            Client tClient = bot.getClient(targetClient);
            String tRoom = bridge.getTargetRoom();

            // Send message to the bridged target room
            tClient.sendMessage(new ChatMessage(tClient, userId, username, room, event.getRoomName(), tRoom, message));
            Msg.debug("Sent bridge message " + message + " to client " + targetClient + " room " + tRoom + " from " + clientId + " room " + room);
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

    /**
     * When a chat is bridged into another chat
     * @param event
     */
    @Override
    public void onLinkRequest(LinkRequestEvent event) {
        String clientId = event.getClient().getId();
        String clientRoom = event.getClientRoom();
        String target = event.getTargetId();
        String targetRoom = event.getTargetRoom();

        // Adding the link
        bot.addLink(clientId, clientRoom, target, targetRoom);
    }
}
