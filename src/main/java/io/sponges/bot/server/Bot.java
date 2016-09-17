package io.sponges.bot.server;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.database.Database;

public interface Bot {

    Logger LOGGER = new LoggerImpl();
    
    Server getServer();

    EventManager getEventManager();

    Database getDatabase();

    CommandManager getCommandManager();

    ClientManager getClientManager();

    ModuleManager getModuleManager();

    WebhookManager getWebhookManager();

    void stop();

}
