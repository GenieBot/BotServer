package pw.sponges.botserver;

import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.messages.Message;

public interface Client {

    String getId();

    void sendMessage(Message message);

    ServerWrapper getWrapper();

    boolean isBridged(String room);

    Bridge getBridge(String room);

    void addBridge(Bridge bridge);

    void removeBridge(Bridge bridge);

}
