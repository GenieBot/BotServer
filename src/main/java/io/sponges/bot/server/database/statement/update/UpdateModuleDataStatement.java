package io.sponges.bot.server.database.statement.update;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class UpdateModuleDataStatement extends AbstractStatement<Integer> {

    private final UUID networkId;
    private final int moduleId;
    private final JSONObject data;

    public UpdateModuleDataStatement(Database database, UUID networkId, int moduleId, JSONObject data) {
        super(database, Statements.UPDATE_MODULE_DATA);
        this.networkId = networkId;
        this.moduleId = moduleId;
        this.data = data;
    }

    @Override
    protected Integer execute() throws Exception {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(data.toString());
            statement.setObject(1, jsonObject);
            statement.setObject(2, networkId);
            statement.setInt(3, moduleId);
            return statement.executeUpdate();
        }
    }
}
