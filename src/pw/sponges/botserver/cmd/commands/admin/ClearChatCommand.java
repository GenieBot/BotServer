package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.SendRawMessage;
import pw.sponges.botserver.permissions.simple.UserRole;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("command.clearchat", UserRole.ADMIN, "clears the chat", "clearchat", "clear", "cleanchat");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.getClient().sendMessage(new SendRawMessage(request.getClient(), request.getRoom(), "\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
    }

}
