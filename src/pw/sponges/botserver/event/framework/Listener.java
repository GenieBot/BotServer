package pw.sponges.botserver.event.framework;

import pw.sponges.botserver.event.events.*;

public interface Listener {

    void onClientInput(ClientInputEvent event);

    void onConnect(ConnectEvent event);

    void onChatMessage(ChatMessageEvent event);

    void onCommandRequest(CommandRequestEvent event);

    void onUserJoin(UserJoinEvent event);

}