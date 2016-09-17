package io.sponges.bot.server.storage.statement.select;

import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.Statements;
import io.sponges.bot.server.storage.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectModuleIdStatement extends AbstractStatement<Integer> {

    private final String name;

    public SelectModuleIdStatement(Database database, String name) {
        super(database, Statements.SELECT_MODULE_ID);
        this.name = name;
    }

    @Override
    protected Integer execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return resultSet.getInt(1);
        }
    }
}
