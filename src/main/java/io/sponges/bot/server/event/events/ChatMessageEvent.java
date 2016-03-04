package io.sponges.bot.server.event.events;

import io.sponges.bot.server.event.framework.Event;
import io.sponges.bot.server.Client;
import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;

public class ChatMessageEvent extends Event {

    private final Client client;
    private final Network network;
    private final Room room;
    private final User user;
    private final String message;

    public ChatMessageEvent(Client client, Network network, Room room, User user, String message) {
        this.client = client;
        this.network = network;
        this.room = room;
        this.user = user;
        this.message = message;
    }

    public Client getClient() {
        return client;
    }

    public Network getNetwork() {
        return network;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}