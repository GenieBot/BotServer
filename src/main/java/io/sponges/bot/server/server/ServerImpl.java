package io.sponges.bot.server.server;

import io.netty.channel.ChannelHandlerContext;
import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.api.server.Server;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.manager.ClientManagerImpl;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.server.internal.InternalServerImpl;
import io.sponges.bot.server.server.internal.InternalServerListener;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketAddress;

public class ServerImpl implements Server {

    private final InternalServerImpl server;

    public ServerImpl(Bot bot, int port) {
        this.server = new InternalServerImpl(port);
        this.server.registerListener(new InternalServerListener() {
            @Override
            public void onConnect(ChannelHandlerContext context) {
                Bot.LOGGER.log(Logger.Type.INFO, "Client connect: unknown (" + context.channel().remoteAddress() + ")");
            }

            @Override
            public void onDisconnect(ChannelHandlerContext context) {
                SocketAddress address = context.channel().remoteAddress();
                Client client = ((ClientManagerImpl) bot.getClientManager()).getClient(address);
                if (client == null) {
                    Bot.LOGGER.log(Logger.Type.WARNING, "Client disconnect: unknown (" + address + ")");
                } else {
                    bot.getClientManager().getClients().remove(client.getId());
                    Bot.LOGGER.log(Logger.Type.INFO, "Client disconnect: " + client.getId() + " (" + address + ")");
                }
            }

            @Override
            public void onMessage(ChannelHandlerContext context, String message) {
                if (message.charAt(0) != '{' || message.charAt(message.length() - 1) != '}') {
                    Bot.LOGGER.log(Logger.Type.WARNING, "Got invalid json: " + message + " from " + context.channel().remoteAddress());
                    return;
                }
                JSONObject json = new JSONObject(message);
                String clientId = json.getString("client").toLowerCase();
                ClientManager clientManager = bot.getClientManager();
                Client client;
                if (clientManager.isClient(clientId)) {
                    client = clientManager.getClient(clientId);
                } else {
                    String defaultPrefix = json.getJSONObject("content").getString("prefix");
                    client = new ClientImpl(clientId, defaultPrefix, context.channel(), bot.getStorage());
                    clientManager.getClients().put(clientId, client);
                }
                ClientInputEvent clientInputEvent = new ClientInputEvent(client, json);
                bot.getEventManager().post(clientInputEvent);
            }

            @Override
            public void onError(ChannelHandlerContext context, Throwable cause) {
                if (cause.getClass() == IOException.class && cause.getMessage()
                        .equals("An existing connection was forcibly closed by the remote host")) {
                    return;
                }
                cause.printStackTrace();
            }
        });
    }

    public void start(Runnable runnable) {
        try {
            this.server.start(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        this.server.stop(() -> {});
    }

    @Override
    public void stop(Runnable runnable) {
        this.server.stop(runnable);
    }
}
