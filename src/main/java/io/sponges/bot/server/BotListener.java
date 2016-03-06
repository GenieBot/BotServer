package io.sponges.bot.server;

import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.event.events.server.ClientConnectEvent;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.server.cmd.CommandRequestImpl;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.protocol.msg.SendRawMessage;
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

    private final EventManager eventManager;

    public BotListener(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void onClientInput(ClientInputEvent event) {
        // Parsing input as JSON
        JSONObject object = new JSONObject(event.getInput());
        String type = object.getString("type").toUpperCase();

        // Checking the type of message sent
        switch (type) {
            case "CONNECT": {
                String clientId = object.getString("client-id");
                Client client = new ClientImpl(clientId);
                eventManager.post(new ClientConnectEvent(client));
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

        // Handle the command
        CommandRequest request = new CommandRequestImpl(client, network, channel)

        if (commandHandler.handle(event.getCommandRequest())) {
            commandRuns++;
        }
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
