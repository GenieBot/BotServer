package io.sponges.bot.server;

import io.sponges.bot.server.cmd.framework.CommandHandler;
import io.sponges.bot.server.event.framework.EventManager;
import io.sponges.bot.server.parser.framework.ParserManager;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.internal.Server;

import java.util.Map;

/**
 * Main class
 */
public interface Bot {

    /**
     * Returns the Server instance
     * @return server
     */
    Server getServer();

    /**
     * Returns the Database instance
     * @return database
     */
    Database getDatabase();

    /**
     * Returns a Map with with connected clients
     * Map contains the client id String as the key, and the Client instance as the value
     * @return clients
     */
    Map<String, Client> getClients();

    /**
     * The EventManager instance
     * @return eventManager
     */
    EventManager getEventManager();

    /**
     * The CommandHandler instance
     * @return commandHandler
     */
    CommandHandler getCommandHandler();

    /**
     * The ParserManager instance
     * @return parserManager
     */
    ParserManager getParserManager();

}
