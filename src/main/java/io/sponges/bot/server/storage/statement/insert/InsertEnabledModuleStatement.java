package io.sponges.bot.server.storage.statement.insert;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertEnabledModuleStatement extends AbstractStatement<Boolean> {

    private final UUID networkId;
    private final int moduleId;

    public InsertEnabledModuleStatement(Database database, UUID networkId, int moduleId) {
        super(database, Statements.INSERT_ENABLED_MODULE);
        this.networkId = networkId;
        this.moduleId = moduleId;
    }

    @Override
    protected Boolean execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            statement.setInt(2, moduleId);
            return statement.execute();
        }
    }
}
