package io.sponges.bot.server.database.statement.insert;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertModuleStatement extends AbstractStatement<Boolean> {

    private final String name;

    public InsertModuleStatement(Database database, String name) {
        super(database, Statements.INSERT_MODULE);
        this.name = name;
    }

    @Override
    protected Boolean execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setString(1, name);
            return statement.execute();
        }
    }
}
