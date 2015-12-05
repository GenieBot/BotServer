package pw.sponges.botserver;

import pw.sponges.botserver.bridge.Bridge;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.event.framework.EventManager;
import pw.sponges.botserver.internal.Server;
import pw.sponges.botserver.internal.impl.ServerImpl;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.impl.DatabaseImpl;
import pw.sponges.botserver.util.Msg;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class
 * TODO abstraction
 */
public class Bot {

    private final Server server;
    private final Map<String, Client> clients;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final Database database;

    /**
     * Constructor initiated in the main method
     */
    public Bot() {
        // Instantiating all the variables
        this.server = new ServerImpl(this);
        this.clients = new HashMap<>();
        this.database = new DatabaseImpl();
        this.commandHandler = new CommandHandler(this, database.getPermissions());
        this.eventManager = new EventManager(database);
        this.eventManager.setListener(new BotListener(this, database.getPermissions()));

        // Starting the actual server
        try {
            this.server.start();
        } catch (CertificateException | InterruptedException | SSLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java application main method
     */
    public static void main(String[] args) {
        new Bot();
    }

    /**
     * Returns the Server instance
     * @return server
     */
    public Server getServer() {
        return server;
    }

    /**
     * Returns the Database instance
     * @return database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Returns a Map with with connected clients
     * Map contains the client id String as the key, and the Client instance as the value
     * @return clients
     */
    public Map<String, Client> getClients() {
        return clients;
    }

    /**
     * Checks to see if a client with the specified id is connected
     * @param id the client id to check
     * @return if client is connected
     */
    public boolean isClient(String id) {
        return clients.containsKey(id);
    }

    /**
     * Returns the client instance with the specified id
     * @param id the id of the client
     * @return client instance
     */
    public Client getClient(String id) {
        return clients.get(id);
    }

    /**
     * The EventManager instance
     * @return eventManager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * The CommandHandler instance
     * @return commandHandler
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Add a chat bridge 'link' between two rooms
     * @param clientId the id of the client bridging from
     * @param clientRoom the room bringing chat from
     * @param target the target client that has the room in
     * @param targetRoom the room to bridge into
     * @return command response
     */
    public String addLink(String clientId, String clientRoom, String target, String targetRoom) {
        // Check if the target client already exists
        boolean isClient = isClient(target);

        // Sanity check
        if (!isClient) {
            Msg.warning("Client " + clientId + " tried to bridge with invalid client " + target + "!");
            return "Client " + target + " is invalid!";
        }

        {
            // Origin client stuff
            Client client = getClient(clientId);

            boolean isBridged = client.isBridged(clientRoom);
            if (isBridged) {
                Msg.warning("The room " + clientRoom + " is already bridged!");
                return "The room " + clientRoom + " is already bridged!";
            }

            Bridge bridge = new Bridge(clientId, clientRoom, target, targetRoom);
            client.addBridge(bridge);
        }

        {
            // Target client stuff
            Client client = getClient(target);

            boolean isBridged = client.isBridged(target);
            if (isBridged) {
                Msg.warning("The room " + clientRoom + " is already bridged!");
                return "The room " + clientRoom + " is already bridged!";
            }

            Bridge bridge = new Bridge(target, targetRoom, clientId, clientRoom);
            client.addBridge(bridge);
        }

        Msg.debug("Added bridge from client " + clientId + " with room " + clientRoom + " to target " + target + " room " + targetRoom + "!");
        return "Added bridge!";
    }

}
