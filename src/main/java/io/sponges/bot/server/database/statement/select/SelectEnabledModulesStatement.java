package io.sponges.bot.server.database.statement.select;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;
import io.sponges.bot.server.database.statement.AbstractStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SelectEnabledModulesStatement extends AbstractStatement<Set<Integer>> {

    private final UUID networkId;

    public SelectEnabledModulesStatement(Database database, UUID networkId) {
        super(database, Statements.SELECT_ENABLED_MODULES);
        this.networkId = networkId;
    }

    @Override
    protected Set<Integer> execute() throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql());
            statement.setObject(1, networkId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return null;
            }
            Set<Integer> results = new HashSet<>();
            while (resultSet.next()) {
                results.add(resultSet.getInt(1));
            }
            return results;
        }
    }
}
