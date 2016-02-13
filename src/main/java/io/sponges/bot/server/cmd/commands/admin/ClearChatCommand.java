package io.sponges.bot.server.cmd.commands.admin;

import io.sponges.bot.server.messages.SendRawMessage;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.cmd.framework.Command;

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
