package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.message.ReceivedMessage;
import io.sponges.bot.api.event.events.cmd.CommandPreProcessEvent;
import io.sponges.bot.api.event.events.cmd.CommandProcessedEvent;
import io.sponges.bot.api.event.events.message.MessageReceivedEvent;
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

    public void onUserChat(MessageReceivedEvent event) {
        Client client = event.getClient();
        Network network = event.getNetwork();
        Channel channel = event.getChannel();
        User user = event.getUser();
        ReceivedMessage message = event.getMessage();
        CommandRequestImpl request = new CommandRequestImpl(event, client, network, channel, user, message);
        handle(request);
    }

    public void handle(CommandRequest request) {
        Scheduler.runAsyncTask(() -> handleRequest(request));
    }

    private String getPrefix(Client client, DataObject networkObject, DataObject channelObject) {
        if (channelObject.exists("prefix")) {
            return (String) channelObject.get("prefix");
        }
        if (networkObject.exists("prefix")) {
            return (String) networkObject.get("prefix");
        }
        return client.getDefaultPrefix();
    }

    private void handleRequest(CommandRequest request) {
        Client client = request.getClient();
        Network network = request.getNetwork();
        Channel channel = request.getChannel();
        String prefix = getPrefix(client, network.getData(), channel.getData());
        String content = request.getMessage().getContent();
        if (!content.startsWith(prefix) || content.length() <= 1) return;
        String[] args = content.split(" ");
        if (args[0].equals(prefix)) args = Arrays.copyOfRange(args, 1, args.length);
        else args[0] = args[0].substring(prefix.length());
        String cmd = args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);
        if (!commands.containsKey(cmd)) return;
        Command command = commands.get(cmd);
        CommandPreProcessEvent preProcessEvent = new CommandPreProcessEvent(request, args, command);
        eventManager.postAsync(preProcessEvent, cancelled -> {
            if (!cancelled) processCommand(preProcessEvent);
        });
    }

    // at this stage we know the event has not been cancelled
    private void processCommand(CommandPreProcessEvent event) {
        CommandRequest request = event.getCommandRequest();
        Command command = event.getCommand();
        String[] args = event.getArgs();
        try {
            command.onCommand(request, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventManager.post(new CommandProcessedEvent(command, request, args));
    }
}
