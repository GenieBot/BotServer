package io.sponges.bot.server.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQL {

    private final HikariDataSource dataSource;

    PostgreSQL() {
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

    private enum Statements {

        // create table
        CREATE_CLIENTS_TABLE("create_clients_table.sql"),
        CREATE_NETWORKS_TABLE("create_networks_table.sql"),
        CREATE_CHANNELS_TABLE("create_channels_table.sql"),
        CREATE_USERS_TABLE("create_users_table.sql"),
        CREATE_MODULES_TABLE("create_modules_table.sql"),
        CREATE_ENABLED_MODULES_TABLE("create_enabled_modules_table.sql"),
        CREATE_MODULE_DATA_TABLE("create_module_data_table.sql"),



        ;

        private final String file;

        private String content = null;

        Statements(String file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return content;
        }
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
            File file = new File(directory, statement.file);
            statement.content = readFile(file);
        }
    }

    private void createDefaultTables() {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(Statements.CREATE_CLIENTS_TABLE.toString());
            connection.createStatement().execute(Statements.CREATE_NETWORKS_TABLE.toString());
            connection.createStatement().execute(Statements.CREATE_CHANNELS_TABLE.toString());
            connection.createStatement().execute(Statements.CREATE_USERS_TABLE.toString());
            connection.createStatement().execute(Statements.CREATE_MODULES_TABLE.toString());
            connection.createStatement().execute(Statements.CREATE_ENABLED_MODULES_TABLE.toString());
            connection.createStatement().execute(Statements.CREATE_MODULE_DATA_TABLE.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
