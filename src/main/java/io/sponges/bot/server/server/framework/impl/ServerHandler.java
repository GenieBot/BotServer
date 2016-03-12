package io.sponges.bot.server.server.framework.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.sponges.bot.server.server.framework.ServerListener;

public final class ServerHandler extends SimpleChannelInboundHandler<String> {

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final ServerImpl server;

    public ServerHandler(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.newSucceededFuture().addListener(channelFuture -> {
            channels.add(channelHandlerContext.channel());
            for (ServerListener listener : server.getListeners()) {
                listener.onConnect(channelHandlerContext);
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        for (ServerListener listener : server.getListeners()) {
            listener.onDisconnect(channelHandlerContext);
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        for (ServerListener listener : server.getListeners()) {
            listener.onMessage(channelHandlerContext, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        for (ServerListener listener : server.getListeners()) {
            listener.onError(channelHandlerContext, cause);
        }
    }
}
