package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.manager.NetworkModuleManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.module.ModuleManager;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.delete.DeleteEnabledModuleStatement;
import io.sponges.bot.server.database.statement.insert.InsertEnabledModuleStatement;
import io.sponges.bot.server.database.statement.select.SelectEnabledModulesStatement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NetworkModuleManagerImpl implements NetworkModuleManager {

    private final Set<Module> enabledModules = new HashSet<>();

    private final Network network;
    private final Database database;
    private final ModuleManager moduleManager;

    public NetworkModuleManagerImpl(Network network, Bot bot) {
        this.network = network;
        this.database = bot.getDatabase();
        this.moduleManager = bot.getModuleManager();
        Set<Integer> enabled;
        try {
            enabled = new SelectEnabledModulesStatement(database, network.getId()).executeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (enabled == null) {
            reset();
        } else {
            enabled.forEach(id -> {
                Module module = moduleManager.getModule(id);
                enabledModules.add(module);
            });
        }
    }

    @Override
    public Collection<Module> getEnabledModules() {
        return enabledModules;
    }

    @Override
    public boolean isEnabled(Module module) {
        return enabledModules.contains(module);
    }

    @Override
    public void setEnabled(Module module, boolean enabled) {
        if (module.isRequired() && !enabled) {
            throw new RuntimeException("Cannot disable module \"" + module.getId() + "\" as required.");
        }
        if (enabledModules.contains(module) && enabled) {
            Bot.LOGGER.log(Logger.Type.WARNING, "Module " + module.getId() + " is already enabled!");
            return;
        }
        if (enabled) {
            enabledModules.add(module);
            try {
                new InsertEnabledModuleStatement(database, network.getId(), module.getId()).executeAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            enabledModules.remove(module);
            try {
                new DeleteEnabledModuleStatement(database, network.getId(), module.getId()).executeAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reset() {
        moduleManager.getModules().forEach(module -> setEnabled(module, module.isRequired()));
    }
}
