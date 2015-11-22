package pw.sponges.botserver.cmd.framework;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.messages.CmdResponseMessage;
import pw.sponges.botserver.messages.ResponseOption;
import pw.sponges.botserver.permissions.Group;

public class CommandRequest {

    private final Client client;
    private final String user, room, input;
    private final Group group;

    public CommandRequest(Client client, String user, String room, String input, Group group) {
        this.client = client;
        this.user = user;
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
        client.sendMessage(new CmdResponseMessage(client, room, user, message, ResponseOption.TEXT));
    }

    public void reply(String message, ResponseOption type) {
        client.sendMessage(new CmdResponseMessage(client, room, user, message, type));
    }
}
