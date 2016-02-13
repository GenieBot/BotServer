package io.sponges.bot.server.framework;

import io.sponges.bot.server.Client;

public interface Network {

    String getId();

    Client getClient();

    RoomManager getRoomManager();

    Room getRoomWithUser(String user);

}
