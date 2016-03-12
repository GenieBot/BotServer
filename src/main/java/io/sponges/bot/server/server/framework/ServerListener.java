package io.sponges.bot.server.server.framework;

import io.netty.channel.ChannelHandlerContext;

public interface ServerListener {

    void onConnect(ChannelHandlerContext context);

    void onDisconnect(ChannelHandlerContext context);

    void onMessage(ChannelHandlerContext context, String message);

    void onError(ChannelHandlerContext context, Throwable cause);

}
