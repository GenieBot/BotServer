package io.sponges.bot.server.storage.statement.insert;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertNetworkStatement extends AbstractStatement<Boolean> {

    private final UUID uuid;
    private final String sourceId;
    private final UUID clientId;

    public InsertNetworkStatement(Database database, UUID uuid, String sourceId, UUID clientId) {
        super(database, Statements.INSERT_NETWORK);
        this.uuid = uuid;
        this.sourceId = sourceId;
        this.clientId = clientId;
    }

    @Override
    protected Boolean execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, uuid);
            statement.setString(2, sourceId);
            statement.setObject(3, clientId);
            return statement.execute();
        }
    }
}
