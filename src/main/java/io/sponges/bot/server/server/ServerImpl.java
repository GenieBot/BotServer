package io.sponges.bot.server.server;

import io.sponges.bot.api.server.Server;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.oldmessages.Message;
import io.sponges.bot.server.util.Msg;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.Optional;

public class ServerImpl implements Server {

    private final Bot bot;
    private final String host;
    private int port = -1;
    private final List<String> channels;

    private final ServerSubscriber serverSubscriber;

    private Jedis jedisSubscriber;
    private Jedis jedisPublisher;

    public ServerImpl(Bot bot, String host, int port, List<String> channels) {
        this.bot = bot;
        this.host = host;
        this.port = port;
        this.channels = channels;

        this.serverSubscriber = new ServerSubscriber(this);
        setupJedis(host, Optional.of(port));
    }

    private void setupJedis(String host, Optional<Integer> port) {
        if (port.isPresent()) {
            this.jedisSubscriber = new Jedis(host, port.get());
            this.jedisPublisher = new Jedis(host, port.get());
        } else {
            this.jedisSubscriber = new Jedis(host);
            this.jedisPublisher = new Jedis(host);
        }
    }

    public void start() {
        new Thread(() -> {
            try {
                jedisSubscriber.subscribe(serverSubscriber, channels.toArray(new String[channels.size()]));
            } catch (JedisConnectionException e) {
                if (e.getMessage().contains("Socket is closed")) {
                    Msg.warning("Unsubscribed from " + channels);
                    return;
                }

                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stop() {
        jedisSubscriber.close();
        jedisPublisher.close();
    }

    public void publish(Message message) {
        publish(message.getClient().getChannel(), message.toString());
    }

    public void publish(String channel, String message) {
        new Thread(() -> {
            jedisPublisher.publish(channel, message);
        }).start();
    }

    public List<String> getChannels() {
        return channels;
    }

    public Bot getBot() {
        return bot;
    }
}
