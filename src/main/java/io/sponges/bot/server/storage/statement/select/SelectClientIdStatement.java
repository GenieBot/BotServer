package io.sponges.bot.server.storage.statement.select;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SelectClientIdStatement extends AbstractStatement<UUID> {

    private final String clientSourceId;

    public SelectClientIdStatement(Database database, String clientSourceId) {
        super(database, Statements.SELECT_CLIENT_ID);
        this.clientSourceId = clientSourceId;
    }

    @Override
    protected UUID execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setString(1, clientSourceId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return UUID.fromString(resultSet.getString(1));
        }
    }
}
