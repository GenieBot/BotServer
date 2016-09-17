package io.sponges.bot.server.database.statement.insert;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertClientStatement extends AbstractStatement<Boolean> {

    private final UUID uuid;
    private final String sourceId;

    public InsertClientStatement(Database database, UUID uuid, String sourceId) {
        super(database, Statements.INSERT_CLIENT);
        this.uuid = uuid;
        this.sourceId = sourceId;
    }

    @Override
    protected Boolean execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, uuid);
            statement.setString(2, sourceId);
            return statement.execute();
        }
    }
}
