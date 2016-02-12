package io.sponges.botserver.internal;

import io.sponges.botserver.Bot;
import io.sponges.botserver.messages.Message;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class ServerImpl implements Server {

    private static final String[] channels = { "server" };

    private final Bot bot;
    private final String host;
    private int port = -1;

    private final ServerSubscriber serverSubscriber;

    private Jedis jedisSubscriber;
    private Jedis jedisPublisher;

    public ServerImpl(Bot bot, String host) {
        this.bot = bot;
        this.host = host;

        this.serverSubscriber = new ServerSubscriber(this);
        setupJedis(host, Optional.<Integer>empty());
    }

    public ServerImpl(Bot bot, String host, int port) {
        this.bot = bot;
        this.host = host;
        this.port = port;

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

    @Override
    public void start() {
        new Thread(() -> {
            jedisSubscriber.subscribe(serverSubscriber, channels);
        }).start();
    }

    @Override
    public void stop() {
        jedisSubscriber.close();
        jedisPublisher.close();
    }

    @Override
    public void publish(Message message) {
        publish(message.getClient().getChannel(), message.toString());
    }

    @Override
    public void publish(String channel, String message) {
        new Thread(() -> {
            jedisPublisher.publish(channel, message);
        }).start();
    }

    public Bot getBot() {
        return bot;
    }
}
