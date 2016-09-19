package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandManager;
import io.sponges.bot.api.module.Module;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ModuleCommandManager implements CommandManager {

    private final Set<Command> commands = new HashSet<>();

    private final Module module;
    private final CommandHandler commandHandler;

    public ModuleCommandManager(Module module, CommandHandler commandHandler) {
        this.module = module;
        this.commandHandler = commandHandler;
    }

    @Override
    public void registerCommand(Command command) {
        command.setModule(module);
        commands.add(command);
        commandHandler.registerCommand(command);
    }

    @Override
    public void unregisterCommand(Command command) {
        commands.remove(command);
        commandHandler.unregisterCommand(command);
    }

    @Override
    public void unregisterAllCommands() {
        commands.forEach(commandHandler::unregisterCommand);
        commands.clear();
    }

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public Collection<Command> getAllCommands() {
        return commandHandler.getCommands();
    }

    @Override
    public Command getCommand(String name) {
        return commandHandler.getCommand(name);
    }
}
