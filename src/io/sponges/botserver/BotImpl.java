package io.sponges.botserver;

import io.sponges.botserver.bridge.Bridge;
import io.sponges.botserver.cmd.framework.CommandHandler;
import io.sponges.botserver.event.framework.EventManager;
import io.sponges.botserver.util.Msg;
import io.sponges.botserver.bridge.BridgeManager;
import io.sponges.botserver.internal.Server;
import io.sponges.botserver.internal.impl.ServerImpl;
import io.sponges.botserver.parser.framework.ParserManager;
import io.sponges.botserver.storage.Database;
import io.sponges.botserver.storage.impl.DatabaseImpl;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class BotImpl implements Bot {

    private final Server server;
    private final Map<String, Client> clients;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final Database database;
    private final ParserManager parserManager;

    /**
     * Constructor initiated in the main method
     */
    public BotImpl() {
        // Instantiating all the variables
        this.server = new ServerImpl(this);
        this.clients = new HashMap<>();
        this.database = new DatabaseImpl();
        this.commandHandler = new CommandHandler(this);
        this.eventManager = new EventManager(database);
        this.eventManager.setListener(new BotListener(this, database));
        this.parserManager = new ParserManager();

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
        new BotImpl();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    @Override
    public Map<String, Client> getClients() {
        return clients;
    }

    @Override
    public boolean isClient(String id) {
        return clients.containsKey(id);
    }

    @Override
    public Client getClient(String id) {
        return clients.get(id);
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public String addLink(String clientId, String clientRoom, String target, String targetRoom) {
        // Check if the target client already exists
        boolean isClient = isClient(target);

        // Sanity check
        if (!isClient) {
            Msg.warning("[Adding link] Client " + clientId + " tried to bridge with invalid client " + target + "!");
            return "Client " + target + " is invalid!";
        }

        {
            // Origin client stuff
            Client client = getClient(clientId);
            BridgeManager bridgeManager = client.getBridgeManager();

            boolean isBridged = bridgeManager.isBridged(clientRoom);
            if (isBridged) {
                Msg.warning("[Adding link] The room " + clientRoom + " is already bridged!");
                return "The room " + clientRoom + " is already bridged!\nTo remove it, use 'bridge remove'.";
            }

            Bridge bridge = new Bridge(clientId, clientRoom, target, targetRoom);
            bridgeManager.addBridge(bridge);
        }

        {
            // Target client stuff
            Client client = getClient(target);
            BridgeManager bridgeManager = client.getBridgeManager();

            boolean isBridged = bridgeManager.isBridged(target);
            if (isBridged) {
                Msg.warning("[Adding link] The room " + clientRoom + " is already bridged!");
                return "The room " + clientRoom + " is already bridged!";
            }

            Bridge bridge = new Bridge(target, targetRoom, clientId, clientRoom);
            bridgeManager.addBridge(bridge);
        }

        Msg.debug("[Adding link] Added bridge from client " + clientId + " with room " + clientRoom + " to target " + target + " room " + targetRoom + "!");
        return "Added bridge!";
    }

    @Override
    public ParserManager getParserManager() {
        return parserManager;
    }
}
