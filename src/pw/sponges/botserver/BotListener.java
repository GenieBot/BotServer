package pw.sponges.botserver;

import org.json.JSONObject;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.event.events.*;
import pw.sponges.botserver.event.framework.EventManager;
import pw.sponges.botserver.event.framework.Listener;
import pw.sponges.botserver.messages.ChatMessage;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.util.Msg;

public class BotListener implements Listener {

    private Bot bot;
    private EventManager eventManager;
    private CommandHandler commandHandler;
    private PermissionsManager permissions;

    private static int chatMessages, serverMessages, commandRuns;

    public BotListener(Bot bot, PermissionsManager permissions) {
        this.bot = bot;
        this.permissions = permissions;
        this.eventManager = bot.getEventManager();
        this.commandHandler = bot.getCommandHandler();
    }

    public static int getChatMessages() {
        return chatMessages;
    }

    public static int getServerMessages() {
        return serverMessages;
    }

    public static int getCommandRuns() {
        return commandRuns;
    }

    @Override
    public void onClientInput(ClientInputEvent event) {
        serverMessages++;

        if (!event.getInput().contains("{")) {
            Msg.warning("Got non json message! " + event.getInput());
            return;
        }

        JSONObject object = new JSONObject(event.getInput());
        Msg.debug(object.toString());

        String type = object.getString("type").toUpperCase();

        switch (type) {
            case "CONNECT": {
                String clientId = object.getString("client-id");
                Client client = new ClientImpl(clientId, event.getWrapper());
                eventManager.handle(new ConnectEvent(client));
                break;
            }

            case "CHAT": {
                String clientId = object.getString("client-id");
                Client client = bot.getClient(clientId);
                String user = object.getString("user");
                String room = object.getString("room");
                String name = object.getString("name");
                String message = object.getString("message");
                eventManager.handle(new ChatMessageEvent(client, user, room, name, message));
                break;
            }

            case "LINK": {
                String clientId = object.getString("client-id");
                Client client = bot.getClient(clientId);
                String clientRoom = object.getString("room");
                String targetId = object.getString("target-client");
                String targetRoom = object.getString("target-room");
                eventManager.handle(new LinkRequestEvent(client, clientRoom, targetId, targetRoom));
                break;
            }

            default: {
                Msg.warning("Unknown message type! " + type);
                Msg.debug(object.toString());
                break;
            }
        }
    }

    @Override
    public void onConnect(ConnectEvent event) {
        String id = event.getClient().getId();
        if (bot.getClients().containsKey(id)) {
            Msg.warning("There is already a client connected with the ID " + id + "!");
            // TODO send already connected message?
            //event.getClient().getWrapper().disconnect();
            bot.getClients().remove(id);
        }

        Msg.log("Client identified as " + id + "!");
        bot.getClients().put(event.getClient().getId(), event.getClient());
    }

    @Override
    public void onChatMessage(ChatMessageEvent event) {
        chatMessages++;
        Client client = event.getClient();

        String clientId = client.getId();
        String room = event.getRoom();
        String user = event.getUser();
        String message = event.getMessage();

        Msg.log("[" + clientId + "] [" + event.getRoomName() + "] " + user + ": " + message);

        PermissionGroups groups = permissions.getGroups(room);
        /*if (!permissions.isLoaded(room)) {
            permissions.loadPermissions(room);
        }*/
        if (!groups.isSetup(user)) {
            groups.setup(user);
        }
        Group group = groups.getUserGroup(user);

        if (CommandHandler.isCommandRequest(room, message)) {
            eventManager.handle(new CommandRequestEvent(new CommandRequest(client, user, room, message, group)));
        }

        if (client.isBridged(room)) {
            Bridge bridge = client.getBridge(room);
            String targetClient = bridge.getTargetClient();
            Client tClient = bot.getClient(targetClient);
            String tRoom = bridge.getTargetRoom();

            tClient.sendMessage(new ChatMessage(tClient, user, room, event.getRoomName(), tRoom, message));
            Msg.debug("Sent bridge message " + message + " to client " + targetClient + " room " + tRoom + " from " + clientId + " room " + room);
        }
    }

    @Override
    public void onCommandRequest(CommandRequestEvent event) {
        commandRuns++;
        commandHandler.handle(event.getCommandRequest());
    }

    @Override
    public void onLinkRequest(LinkRequestEvent event) {
        String clientId = event.getClient().getId();
        String clientRoom = event.getClientRoom();
        String target = event.getTargetId();
        String targetRoom = event.getTargetRoom();

        bot.addLink(clientId, clientRoom, target, targetRoom);
    }
}
