package io.sponges.botserver.internal;

import io.sponges.botserver.messages.Message;

public interface Server {

    void start();

    void stop();

    void publish(Message message);

    void publish(String channel, String message);

}
