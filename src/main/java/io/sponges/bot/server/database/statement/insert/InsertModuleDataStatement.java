package io.sponges.bot.server.database.statement.insert;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertModuleDataStatement extends AbstractStatement<Boolean> {

    private final UUID networkId;
    private final int moduleId;
    private final JSONObject json;

    public InsertModuleDataStatement(Database database, UUID networkId, int moduleId, JSONObject json) {
        super(database, Statements.INSERT_MODULE_DATA);
        this.networkId = networkId;
        this.moduleId = moduleId;
        this.json = json;
    }

    @Override
    protected Boolean execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            statement.setInt(2, moduleId);
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(json.toString());
            statement.setObject(3, jsonObject);
            return statement.execute();
        }
    }
}
