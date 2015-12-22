package pw.sponges.botserver.cmd.framework;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.messages.CmdResponseMessage;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.util.Msg;

public class CommandRequest {

    private final Client client;
    private final String user, username, room, input;
    private final Group group;

    public CommandRequest(Client client, String user, String username, String room, String input, Group group) {
        this.client = client;
        this.user = user;
        this.username = username;
        this.room = room;
        this.input = input;
        this.group = group;
    }

    public Client getClient() {
        return client;
    }

    public String getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getRoom() {
        return room;
    }

    public String getInput() {
        return input;
    }

    public Group getGroup() {
        return group;
    }

    public void reply(String message) {
        Msg.debug("Replying with message...");

        client.sendMessage(new CmdResponseMessage(client, room, user, username, message));
    }
}
