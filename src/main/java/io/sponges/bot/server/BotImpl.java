package io.sponges.bot.server;

import io.sponges.bot.server.cmd.framework.CommandHandler;
import io.sponges.bot.server.config.Configuration;
import io.sponges.bot.server.event.events.*;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.internal.Server;
import io.sponges.bot.server.internal.ServerImpl;
import io.sponges.bot.server.parser.framework.ParserManager;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.impl.DatabaseImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotImpl implements Bot {

    private final Map<String, Client> clients = new HashMap<>();

    private final Server server;
    private final EventBus eventBus;
    private final CommandHandler commandHandler;
    private final Database database;
    private final ParserManager parserManager;

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
        this.database = new DatabaseImpl();
        this.commandHandler = new CommandHandler(this);
        this.server = new ServerImpl(this, host, port, channels);

        BotListener botListener = new BotListener(this, server, database);
        this.eventBus.register(ClientInputEvent.class, botListener::onClientInput);
        this.eventBus.register(ConnectEvent.class, botListener::onConnect);
        this.eventBus.register(ChatMessageEvent.class, botListener::onChatMessage);
        this.eventBus.register(UserJoinEvent.class, botListener::onUserJoin);
        this.eventBus.register(CommandRequestEvent.class, botListener::onCommandRequest);

        this.parserManager = new ParserManager();

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

    @Override
    public ParserManager getParserManager() {
        return parserManager;
    }
}
