package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.Setting;

import java.util.List;

public class HelpCommand extends Command {

    private Database database;
    private CommandHandler commandHandler;

    private String help, user, mod, admin;
    private boolean loaded = false;

    public HelpCommand(Database database, CommandHandler commandHandler) {
        super("command.help", "help", "h", "commands");
        this.database = database;
        this.commandHandler = commandHandler;

        this.help = "SpongyBot commands:\n{prefix}help user\n{prefix}help admin";
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        /*if (!loaded) {
            StringBuilder user = new StringBuilder("User commands:");
            StringBuilder mod = new StringBuilder("Mod commands:");
            StringBuilder admin = new StringBuilder("Admin help:");

            for (Command cmd : commandHandler.getCommands()) {
                switch (cmd.getPermission()) {
                    case USER: {
                        user.append("\n{prefix}").append(cmd.getNames()[0]);
                        break;
                    }

                    case MOD: {
                        mod.append("\n{prefix}").append(cmd.getNames()[0]);
                        break;
                    }

                    case ADMIN: {
                        admin.append("\n{prefix}").append(cmd.getNames()[0]);
                        break;
                    }
                }
            }

            this.user = user.toString();
            this.mod = mod.toString();
            this.admin = admin.toString();
            loaded = true;
        }

        String room = request.getRoom();

        if (args.length == 0) {
            request.reply(withPrefixes(help, room));
        } else {
            String reply = null;

            switch (args[0].toLowerCase()) {
                case "unload": loaded = false; reply = "Unloaded the help message."; break;
                case "user": reply = user; break;
                case "mod": reply = mod; break;
                case "admin": reply = admin; break;
                default: reply = "Invalid arguments."; break;
            }

            request.reply(withPrefixes(reply, room));
        }*/

        StringBuilder str = new StringBuilder("Commands:\n");

        List<Command> commands = commandHandler.getCommands();
        for (int i = 0; i < commands.size(); i++) {
            Command cmd = commands.get(i);
            str.append(cmd.getNames()[0]);

            if (i != commands.size() - 1) {
                str.append(", ");
            }
        }

        request.reply(str.toString());
    }

    private String withPrefixes(String input, String room) {
        return input.replace("{prefix}", (String) database.getData(room).getSettings().get(Setting.PREFIX));
    }

}
