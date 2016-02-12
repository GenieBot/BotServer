package io.sponges.botserver;

import io.sponges.botserver.cmd.framework.CommandHandler;
import io.sponges.botserver.event.framework.EventManager;
import io.sponges.botserver.internal.Server;
import io.sponges.botserver.parser.framework.ParserManager;
import io.sponges.botserver.storage.Database;

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
