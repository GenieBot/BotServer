package io.sponges.bot.server.server.framework.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.ConcurrentSet;
import io.sponges.bot.server.server.framework.Server;
import io.sponges.bot.server.server.framework.exception.ServerAlreadyRunningException;
import io.sponges.bot.server.server.framework.ServerListener;
import io.sponges.bot.server.server.framework.exception.ServerNotRunningException;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ServerImpl implements Server {

    private final Object lock = new Object();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Set<ServerListener> listeners = new ConcurrentSet<>();

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public ServerImpl(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
    }

    @Override
    public void start(Runnable runnable) throws ServerAlreadyRunningException, InterruptedException {
        if (running.get()) {
            throw new ServerAlreadyRunningException();
        }
        running.set(true);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(this));
            ChannelFuture future = serverBootstrap.bind(port);
            runnable.run();
            future.sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            running.set(false);
        }
    }

    @Override
    public void stop(Runnable runnable) throws ServerNotRunningException {
        if (!running.get()) {
            throw new ServerNotRunningException();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        running.set(false);
        runnable.run();
    }

    @Override
    public void registerListener(ServerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(ServerListener listener) {
        listeners.remove(listener);
    }

    public Set<ServerListener> getListeners() {
        return listeners;
    }
}
