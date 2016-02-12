package io.sponges.botserver.cmd.commands.fun;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;

import java.net.URLEncoder;

public class LmgtfyCommand extends Command {

    public LmgtfyCommand() {
        super("command.lmgtfy", UserRole.USER, "cheeky google searching meme", "lmgtfy", "letmegooglethatforyou", "sarcasticsearch", "gtfy");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("tell me wot 2 google lol... usage: lmgtfy [term]");
            return;
        }


        StringBuilder response = new StringBuilder();
        response.append("http://lmgtfy.com/?q=");

        for (int i = 0; i < args.length; i++) {
            response.append(URLEncoder.encode(args[i]));

            if (i != args.length - 1) {
                response.append("+");
            }
        }

        request.reply(response.toString());
    }
}
