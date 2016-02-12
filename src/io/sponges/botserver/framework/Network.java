package io.sponges.botserver.framework;

import io.sponges.botserver.Client;

public interface Network {

    String getId();

    Client getClient();

    RoomManager getRoomManager();

    Room getRoomWithUser(String user);

}
