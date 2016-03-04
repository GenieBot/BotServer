package io.sponges.bot.server.internal;

import io.sponges.bot.server.messages.Message;

import java.util.List;

public interface Server {

    void start();

    void stop();

    void publish(Message message);

    void publish(String channel, String message);

    List<String> getChannels();

}
