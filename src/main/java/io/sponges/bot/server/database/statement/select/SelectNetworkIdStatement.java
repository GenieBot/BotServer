package io.sponges.bot.server.database.statement.select;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SelectNetworkIdStatement extends AbstractStatement<UUID> {

    private final UUID clientId;
    private final String networkSourceId;

    public SelectNetworkIdStatement(Database database, UUID clientId, String networkSourceId) {
        super(database, Statements.SELECT_NETWORK_ID);
        this.clientId = clientId;
        this.networkSourceId = networkSourceId;
    }

    @Override
    protected UUID execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, clientId);
            statement.setString(2, networkSourceId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return UUID.fromString(resultSet.getString(1));
        }
    }
}
