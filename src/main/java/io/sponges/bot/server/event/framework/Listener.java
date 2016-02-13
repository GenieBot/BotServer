package io.sponges.bot.server.event.framework;

import io.sponges.bot.server.event.events.*;

public interface Listener {

    void onClientInput(ClientInputEvent event);

    void onConnect(ConnectEvent event);

    void onChatMessage(ChatMessageEvent event);

    void onCommandRequest(CommandRequestEvent event);

    void onUserJoin(UserJoinEvent event);

}