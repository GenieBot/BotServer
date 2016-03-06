package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandHandler {

    private final Map<String, Command> commands = new ConcurrentHashMap<>();

    protected void registerCommand(Command command) {
        String[] names = command.getNames();
        for (String name : names) {
            commands.put(name, command);
        }
    }

    protected void unregisterCommand(Command command) {
        String[] names = command.getNames();
        commands.remove(names);
    }

    public boolean handle(CommandRequest request) {
        String content = request.getMessage().getContent();
        if (!content.startsWith("-") || content.length() <= 1) return false; // TODO load prefix from settings
        String[] args = content.split(" ");
        String cmd = args[0].substring(1).toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);
        if (!commands.containsKey(cmd)) return false;
        commands.get(cmd).onCommand(request, args);
        return true;
    }

}
