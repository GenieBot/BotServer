package io.sponges.bot.server;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.server.cmd.CommandHandler;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.storage.Database;

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
     * The EventBus instance
     * @return eventBus
     */
    EventBus getEventBus();

    /**
     * The CommandHandler instance
     * @return commandHandler
     */
    CommandHandler getCommandHandler();

}
