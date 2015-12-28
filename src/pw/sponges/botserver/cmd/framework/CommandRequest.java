package pw.sponges.botserver.cmd.framework;

/*public class CommandRequest {

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
*/

import pw.sponges.botserver.Client;
import pw.sponges.botserver.framework.Network;
import pw.sponges.botserver.framework.Room;
import pw.sponges.botserver.framework.User;
import pw.sponges.botserver.messages.CmdResponseMessage;

public class CommandRequest {

    private final User user;
    private final Room room;
    private final Network network;
    private final Client client;
    private final String input;

    public CommandRequest(User user, String input) {
        this.user = user;
        this.room = user.getRoom();
        this.network = room.getNetwork();
        this.client = network.getClient();
        this.input = input;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public Network getNetwork() {
        return network;
    }

    public Client getClient() {
        return client;
    }

    public String getInput() {
        return input;
    }

    public void reply(String message) {
        client.sendMessage(new CmdResponseMessage(user, message));
    }
}