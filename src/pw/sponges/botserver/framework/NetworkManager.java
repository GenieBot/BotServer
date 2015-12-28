package pw.sponges.botserver.framework;

public interface NetworkManager {

    boolean isNetwork(String id);

    Network getNetwork(String id);

    Network getOrCreateNetwork(String id);

}
