package io.sponges.bot.server.module;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.cmd.CommandHandler;
import io.sponges.bot.server.cmd.ModuleCommandManager;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.insert.InsertModuleStatement;
import io.sponges.bot.server.database.statement.select.SelectModuleIdStatement;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.event.framework.ModuleEventManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ModuleManagerImpl implements ModuleManager {

    private final Map<Integer, Module> modules = new HashMap<>();
    private final Map<String, Integer> ids = new HashMap<>();

    private final Database database;
    private final EventBus eventBus;
    private final Server server;
    private final CommandHandler commandHandler;
    private final ClientManager clientManager;
    private final WebhookManager webhookManager;

    public ModuleManagerImpl(Bot bot) {
        this.database = bot.getDatabase();
        this.eventBus = bot.getEventBus();
        this.server = bot.getServer();
        this.commandHandler = bot.getCommandHandler();
        this.clientManager = bot.getClientManager();
        this.webhookManager = bot.getWebhookManager();
        load();
    }

    @Override
    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    @Override
    public boolean isModule(int id) {
        return modules.containsKey(id);
    }

    @Override
    public boolean isModule(String s) {
        return ids.containsKey(s.toLowerCase());
    }

    @Override
    public Module getModule(int id) {
        return modules.get(id);
    }

    @Override
    public int getModuleId(String name) {
        return ids.get(name.toLowerCase());
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
    }

    public void register(Module module) {
        String name = module.getName().toLowerCase();
        int id;
        try {
            id = new SelectModuleIdStatement(database, name).executeAsync().get();
        } catch (Exception e) {
            if (!(e instanceof NullPointerException)) {
                e.printStackTrace();
                return;
            }
            try {
                new InsertModuleStatement(database, name).executeSync();
                id = new SelectModuleIdStatement(database, name).executeAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
        modules.put(id, module);
        ids.put(name, id);
        module.init(id, server, new ModuleEventManager(eventBus, module),
                new ModuleCommandManager(module, commandHandler), this, clientManager, webhookManager, Bot.LOGGER,
                new ModuleDataImpl(database, module));
        module.getLogger().log(Logger.Type.INFO, "Enabling " + name + " (" + module.getId() + ") version " + module.getVersion());
        try {
            module.onEnable();
        } catch (NoSuchMethodError | NoClassDefFoundError error) {
            error.printStackTrace();
            modules.remove(module.getId());
            ids.remove(module.getName().toLowerCase());
        }
    }

    public void unregister(Map<Integer, Module> modules, Module module) {
        modules.remove(module.getId());
        module.getLogger().log(Logger.Type.INFO, "Disabling " + module.getId() + " version " + module.getVersion());
        module.onDisable();
        module.getCommandManager().unregisterAllCommands();
        module.getEventManager().unregisterAll();
    }

}
