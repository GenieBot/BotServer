package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.server.Bot;

import java.util.Collection;

public class CommandManagerImpl implements CommandManager {

    private final Bot bot;
    private final CommandHandler commandHandler;

    public CommandManagerImpl(Bot bot) {
        this.bot = bot;
        this.commandHandler = bot.getCommandHandler();

        registerCommand(null, new TestCommand());
        registerCommand(null, new ReloadCommand(bot));
        registerCommand(null, new StopCommand(bot));
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

    @Override
    public Collection<Command> getCommands() {
        return commandHandler.getCommands();
    }

    @Override
    public Collection<String> getNames() {
        return commandHandler.getNames();
    }

    @Override
    public Command getCommand(String s) {
        return commandHandler.getCommand(s);
    }
}
