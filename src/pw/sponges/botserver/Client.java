package pw.sponges.botserver;

import pw.sponges.botserver.internal.ServerThread;
import pw.sponges.botserver.messages.Message;

import java.util.HashMap;
import java.util.Map;

public class Client {

    private final String id;
    private final ServerThread thread;

    //            room  bridge
    private Map<String, Bridge> bridges;

    //            room  prefix
    //private Map<String, String> prefixes;

    public Client(String id, ServerThread thread) {
        this.id = id;
        this.thread = thread;

        this.bridges = new HashMap<>();
        //this.prefixes = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public ServerThread getThread() {
        return thread;
    }

    public void sendMessage(String message) {
        thread.print(message);
    }

    public void sendMessage(Message message) {
        thread.print(message.getJSON().toString());
    }

    public Map<String, Bridge> getBridges() {
        return bridges;
    }

    public boolean isBridged(String room) {
        return bridges.containsKey(room);
    }

    public Bridge getBridge(String room) {
        return bridges.get(room);
    }

    public void addBridge(Bridge bridge) {
        bridges.put(bridge.getClientRoom(), bridge);
    }

    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge.getClientRoom());
    }

    /*public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public void setPrefix(String room, String prefix) {
        prefixes.put(room, prefix);
        sendMessage(new PrefixChangeMessage(this, room, prefix));
    }

    public String getPrefix(String room) {
        if (!prefixes.containsKey(room)) {
            String prefix = "#";
            prefixes.put(room, prefix);
            return prefix;
        }

        return prefixes.get(room);
    }*/
}
