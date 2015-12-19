package pw.sponges.botserver.bridge.impl;

import pw.sponges.botserver.bridge.Bridge;
import pw.sponges.botserver.bridge.BridgeManager;

import java.util.HashMap;
import java.util.Map;

public class BridgeManagerImpl implements BridgeManager {

    private final Map<String, Bridge> bridges;

    public BridgeManagerImpl() {
        this.bridges = new HashMap<>();
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
        bridges.remove(bridge.getTargetRoom());
    }

}
