package pw.sponges.botserver.event.events;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.event.framework.Event;

public class ChatMessageEvent extends Event {

    private final Client client;
    private final String user, room, roomName, message;

    public ChatMessageEvent(Client client, String user, String room, String roomName, String message) {
        this.client = client;
        this.user = user;
        this.room = room;
        this.roomName = roomName;
        this.message = message;
    }

    public Client getClient() {
        return client;
    }

    public String getUser() {
        return user;
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
}
