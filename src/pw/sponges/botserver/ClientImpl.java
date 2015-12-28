package pw.sponges.botserver;

import org.json.JSONObject;
import pw.sponges.botserver.bridge.BridgeManager;
import pw.sponges.botserver.bridge.impl.BridgeManagerImpl;
import pw.sponges.botserver.framework.NetworkManager;
import pw.sponges.botserver.framework.impl.NetworkManagerImpl;
import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.messages.Message;
import pw.sponges.botserver.util.Msg;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id;
    private final ServerWrapper wrapper;
    private final NetworkManager networkManager;
    private final BridgeManager bridgeManager;

    public ClientImpl(String id, ServerWrapper wrapper) {
        this.id = id;
        this.wrapper = wrapper;
        this.networkManager = new NetworkManagerImpl(this);
        this.bridgeManager = new BridgeManagerImpl();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void sendMessage(Message message) {
        Msg.debug("[Sending message] " + message.toString());
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
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public String toString() {
        return new JSONObject().put("id", id).put("wrapper", wrapper.toString()).put("bridge", bridgeManager.toString()).toString();
    }
}
