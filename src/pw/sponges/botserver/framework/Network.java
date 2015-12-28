package pw.sponges.botserver.framework;

import pw.sponges.botserver.Client;

public interface Network {

    String getId();

    Client getClient();

    RoomManager getRoomManager();

    Room getRoomWithUser(String user);

}
