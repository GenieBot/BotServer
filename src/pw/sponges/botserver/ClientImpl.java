package pw.sponges.botserver;

import org.json.JSONObject;
import pw.sponges.botserver.bridge.BridgeManager;
import pw.sponges.botserver.bridge.impl.BridgeManagerImpl;
import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.messages.Message;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id;
    private final ServerWrapper wrapper;
    private final BridgeManager bridgeManager;

    public ClientImpl(String id, ServerWrapper wrapper) {
        this.id = id;
        this.wrapper = wrapper;
        this.bridgeManager = new BridgeManagerImpl();
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
    public BridgeManager getBridgeManager() {
        return bridgeManager;
    }

    @Override
    public String toString() {
        return new JSONObject().put("id", id).put("wrapper", wrapper.toString()).put("bridge", bridgeManager.toString()).toString();
    }
}
