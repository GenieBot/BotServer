package io.sponges.bot.server.module;

import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.api.server.Server;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManagerImpl implements ModuleManager {

    private Map<String, Module> modules = new ConcurrentHashMap<>();

    private final Server server;
    private final EventManager eventManager;
    private final CommandManager commandManager;

    public ModuleManagerImpl(Server server, EventManager eventManager, CommandManager commandManager) {
        this.server = server;
        this.eventManager = eventManager;
        this.commandManager = commandManager;

        load();
    }

    @Override
    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
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
            System.out.println("Loaded " + modules.size() + " modules!");
        } else {
            System.out.println("Found no modules to load!");
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
        module.init(server, eventManager, commandManager, this);
        module.getLogger().log("Enabling " + module.getId() + " version " + module.getVersion());
        module.onEnable();
    }

    public void unregister(Map<String, Module> modules, Module module) {
        modules.remove(module.getId().toLowerCase());
        module.getLogger().log("Disabling " + module.getId() + " version " + module.getVersion());
        module.onDisable();
        module.getCommandManager().unregisterCommands(module);
        module.getEventManager().unregister(module);
    }

}
