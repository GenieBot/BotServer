package io.sponges.bot.server.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.sponges.bot.api.Logger;
import io.sponges.bot.server.Bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings({"unchecked", "Convert2Diamond"})
public class PostgreSQL implements Database {

    private final HikariDataSource dataSource;

    public PostgreSQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/test");
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername("postgres");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(config);
        try {
            loadStatements();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createDefaultTables();
    }

    public static void main(String[] args) {
        new PostgreSQL();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Bot.LOGGER.log(Logger.Type.DEBUG, "PostreSQL#getConnection invoked");
        return dataSource.getConnection();
    }

    private String readFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String input;
            while ((input = reader.readLine()) != null) {
                builder.append(input);
            }
            return builder.toString();
        }
    }

    private void loadStatements() throws IOException {
        File directory = new File("src/main/resources/sql");
        for (Statements statement : Statements.values()) {
            File file = new File(directory, statement.getFile());
            statement.setContent(readFile(file));
        }
    }

    private void createDefaultTables() {
        try {
            Statement statement = getConnection().createStatement();
            statement.addBatch(Statements.CREATE_CLIENTS_TABLE.toString());
            statement.addBatch(Statements.CREATE_NETWORKS_TABLE.toString());
            statement.addBatch(Statements.CREATE_CHANNELS_TABLE.toString());
            statement.addBatch(Statements.CREATE_USERS_TABLE.toString());
            statement.addBatch(Statements.CREATE_MODULES_TABLE.toString());
            statement.addBatch(Statements.CREATE_ENABLED_MODULES_TABLE.toString());
            statement.addBatch(Statements.CREATE_MODULE_DATA_TABLE.toString());
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
