package io.sponges.bot.server.server.internal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.ConcurrentSet;
import io.sponges.bot.api.Logger;
import io.sponges.bot.server.Bot;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public final class InternalServerImpl {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Set<InternalServerListener> listeners = new ConcurrentSet<>();

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public InternalServerImpl(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
    }

    public void start(Runnable runnable) throws InterruptedException {
        if (running.get()) {
            Bot.LOGGER.log(Logger.Type.WARNING, "already running");
            return;
        }
        running.set(true);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new InternalServerInitializer(this));
            ChannelFuture future = serverBootstrap.bind(port);
            runnable.run();
            future.sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            running.set(false);
        }
    }

    public void stop(Runnable runnable) {
        if (!running.get()) {
            Bot.LOGGER.log(Logger.Type.WARNING, "not running");
            return;
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        running.set(false);
        runnable.run();
    }

    public void registerListener(InternalServerListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(InternalServerListener listener) {
        listeners.remove(listener);
    }

    public Set<InternalServerListener> getListeners() {
        return listeners;
    }
}
