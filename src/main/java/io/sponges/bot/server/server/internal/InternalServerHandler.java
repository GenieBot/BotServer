package io.sponges.bot.server.server.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

class InternalServerHandler extends SimpleChannelInboundHandler<String> {

    private final InternalServerListener listener;

    InternalServerHandler(InternalServerListener listener) {
        this.listener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.newSucceededFuture().addListener(channelFuture -> {
            listener.onConnect(channelHandlerContext);
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        listener.onDisconnect(channelHandlerContext);
    }

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        listener.onMessage(channelHandlerContext, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        listener.onError(channelHandlerContext, cause);
    }
}
