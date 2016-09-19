package io.sponges.bot.server;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.entities.message.ReceivedMessage;
import io.sponges.bot.api.event.events.message.MessageReceivedEvent;
import io.sponges.bot.api.event.events.user.UserJoinEvent;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.cmd.CommandHandler;
import io.sponges.bot.server.config.Configuration;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.PostgreSQL;
import io.sponges.bot.server.entities.manager.ClientManagerImpl;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.module.ModuleManagerImpl;
import io.sponges.bot.server.protocol.msg.StopMessage;
import io.sponges.bot.server.protocol.parser.framework.ParserManager;
import io.sponges.bot.server.server.ServerImpl;
import io.sponges.bot.server.webhook.server.WebhookServer;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ConstantConditions")
public class BotImpl implements Bot {

    private final ServerImpl server;
    private final EventBus eventBus;
    private final Database database;
    private final CommandHandler commandHandler;
    private final ClientManager clientManager;
    private final ModuleManager moduleManager;
    private final WebhookServer webhookServer;
    private final WebhookManager webhookManager;

    private BotImpl() throws IOException {
        LOGGER.setDebug(false);

        JSONObject config = new Configuration().load(new File("config.json"));
        JSONObject server = config.getJSONObject("server");
        int port = server.getInt("port");

        this.eventBus = new EventBus();
        this.database = new PostgreSQL();
        this.commandHandler = new CommandHandler(this);
        this.server = new ServerImpl(this, port);
        this.clientManager = new ClientManagerImpl();

        this.webhookServer = new WebhookServer(config.getJSONObject("webhook-server").getInt("port"));
        this.webhookManager = this.webhookServer.getWebhookManager();

        ParserManager parserManager = new ParserManager(this);

        eventBus.register(ClientInputEvent.class, parserManager::onClientInput);
        eventBus.register(MessageReceivedEvent.class, event -> {
            Client client = event.getClient();
            Network network = event.getNetwork();
            Channel channel = event.getChannel();
            User user = event.getUser();
            ReceivedMessage message = event.getMessage();
            String content = message.getContent();
            LOGGER.log("CHAT", String.format("[%s %s %s] %s: %s", client.getId(), network.getId(), channel.getId(),
                    user.getId(), content));
            commandHandler.onUserChat(event);
        });
        eventBus.register(UserJoinEvent.class, (event) -> LOGGER.log(Logger.Type.DEBUG, event.getUser().getId() + " joined!"));

        this.moduleManager = new ModuleManagerImpl(this);

        // Starting the actual server
        this.server.start(() -> LOGGER.log(Logger.Type.DEBUG, "Started!"));
    }

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
            new StopMessage(client).send();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.webhookServer.stop();
        this.server.stop(() -> LOGGER.log(Logger.Type.INFO, "Server closed!"));
        System.exit(-1);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public Database getDatabase() {
        return database;
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
    public WebhookManager getWebhookManager() {
        return webhookManager;
    }
}
