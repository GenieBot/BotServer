package io.sponges.bot.server;

import io.sponges.bot.server.cmd.framework.CommandHandler;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.event.events.*;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;
import io.sponges.bot.server.internal.Server;
import io.sponges.bot.server.messages.KickUserMessage;
import io.sponges.bot.server.messages.SendRawMessage;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.util.Msg;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * Listener class for bot events
 */
public class BotListener {

    private final Bot bot;
    private final Server server;
    private final EventBus eventBus;
    private final CommandHandler commandHandler;
    private final Database database;

    private static volatile int chatMessages, serverMessages, commandRuns;

    public BotListener(Bot bot, Server server, Database database) {
        this.bot = bot;
        this.server = server;
        this.database = database;

        this.eventBus = bot.getEventBus();
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
        String type = object.getString("type").toUpperCase();

        // Checking the type of message sent
        switch (type) {
            case "CONNECT": {
                String clientId = object.getString("client-id");
                String channel = object.getString("channel");
                Client client = new ClientImpl(clientId, channel, server);
                eventBus.post(new ConnectEvent(client));
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

                eventBus.post(new ChatMessageEvent(client, network, room, user, message));
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

                eventBus.post(new UserJoinEvent(user));
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
    public void onChatMessage(ChatMessageEvent event) {
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

        // Is the message a command?
        if (CommandHandler.isCommandRequest(room, message)) {
            eventBus.post(new CommandRequestEvent(new CommandRequest(user, message)));
        }
    }

    /**
     * When a command is requested
     * @param event CommandRequestEvent instance
     */
    public void onCommandRequest(CommandRequestEvent event) {
        // Increment command run counter
        commandRuns++;

        // Handle the command
        commandHandler.handle(event.getCommandRequest());
    }

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
