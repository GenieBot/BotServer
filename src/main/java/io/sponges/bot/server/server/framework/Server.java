package io.sponges.bot.server.server.framework;

import io.sponges.bot.server.server.framework.exception.ServerAlreadyRunningException;
import io.sponges.bot.server.server.framework.exception.ServerNotRunningException;

public interface Server {

    void start(Runnable runnable) throws ServerAlreadyRunningException, InterruptedException;

    void stop(Runnable runnable) throws ServerNotRunningException;

    void registerListener(ServerListener listener);

    void unregisterListener(ServerListener listener);

}
