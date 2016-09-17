package io.sponges.bot.server.database.statement;

import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.Statements;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract class AbstractStatement<T> {

    protected final Database database;
    private final Statements statement;

    public AbstractStatement(Database database, Statements statement) {
        this.database = database;
        this.statement = statement;
    }

    public T executeSync() throws Exception {
        return execute();
    }

    public Future<T> executeAsync() throws Exception {
        return ((Callable<Future<T>>) () -> Database.EXECUTOR_SERVICE.submit(AbstractStatement.this::execute)).call();
    }

    protected abstract T execute() throws Exception;

    protected String sql() {
        return statement.getContent();
    }
}
