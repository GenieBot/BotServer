package io.sponges.botserver;

import io.sponges.botserver.cmd.framework.CommandHandler;
import io.sponges.botserver.event.framework.EventManager;
import io.sponges.botserver.internal.Server;
import io.sponges.botserver.internal.ServerImpl;
import io.sponges.botserver.parser.framework.ParserManager;
import io.sponges.botserver.storage.Database;
import io.sponges.botserver.storage.impl.DatabaseImpl;

import java.util.HashMap;
import java.util.Map;

public class BotImpl implements Bot {

    private final Map<String, Client> clients = new HashMap<>();

    private final Server server;
    private final EventManager eventManager;
    private final CommandHandler commandHandler;
    private final Database database;
    private final ParserManager parserManager;

    /**
     * Constructor initiated in the main method
     */
    public BotImpl() {
        // Instantiating all the variables
        this.server = new ServerImpl(this, "localhost"); // TODO configurable
        this.database = new DatabaseImpl();
        this.commandHandler = new CommandHandler(this);
        this.eventManager = new EventManager(database);
        this.eventManager.setListener(new BotListener(this, server, database));
        this.parserManager = new ParserManager();

        // Starting the actual server
        this.server.start();
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
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public ParserManager getParserManager() {
        return parserManager;
    }
}
