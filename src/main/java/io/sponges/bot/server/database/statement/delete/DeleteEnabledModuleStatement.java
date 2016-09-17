package io.sponges.bot.server.database.statement.delete;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class DeleteEnabledModuleStatement extends AbstractStatement<Boolean> {

    private final UUID networkId;
    private final int moduleId;

    public DeleteEnabledModuleStatement(Database database, UUID networkId, int moduleId) {
        super(database, Statements.DELETE_ENABLED_MODULE);
        this.networkId = networkId;
        this.moduleId = moduleId;
    }

    @Override
    protected Boolean execute() throws Exception {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            statement.setInt(2, moduleId);
            return statement.execute();
        }
    }
}
