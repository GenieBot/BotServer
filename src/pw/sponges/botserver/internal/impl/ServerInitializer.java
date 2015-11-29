package pw.sponges.botserver.internal.impl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import pw.sponges.botserver.Bot;

/**
 * This class is what should happen for each new connection to the server.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    // Passed via dependency injection to ServerHandler
    private final Bot bot;

    // Encryption for connection
    private final SslContext context;

    public ServerInitializer(Bot bot, SslContext context) {
        this.bot = bot;
        this.context = context;
    }

    /**
     * Method called as soon as the new connection is initiated.
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(context.newHandler(channel.alloc()));

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // and then business logic.
        pipeline.addLast(new ServerHandler(bot));
    }

}
