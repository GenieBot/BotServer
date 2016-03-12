package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.module.Module;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandHandler {

    private final Map<String, Command> commands = new ConcurrentHashMap<>();
    private final Map<Module, List<Command>> moduleCommands = new ConcurrentHashMap<>();

    protected void registerCommand(Module module, Command command) {
        String[] names = command.getNames();
        for (String name : names) {
            commands.put(name, command);
        }

        if (module != null) {
            List<Command> commands = new ArrayList<>();
            if (moduleCommands.containsKey(module)) {
                commands = moduleCommands.get(module);
            }
            commands.add(command);
            moduleCommands.put(module, commands);
        }
    }

    protected void unregisterCommand(Command command) {
        String[] names = command.getNames();
        for (String name : names) {
            commands.remove(name);
        }
    }

    protected void unregisterCommands(Module module) {
        if (!moduleCommands.containsKey(module)) return;
        moduleCommands.remove(module);
    }

    protected Collection<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        this.commands.values().stream().filter(command -> !commands.contains(command)).forEach(commands::add);
        return Collections.unmodifiableList(commands);
    }

    public void onUserChat(UserChatEvent userChatEvent) {
        Client client = userChatEvent.getClient();
        Network network = userChatEvent.getNetwork();
        Channel channel = userChatEvent.getChannel();
        User user = userChatEvent.getUser();
        Message message = userChatEvent.getMessage();
        CommandRequest request = new CommandRequestImpl(client, network, channel, user, message);
        handle(request);
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
