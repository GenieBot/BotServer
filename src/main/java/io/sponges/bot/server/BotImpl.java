package io.sponges.bot.server;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.event.events.server.ClientConnectEvent;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.server.cmd.CommandHandler;
import io.sponges.bot.server.cmd.CommandManagerImpl;
import io.sponges.bot.server.config.Configuration;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.event.framework.EventManagerImpl;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.server.ServerImpl;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.impl.DatabaseImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotImpl implements Bot {

    private final ServerImpl server;
    private final EventBus eventBus;
    private final EventManager eventManager;
    private final CommandManagerImpl commandManager;
    private final CommandHandler commandHandler;
    private final Database database;

    /**
     * Constructor initiated in the main method
     */
    public BotImpl() throws IOException {
        JSONObject config = new Configuration().load(new File("config.json"));
        JSONObject redis = config.getJSONObject("redis");
        String host = redis.getString("host");
        int port = redis.getInt("port");

        JSONArray arrayChannels = config.getJSONArray("channels");
        List<String> channels = new ArrayList<>();
        for (int i = 0; i < arrayChannels.length(); i++) {
            channels.add(arrayChannels.getString(i));
        }

        // TODO deal with NPEs if not yet configured

        this.eventBus = new EventBus();
        this.eventManager = new EventManagerImpl(this.eventBus);
        this.database = new DatabaseImpl();
        this.commandHandler = new CommandHandler();
        this.commandManager = new CommandManagerImpl(this.commandHandler);
        this.server = new ServerImpl(this, host, port, channels);

        BotListener botListener = new BotListener(this, server, database);
        this.eventManager.register(ClientInputEvent.class, botListener::onClientInput);
        this.eventManager.register(ClientConnectEvent.class, botListener::onClientConnect);
        this.eventManager.register(UserChatEvent.class, botListener::onUserChatMessage);

        // Starting the actual server
        this.server.start();
    }

    /**
     * Java application main method
     */
    public static void main(String[] args) {
        try {
            new BotImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
