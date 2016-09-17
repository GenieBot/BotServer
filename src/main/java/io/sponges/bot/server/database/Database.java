package io.sponges.bot.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Database {

    ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    Connection getConnection() throws SQLException;

}
