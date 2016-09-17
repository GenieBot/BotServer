package io.sponges.bot.server.storage.statement.insert;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;
import org.json.JSONObject;

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
            statement.setString(3, json.toString());
            return statement.execute();
        }
    }
}
