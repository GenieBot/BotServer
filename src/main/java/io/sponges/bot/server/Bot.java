package io.sponges.bot.server;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.cmd.CommandHandler;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.event.framework.EventBus;

public interface Bot {

    Logger LOGGER = new LoggerImpl();
    
    Server getServer();

    EventBus getEventBus();

    Database getDatabase();

    CommandHandler getCommandHandler();

    ClientManager getClientManager();

    ModuleManager getModuleManager();

    WebhookManager getWebhookManager();

    void stop();

}
