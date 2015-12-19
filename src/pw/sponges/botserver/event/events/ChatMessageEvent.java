package pw.sponges.botserver.event.events;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.event.framework.Event;
import pw.sponges.botserver.permissions.simple.UserRole;

public class ChatMessageEvent extends Event {

    private final Client client;
    private final String userId, username, room, roomName, message;
    private final UserRole role;

    public ChatMessageEvent(Client client, String userId, String username, String room, String roomName, String message, UserRole role) {
        this.client = client;
        this.userId = userId;
        this.username = username;
        this.room = room;
        this.roomName = roomName;
        this.message = message;
        this.role = role;
    }

    public Client getClient() {
        return client;
    }

    public String getRoom() {
        return room;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String needsChecks() {
        return room;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}
