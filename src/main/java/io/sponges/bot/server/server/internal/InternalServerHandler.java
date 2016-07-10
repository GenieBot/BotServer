package io.sponges.bot.server.server.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public final class InternalServerHandler extends SimpleChannelInboundHandler<String> {

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final InternalServerImpl server;

    public InternalServerHandler(InternalServerImpl server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.newSucceededFuture().addListener(channelFuture -> {
            channels.add(channelHandlerContext.channel());
            for (InternalServerListener listener : server.getListeners()) {
                listener.onConnect(channelHandlerContext);
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        for (InternalServerListener listener : server.getListeners()) {
            listener.onDisconnect(channelHandlerContext);
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        for (InternalServerListener listener : server.getListeners()) {
            listener.onMessage(channelHandlerContext, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        for (InternalServerListener listener : server.getListeners()) {
            listener.onError(channelHandlerContext, cause);
        }
    }
}
