package io.sponges.bot.server.module;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.Bot;
import io.sponges.proxypool.ProxyPool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManagerImpl implements ModuleManager {

    private Map<String, Module> modules = new ConcurrentHashMap<>();

    private final Server server;
    private final EventManager eventManager;
    private final CommandManager commandManager;
    private final Storage storage;
    private final ProxyPool proxyPool;
    private final ClientManager clientManager;
    private final WebhookManager webhookManager;

    public ModuleManagerImpl(Bot bot) {
        this.server = bot.getServer();
        this.eventManager = bot.getEventManager();
        this.commandManager = bot.getCommandManager();
        this.storage = bot.getStorage();
        this.proxyPool = bot.getProxyPool();
        this.clientManager = bot.getClientManager();
        this.webhookManager = bot.getWebhookManager();
        load();
    }

    @Override
    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    @Override
    public boolean isModule(String s) {
        return modules.containsKey(s);
    }

    @Override
    public Module getModule(String s) {
        return modules.get(s.toLowerCase());
    }

    private void load() {
        this.modules.clear();
        List<Module> modules = null;
        try {
            modules = new ModuleLoader().load(new File("modules"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (modules != null && !modules.isEmpty()) {
            modules.forEach(this::register);
            Bot.LOGGER.log(Logger.Type.INFO, "Loaded " + modules.size() + " modules!");
        } else {
            Bot.LOGGER.log(Logger.Type.WARNING, "Found no modules to load!");
        }
    }

    @Override
    public void reload() {
        Map<String, Module> modules = this.modules;
        for (Module module : this.modules.values()) {
            unregister(modules, module);
        }
        this.modules = modules;
        load();
    }

    public void register(Module module) {
        modules.put(module.getId().toLowerCase(), module);
        module.init(server, eventManager, commandManager, this, storage, proxyPool, clientManager, webhookManager, Bot.LOGGER);
        module.getLogger().log(Logger.Type.INFO, "Enabling " + module.getId() + " version " + module.getVersion());
        try {
            module.onEnable();
        } catch (NoClassDefFoundError error) {
            error.printStackTrace();
            modules.remove(module.getId().toLowerCase());
        }
    }

    public void unregister(Map<String, Module> modules, Module module) {
        modules.remove(module.getId().toLowerCase());
        module.getLogger().log(Logger.Type.INFO, "Disabling " + module.getId() + " version " + module.getVersion());
        module.onDisable();
        module.getCommandManager().unregisterCommands(module);
        module.getEventManager().unregister(module);
    }

}
