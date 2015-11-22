package pw.sponges.botserver;

import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.event.framework.EventManager;
import pw.sponges.botserver.internal.Server;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.impl.PermissionsManagerImpl;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.impl.DatabaseImpl;
import pw.sponges.botserver.util.Msg;

import java.util.HashMap;
import java.util.Map;

public class Bot {

    public static final boolean DEBUG = true;

    private final Server server;
    private final Map<String, Client> clients;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final Database database;
    private final PermissionsManager permissions;

    public Bot() {
        this.server = new Server(this);
        this.clients = new HashMap<>();
        this.database = new DatabaseImpl();
        this.permissions = new PermissionsManagerImpl(database);
        this.commandHandler = new CommandHandler(this);
        this.eventManager = new EventManager(database, permissions);
        this.eventManager.setListener(new BotListener(this, permissions));
        this.server.start();
    }

    public static void main(String[] args) {
        new Bot();
    }

    public Server getServer() {
        return server;
    }

    public Database getDatabase() {
        return database;
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public boolean isClient(String id) {
        return clients.containsKey(id);
    }

    public Client getClient(String id) {
        return clients.get(id);
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public String addLink(String clientId, String clientRoom, String target, String targetRoom) {
        boolean isClient = isClient(target);
        if (!isClient) {
            Msg.warning("Client " + clientId + " tried to bridge with invalid client " + target + "!");
            return "Client " + target + " is invalid!";
        }

        {
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
