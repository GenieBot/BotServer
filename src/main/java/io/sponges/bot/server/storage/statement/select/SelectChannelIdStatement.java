package io.sponges.bot.server.storage.statement.select;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SelectChannelIdStatement extends AbstractStatement<UUID> {

    private final UUID networkId;
    private final String channelSourceId;

    public SelectChannelIdStatement(Database database, UUID networkId, String channelSourceId) {
        super(database, Statements.SELECT_CHANNEL_ID);
        this.networkId = networkId;
        this.channelSourceId = channelSourceId;
    }

    @Override
    protected UUID execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            statement.setString(2, channelSourceId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return UUID.fromString(resultSet.getString(1));
        }
    }
}
