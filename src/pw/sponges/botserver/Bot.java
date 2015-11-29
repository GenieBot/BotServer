package pw.sponges.botserver;

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

public class Bot {

    private final Server server;
    private final Map<String, Client> clients;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final Database database;

    public Bot() {
        this.server = new ServerImpl(this);
        this.clients = new HashMap<>();
        this.database = new DatabaseImpl();
        this.commandHandler = new CommandHandler(this, database.getPermissions());
        this.eventManager = new EventManager(database);
        this.eventManager.setListener(new BotListener(this, database.getPermissions()));

        try {
            this.server.start();
        } catch (CertificateException | InterruptedException | SSLException e) {
            e.printStackTrace();
        }
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
