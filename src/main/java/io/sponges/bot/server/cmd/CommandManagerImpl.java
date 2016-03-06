package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandManager;

public class CommandManagerImpl implements CommandManager {

    private final CommandHandler commandHandler;

    public CommandManagerImpl(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void registerCommand(Command command) {
        commandHandler.registerCommand(command);
    }

    @Override
    public void unregisterCommand(Command command) {
        commandHandler.unregisterCommand(command);
    }
}
