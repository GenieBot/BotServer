package io.sponges.bot.server;

import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.cmd.CommandHandler;

public interface Bot {
    
    Server getServer();

    EventManager getEventManager();

    CommandHandler getCommandHandler();

    ClientManager getClientManager();

    ModuleManager getModuleManager();

    Storage getStorage();

    void stop();

}
