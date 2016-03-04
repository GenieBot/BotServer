package io.sponges.bot.server.cmd.framework;

import io.sponges.bot.server.Bot;
import io.sponges.bot.server.cmd.commands.admin.*;
import io.sponges.bot.server.cmd.commands.fun.*;
import io.sponges.bot.server.cmd.commands.info.*;
import io.sponges.bot.server.cmd.commands.mc.*;
import io.sponges.bot.server.cmd.commands.op.*;
import io.sponges.bot.server.cmd.commands.steam.SteamStatusCommand;
import io.sponges.bot.server.cmd.commands.util.JSONBeautifier;
import io.sponges.bot.server.cmd.commands.util.JavaCommand;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.util.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHandler {

    private final List<Command> commands = new ArrayList<>();

    private final Bot bot;
    private final Database database;

    public CommandHandler(Bot bot) {
        this.bot = bot;
        this.database = bot.getDatabase();

        // TODO dynamic command registration
        this.registerCommands(
                new TestCommand(),
                new ChatInfoCommand(),
                new PrefixCommand(database),
                new AboutCommand(),
                new ClientsCommand(bot),
                new StopCommand(bot),
                new UrbanCommand(),
                new SettingsCommand(bot.getDatabase()),
                new ServerCommand(),
                new HelpCommand(database, this),
                new UserInfoCommand(),
                new MCNetworks(),
                new JoinRoomCommand(),
                new StatsCommand(),
                new JSONBeautifier(),
                new CmdListCommand(this),
                new JavaCommand(bot),
                new TumblrArgumentCommand(),
                new GoogleCommand(),
                new LmgtfyCommand(),
                new HasPaid(),
                new MinecraftStatusCommand(),
                new WebsiteCommand(),
                new ChatbotCommand(),
                new TimeCommand(),
                new MirrorCommand(),
                new SteamStatusCommand(),
                new ToggleCommand(database, this),
                new KickCommand(),
                new BanCommand(database),
                new SendMessageCommand(),
                new ClearChatCommand(),
                new SpamCommand(),
                new AdvertisementCommand()
        );
    }

    public void registerCommands(Command... commands) {
        Collections.addAll(this.commands, commands);
    }

    public static boolean isCommandRequest(Room room, String input) {
        String prefix;
        if (room.getPrefix() != null) {
            prefix = room.getPrefix();
        } else {
            prefix = (String) room.getRoomData().get(Setting.PREFIX);
            room.setPrefix(prefix);
        }
        return input.toLowerCase().startsWith(prefix.toLowerCase());
    }

    public Command getCommand(String command) {
        for (Command cmd : commands) {
            for (String name : cmd.getNames()) {
                if (name.equalsIgnoreCase(command)) return cmd;
            }
        }

        return null;
    }

    public void handle(CommandRequest request) {
        Scheduler.runAsyncTask(() -> handleCommand(request));
    }

    public void handleCommand(CommandRequest request) {
        String input = request.getInput();
        Room room = request.getRoom();

        RoomData roomData = request.getRoom().getRoomData();
        String prefix = (String) roomData.get(Setting.PREFIX);
        String noPrefix = input.substring(prefix.length());

        String[] args = noPrefix.split(" ");
        String cmd = args[0];

        for (Command command : commands) {
            for (String name : command.getNames()) {
                if (name.equalsIgnoreCase(cmd)) {
                    @SuppressWarnings("unchecked")
                    List<String> disabledCommands = (List<String>) roomData.get(Setting.DISABLED_COMMANDS);
                    if (disabledCommands.contains(command.getNames()[0].toLowerCase())) {
                        request.reply("That command is disabled! Enable it with the 'toggle' command.");
                        return;
                    }

                    UserRole role = command.getRole();
                    UserRole userRole = request.getUser().getRole();

                    boolean noPerm = false;
                    if (role == UserRole.ADMIN) {
                        if (userRole != UserRole.ADMIN && userRole != UserRole.OP) {
                            noPerm = true;
                        }
                    } else if (role == UserRole.OP) {
                        if (userRole != UserRole.OP) {
                            noPerm = true;
                        }
                    }
                    if (noPerm) {
                        noPermission(request, role, userRole);
                        return;
                    }

                    boolean adminOnly = (boolean) roomData.get(Setting.ADMIN_ONLY);

                    if (adminOnly && (userRole != UserRole.ADMIN && userRole != UserRole.OP)) {
                        request.reply("Room is in admin only mode! To disable this, use\nset admin-only false");
                        return;
                    }

                    String[] newArgs;
                    if (args.length > 1) {
                        newArgs = new String[args.length - 1];

                        for (int i = 1; i < args.length; i++) {
                            newArgs[i - 1] = args[i];
                        }
                    } else {
                        newArgs = new String[0];
                    }
                    command.onCommand(request, newArgs);
                    return;
                }
            }
        }
    }

    private void noPermission(CommandRequest request, UserRole role, UserRole userRole) {
        request.reply("You do not have permission to do that! (" + role.name().toUpperCase() + ")"
                + "\nYour role: " + userRole.name().toLowerCase()
                + "\nYour id: " + request.getUser().getId());
    }

    public List<Command> getCommands() {
        return commands;
    }
}
