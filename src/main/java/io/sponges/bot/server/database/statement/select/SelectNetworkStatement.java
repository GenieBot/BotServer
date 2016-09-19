package io.sponges.bot.server.database.statement.select;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SelectNetworkStatement extends AbstractStatement<String[]> {

    private final UUID networkId;

    public SelectNetworkStatement(Database database, UUID networkId) {
        super(database, Statements.SELECT_NETWORK);
        this.networkId = networkId;
    }

    @Override
    protected String[] execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new String[]{ resultSet.getString(1), resultSet.getString(2) };
        }
    }
}
