package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.server.Bot;

public class CommandManagerImpl implements CommandManager {

    private final Bot bot;
    private final CommandHandler commandHandler;

    public CommandManagerImpl(Bot bot) {
        this.bot = bot;
        this.commandHandler = bot.getCommandHandler();

        registerCommand(null, new TestCommand());
        registerCommand(null, new ReloadCommand(bot));
    }

    @Override
    public void registerCommand(Module module, Command command) {
        commandHandler.registerCommand(module, command);
    }

    @Override
    public void unregisterCommand(Command command) {
        commandHandler.unregisterCommand(command);
    }

    @Override
    public void unregisterCommands(Module module) {
        commandHandler.unregisterCommands(module);
    }
}
