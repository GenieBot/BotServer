package pw.sponges.botserver.internal.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import pw.sponges.botserver.Bot;
import pw.sponges.botserver.internal.Server;
import pw.sponges.botserver.util.Msg;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * The socket server class.
 */
public class ServerImpl implements Server {

    public static final int PORT = 9090;

    // The instance of Bot passed to ServerInitializer
    private final Bot bot;

    /*
    Server related variables.
    Assigned in #start method instead of constructor.
     */
    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;
    private ServerBootstrap bootstrap = null;
    private ChannelFuture future = null;

    public ServerImpl(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void start() throws CertificateException, SSLException, InterruptedException {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SslContext context = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());

        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(bot, context));

            future = bootstrap.bind(PORT);
            future.sync().channel().closeFuture().sync();
        } finally {
            System.out.println("Shutting down!");
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void stop() throws InterruptedException {
        Msg.debug("Stop called");
        try {
            Msg.debug("Syncing future");
            future.sync().channel().closeFuture().getNow();
            Msg.debug("Synced future");
        } finally {
            Msg.debug("Shutting down gracefully");
            boss.shutdownGracefully();
            Msg.debug("Shutting down worker");
            worker.shutdownGracefully();
            Msg.debug("done!");
            System.exit(500);
            Msg.debug("exiting!");
        }
    }
}
