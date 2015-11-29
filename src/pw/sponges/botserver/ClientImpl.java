package pw.sponges.botserver;

import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.messages.Message;

import java.util.HashMap;
import java.util.Map;

public class ClientImpl implements Client {

    private final String id;
    private final ServerWrapper wrapper;
    private final Map<String, Bridge> bridges;

    public ClientImpl(String id, ServerWrapper wrapper) {
        this.id = id;
        this.wrapper = wrapper;
        this.bridges = new HashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void sendMessage(Message message) {
        wrapper.sendMessage(message.toString());
    }

    @Override
    public ServerWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public boolean isBridged(String room) {
        return bridges.containsKey(room);
    }

    @Override
    public Bridge getBridge(String room) {
        return bridges.get(room);
    }

    @Override
    public void addBridge(Bridge bridge) {
        bridges.put(bridge.getClientRoom(), bridge);
    }

    @Override
    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge.getClientRoom());
    }

}
