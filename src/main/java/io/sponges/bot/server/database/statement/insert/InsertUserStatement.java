package io.sponges.bot.server.database.statement.insert;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertUserStatement extends AbstractStatement<Boolean> {

    private final UUID uuid;
    private final String sourceId;
    private final UUID networkId;

    public InsertUserStatement(Database database, UUID uuid, String sourceId, UUID networkId) {
        super(database, Statements.INSERT_USER);
        this.uuid = uuid;
        this.sourceId = sourceId;
        this.networkId = networkId;
    }

    @Override
    protected Boolean execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, uuid);
            statement.setString(2, sourceId);
            statement.setObject(3, networkId);
            return statement.execute();
        }
    }
}
