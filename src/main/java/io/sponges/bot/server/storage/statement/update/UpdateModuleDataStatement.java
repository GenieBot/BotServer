package io.sponges.bot.server.storage.statement.update;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;
import org.json.JSONObject;

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
            statement.setObject(1, networkId);
            statement.setInt(2, moduleId);
            statement.setString(3, data.toString());
            return statement.executeUpdate();
        }
    }
}
