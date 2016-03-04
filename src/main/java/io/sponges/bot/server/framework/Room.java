package io.sponges.bot.server.framework;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.UserRole;

import java.util.Map;

public interface Room {

    Map<String, User> getUsers();

    Client getClient();

    Network getNetwork();

    String getId();

    String getTopic();

    RoomData getRoomData();

    void setRoomData(RoomData roomData);

    boolean isUser(String id);

    User getUser(String id);

    User getOrCreateUser(String id, String username, String displayName, UserRole role);

    String getPrefix();

    void setPrefix(String prefix);

    void sendMessage(String message);
}
