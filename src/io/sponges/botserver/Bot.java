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
     * Checks to see if a client with the specified id is connected
     * @param id the client id to check
     * @return if client is connected
     */
    boolean isClient(String id);

    /**
     * Returns the client instance with the specified id
     * @param id the id of the client
     * @return client instance
     */
    Client getClient(String id);

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
     * Add a chat bridge 'link' between two rooms
     * @param clientId the id of the client bridging from
     * @param clientRoom the room bringing chat from
     * @param target the target client that has the room in
     * @param targetRoom the room to bridge into
     * @return command response
     *
     * TODO move to bridge manager
     */
    String addLink(String clientId, String clientRoom, String target, String targetRoom);

    /**
     * The ParserManager instance
     * @return parserManager
     */
    ParserManager getParserManager();

}
