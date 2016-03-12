package io.sponges.bot.server.server.framework.impl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public final class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ServerImpl server;

    public ServerInitializer(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // text line codec
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder()); // could add charset here
        pipeline.addLast(new StringEncoder());

        pipeline.addLast(new ServerHandler(server));
    }

}
