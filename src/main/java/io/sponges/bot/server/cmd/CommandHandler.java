package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.event.events.cmd.CommandPreProcessEvent;
import io.sponges.bot.api.event.events.cmd.CommandProcessedEvent;
import io.sponges.bot.api.event.events.user.UserChatEvent;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.Module;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.util.Scheduler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandHandler {

    private final Map<String, Command> commands = new ConcurrentHashMap<>();
    private final Map<Module, List<Command>> moduleCommands = new ConcurrentHashMap<>();

    private final EventManager eventManager;

    public CommandHandler(EventManager eventManager) {
        this.eventManager = eventManager;
    }

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

    protected boolean hasCommands(Module module) {
        return moduleCommands.containsKey(module);
    }

    protected Collection<Command> getCommands(Module module) {
        return Collections.unmodifiableCollection(moduleCommands.get(module));
    }

    protected Collection<String> getNames() {
        return Collections.unmodifiableCollection(commands.keySet());
    }

    protected Command getCommand(String name) {
        return commands.get(name);
    }

    public void onUserChat(UserChatEvent userChatEvent) {
        Client client = userChatEvent.getClient();
        Network network = userChatEvent.getNetwork();
        Channel channel = userChatEvent.getChannel();
        User user = userChatEvent.getUser();
        Message message = userChatEvent.getMessage();
        CommandRequestImpl request = new CommandRequestImpl(client, network, channel, user, message);
        handle(request);
    }

    public void handle(CommandRequest request) {
        Scheduler.runAsyncTask(() -> handleRequest(request));
    }

    private boolean handleRequest(CommandRequest request) {
        Client client = request.getClient();
        Network network = request.getNetwork();
        Channel channel = request.getChannel();
        DataObject networkObject = network.getData();
        DataObject channelObject = channel.getData();
        String prefix;
        if (channelObject.exists("prefix")) {
            prefix = (String) channelObject.get("prefix");
        } else {
            if (networkObject.exists("prefix")) {
                prefix = (String) networkObject.get("prefix");
            } else {
                prefix = client.getDefaultPrefix();
            }
        }
        String content = request.getMessage().getContent();
        if (!content.startsWith(prefix) || content.length() <= 1) return false;
        String[] args = content.split(" ");
        if (args[0].equals(prefix)) args = Arrays.copyOfRange(args, 1, args.length);
        else args[0] = args[0].substring(prefix.length());
        String cmd = args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);
        if (!commands.containsKey(cmd)) return false;
        Command command = commands.get(cmd);
        CommandPreProcessEvent preProcessEvent = new CommandPreProcessEvent(request, args, command);
        if (eventManager.post(preProcessEvent) == null) return false;
        if (command.isGlobalDisabled()) {
            request.reply("Sorry, that command is disabled for everyone!");
            return false;
        }
        if (command.isLimitedToNetwork() && !network.getId().equals(command.getNetworkOnly())) {
            return false;
        }
        try {
            command.onCommand(request, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventManager.post(new CommandProcessedEvent(command, request, args));
        return true;
    }

}
