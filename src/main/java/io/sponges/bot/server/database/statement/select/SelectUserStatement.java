package io.sponges.bot.server.database.statement.select;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SelectUserStatement extends AbstractStatement<String[]> {

    private final UUID userId;

    public SelectUserStatement(Database database, UUID userId) {
        super(database, Statements.SELECT_USER);
        this.userId = userId;
    }

    @Override
    protected String[] execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new String[]{ resultSet.getString(1), resultSet.getString(2) };
        }
    }
}
