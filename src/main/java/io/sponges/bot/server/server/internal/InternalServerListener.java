package io.sponges.bot.server.server.internal;

import io.netty.channel.ChannelHandlerContext;

public interface InternalServerListener {

    void onConnect(ChannelHandlerContext context);

    void onDisconnect(ChannelHandlerContext context);

    void onMessage(ChannelHandlerContext context, String message);

    void onError(ChannelHandlerContext context, Throwable cause);

}
