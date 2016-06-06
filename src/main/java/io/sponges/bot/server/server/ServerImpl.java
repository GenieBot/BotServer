package io.sponges.bot.server.server;

import io.netty.channel.ChannelHandlerContext;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.server.framework.Server;
import io.sponges.bot.server.server.framework.ServerListener;
import io.sponges.bot.server.server.framework.exception.ServerAlreadyRunningException;
import io.sponges.bot.server.server.framework.exception.ServerNotRunningException;
import io.sponges.bot.server.util.ValidationUtils;
import org.json.JSONObject;

public class ServerImpl implements io.sponges.bot.api.server.Server {

    private final Server server;

    public ServerImpl(Bot bot, int port) {
        this.server = new io.sponges.bot.server.server.framework.impl.ServerImpl(port);
        this.server.registerListener(new ServerListener() {
            @Override
            public void onConnect(ChannelHandlerContext context) {
                System.out.println("Unidentified client " + context.channel().remoteAddress() + " connected");
            }

            @Override
            public void onDisconnect(ChannelHandlerContext context) {
                System.out.println("Client client " + context.channel().remoteAddress() + " disconnected");
            }

            @Override
            public void onMessage(ChannelHandlerContext context, String message) {
                if (!ValidationUtils.isValidJson(message)) {
                    System.out.println("Got invalid json: " + message + " from " + context.channel().remoteAddress());
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
                cause.printStackTrace();
            }
        });
    }

    public void start(Runnable runnable) {
        try {
            this.server.start(runnable);
        } catch (ServerAlreadyRunningException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            this.server.stop(() -> {});
        } catch (ServerNotRunningException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(Runnable runnable) {
        try {
            this.server.stop(runnable);
        } catch (ServerNotRunningException e) {
            e.printStackTrace();
        }
    }
}
