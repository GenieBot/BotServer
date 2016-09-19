package io.sponges.bot.server.module;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.module.ModuleData;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.insert.InsertModuleDataStatement;
import io.sponges.bot.server.database.statement.select.SelectModuleDataStatement;
import org.json.JSONObject;

public class ModuleDataImpl implements ModuleData {

    private final Database database;
    private final Module module;

    public ModuleDataImpl(Database database, Module module) {
        this.database = database;
        this.module = module;
    }

    @Override
    public JSONObject get(Network network) {
        try {
            return new SelectModuleDataStatement(database, network.getId(), module.getId()).executeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Network network, JSONObject json) {
        try {
            new InsertModuleDataStatement(database, network.getId(), module.getId(), json).executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
