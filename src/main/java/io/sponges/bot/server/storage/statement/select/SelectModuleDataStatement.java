package io.sponges.bot.server.storage.statement.select;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SelectModuleDataStatement extends AbstractStatement<JSONObject> {

    private final UUID networkId;
    private final int moduleId;

    public SelectModuleDataStatement(Database database, UUID networkId, int moduleId) {
        super(database, Statements.SELECT_MODULE_DATA);
        this.networkId = networkId;
        this.moduleId = moduleId;
    }

    @Override
    protected JSONObject execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            statement.setInt(2, moduleId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new JSONObject(resultSet.getString(1));
        }
    }
}
