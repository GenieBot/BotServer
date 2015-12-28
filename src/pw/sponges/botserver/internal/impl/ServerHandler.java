package pw.sponges.botserver.internal.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import pw.sponges.botserver.Bot;
import pw.sponges.botserver.event.events.ClientInputEvent;
import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.util.Msg;

import java.util.Arrays;

/**
 * This class handles the events for each connection.
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> implements ServerWrapper {

    // This is a global collection of channels (static makes it global)
    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // The instance of Bot used for the event handler
    private final Bot bot;

    // The channel instance that is assigned to this ServerHandler.
    private Channel channel = null;

    public ServerHandler(Bot bot) {
        this.bot = bot;
        Msg.debug("new handler " + Arrays.toString(channels.toArray()));
    }

    /**
     * Called when the session has been secured.
     */
    @Override
    public void channelActive(final ChannelHandlerContext context) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        context.pipeline().get(SslHandler.class).handshakeFuture().addListener(future -> {
            write(context, "Welcome to BotServer!" +
                    "\nTime: " + System.currentTimeMillis() +
                    "\nCipher: " + context.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite());
            channel = context.channel();
            channels.add(channel);
        });
    }

    /**
     * Called when a new message from a client is received.
     */
    @Override
    public void messageReceived(ChannelHandlerContext context, String message) {
        Msg.debug("ServerHandler> Message: " + message);
        Msg.debug(Arrays.toString(channels.toArray()));

        switch (message.toLowerCase()) {
            case "hi": {
                Msg.debug("SeverHandler> Command response> \"Sending hello\"!");
                write(context, "hello");
                break;
            }

            case "bye": {
                Msg.debug("SeverHandler> Command response> Exiting!");
                context.close();
                break;
            }

            case "stop": {
                Msg.debug("ServerHandler> Command response> Stopping!");
                context.channel().close();
                context.channel().parent().close();
                break;
            }
        }

        bot.getEventManager().handle(new ClientInputEvent(message, this));
    }

    /**
     * Called whenever there is an exception with the networking.
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof TooLongFrameException) {
            Msg.warning("Message too long!");
            return;
        }

        if (cause.getMessage().equals("An existing connection was forcibly closed by the remote host")) {
            Msg.debug("Client disconnected!");
            ctx.close();
            return;
        }

        cause.printStackTrace();
        //ctx.close();
    }

    /**
     * Method to simplify writing and flushing to a ChannelHandlerContext.
     * @param context the context to write to
     * @param message the message to write
     */
    private void write(final ChannelHandlerContext context, final String message) {
        context.writeAndFlush(message + "\n");
    }

    /**
     * Method to simplify writing to a channel.
     * @param channel the channel to write to
     * @param message the message to write
     */
    private void write(final Channel channel, final String message) {
        channel.writeAndFlush(message + "\n");
    }

    /**
     * A method to make writing to the context simple.
     * @param message the message to write
     */
    @Override
    public void sendMessage(String message) {
        write(channel, message);
    }

    /**
     * Disconnects the client.
     */
    @Override
    public void disconnect() {
        channel.close();
    }
}
