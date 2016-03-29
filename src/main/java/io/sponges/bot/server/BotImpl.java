package io.sponges.bot.server;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.event.events.channel.ChannelTopicChangeEvent;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.cmd.CommandHandler;
import io.sponges.bot.server.cmd.CommandManagerImpl;
import io.sponges.bot.server.config.Configuration;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.manager.ClientManagerImpl;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.event.framework.EventManagerImpl;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.module.ModuleManagerImpl;
import io.sponges.bot.server.protocol.msg.StopMessage;
import io.sponges.bot.server.protocol.parser.framework.ParserManager;
import io.sponges.bot.server.server.ServerImpl;
import io.sponges.bot.server.storage.StorageImpl;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;

public class BotImpl implements Bot {

    private final ServerImpl server;
    private final EventBus eventBus;
    private final EventManager eventManager;
    private final CommandManagerImpl commandManager;
    private final CommandHandler commandHandler;
    private final ClientManager clientManager;
    private final ParserManager parserManager;
    private final ModuleManager moduleManager;
    private final Storage storage;

    /**
     * Constructor initiated in the main method
     */
    public BotImpl() throws IOException {
        JSONObject config = new Configuration().load(new File("config.json"));
        JSONObject server = config.getJSONObject("server");
        int port = server.getInt("port");

        this.eventBus = new EventBus();
        this.eventManager = new EventManagerImpl(this.eventBus);
        this.commandHandler = new CommandHandler(eventManager);
        this.commandManager = new CommandManagerImpl(this);
        this.server = new ServerImpl(this, port);
        this.clientManager = new ClientManagerImpl();
        this.parserManager = new ParserManager(this);

        this.eventBus.register(ClientInputEvent.class, parserManager::onClientInput);
        this.eventBus.register(UserChatEvent.class, userChatEvent -> {
            Client client = userChatEvent.getClient();
            Network network = userChatEvent.getNetwork();
            Channel channel = userChatEvent.getChannel();
            User user = userChatEvent.getUser();
            Message message = userChatEvent.getMessage();
            String content = message.getContent();
            System.out.printf("[%s] [%s - %s] %s: %s\r\n", client.getId(), network.getId(), channel.getId(),
                    user.getId(), content);
        });
        this.eventBus.register(UserChatEvent.class, commandHandler::onUserChat);
        this.eventBus.register(UserJoinEvent.class, (event) -> System.out.println(event.getUser().getId() + " joined!"));
        this.eventBus.register(ChannelTopicChangeEvent.class, (event) -> {
            System.out.println(event.getUser().getId() + " changed topic to " + event.getNewTopic() + "!"
                    + (event.getOldTopic().isPresent() ? " Old topic: " + event.getOldTopic().get() : ""));
        });

        JSONObject redis = config.getJSONObject("redis");
        this.storage = new StorageImpl(redis.getString("host"), redis.getInt("port"));

        this.moduleManager = new ModuleManagerImpl(this.server, eventManager, commandManager, storage);

        // Starting the actual server
        this.server.start(() -> System.out.println("Started!"));
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
    public void stop() {
        for (Client client : clientManager.getClients().values()) {
            new StopMessage(client).send((ClientImpl) client);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            StorageImpl storage = (StorageImpl) this.storage;
            try (Jedis jedis = storage.getPool().getResource()) {
                String response = jedis.save();
                System.out.println("Redis save: " + response);
            }
            storage.getPool().destroy();
        }).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.stop(() -> System.out.println("Server closed!"));
    }

    @Override
    public Server getServer() {
        return server;
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
    public ClientManager getClientManager() {
        return clientManager;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }
}
