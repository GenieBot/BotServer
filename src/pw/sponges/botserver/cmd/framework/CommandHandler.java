package pw.sponges.botserver.cmd.framework;

import pw.sponges.botserver.Bot;
import pw.sponges.botserver.cmd.commands.op.ClientsCommand;
import pw.sponges.botserver.cmd.commands.op.JoinRoomCommand;
import pw.sponges.botserver.cmd.commands.op.StopCommand;
import pw.sponges.botserver.cmd.commands.fun.UrbanCommand;
import pw.sponges.botserver.cmd.commands.info.*;
import pw.sponges.botserver.cmd.commands.mc.MCNetworks;
import pw.sponges.botserver.cmd.commands.mc.ServerCommand;
import pw.sponges.botserver.cmd.commands.admin.PrefixCommand;
import pw.sponges.botserver.cmd.commands.admin.SettingsCommand;
import pw.sponges.botserver.cmd.commands.util.LinkCommand;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.Setting;
import pw.sponges.botserver.util.Msg;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    // TODO revamp the way commands send responses instead of return

    private Bot bot;
    private List<Command> commands;
    private static Database database;

    public CommandHandler(Bot bot) {
        this.bot = bot;
        this.commands = new ArrayList<>();
        database = bot.getDatabase();

        this.registerCommands(
                new TestCommand(),
                new ChatInfoCommand(),
                new PrefixCommand(database),
                new LinkCommand(bot),
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
                new StatsCommand()
        );
    }

    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            this.commands.add(command);
        }
    }

    public static boolean isCommandRequest(String room, String input) {
        String prefix = (String) database.getData(room).getSettings().get(Setting.PREFIX);

        return input.toLowerCase().startsWith(prefix.toLowerCase());
    }

    public void handle(CommandRequest request) {
        String input = request.getInput();
        String room = request.getRoom();
        String prefix = (String) database.getData(room).getSettings().get(Setting.PREFIX);
        String noPrefix = input.substring(prefix.length());

        String[] args = noPrefix.split(" ");
        String cmd = args[0];

        for (Command command : commands) {
            for (String name : command.getNames()) {
                if (name.equalsIgnoreCase(cmd)) {
                    String node = command.getPermission();

                    if (!request.getGroup().getPermissionNodes().contains(node) && !request.getUser().equals("87164639695110144")) {
                        request.reply("You do not have permission to do that! Node: " + node);
                        return;
                    }

                    List<String> newArgs = new ArrayList<>();
                    String firstArg = args[0];

                    for (String arg : args) {
                        if (!arg.equalsIgnoreCase(firstArg)) {
                            newArgs.add(arg);
                        }
                    }

                    command.onCommand(request, newArgs.toArray(new String[newArgs.size()]));

                    /*request.getClient().sendMessage(new CmdResponseMessage(request.getClient(),
                            request.getRoom(),
                            request.getUser(),
                            message).getJSON().toString());*/

                    return;
                }
            }
        }

        Msg.log(request.getUser() + " RAN THE UNKNOWN COMMAND '" + noPrefix + "'!");
    }

    public List<Command> getCommands() {
        return commands;
    }
}
