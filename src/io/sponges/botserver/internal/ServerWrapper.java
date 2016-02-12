package io.sponges.botserver.internal;

public interface ServerWrapper {

    /**
     * Send a message to the client.
     * @param message the message to send
     */
    void sendMessage(String message);

    /**
     * Stops the connection.
     */
    void disconnect();
}
