package io.sponges.botserver.cmd.commands.admin;

import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.messages.SendRawMessage;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("command.clearchat", UserRole.ADMIN, "clears the chat", "clearchat", "clear", "cleanchat");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.getClient().sendMessage(
                new SendRawMessage(request.getClient(), request.getNetwork().getId(), request.getRoom().getId(),
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
    }

}
