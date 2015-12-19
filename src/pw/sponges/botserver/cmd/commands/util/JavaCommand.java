package pw.sponges.botserver.cmd.commands.util;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pw.sponges.botserver.Bot;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class JavaCommand extends Command {

    private final Bot bot;
    private final Interpreter interpreter;
    private final ByteArrayOutputStream baos;

    public JavaCommand(Bot bot) {
        super("command.java", UserRole.OP, "Execute java code", "java", "exec", "runtime");

        this.bot = bot;
        this.interpreter = new Interpreter();
        this.baos = new ByteArrayOutputStream();

        try {
            interpreter.set("bot", bot);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: java [code]");
            return;
        }

        try {
            String result = interpreter.eval("import *;" + StringUtils.join(args, ' ')) + "";
            String content = baos.toString("UTF-8");

            request.reply("Execution complete " + (content.isEmpty() ? "(Result)" : "(Content)") + ":\n"
                    + (content.isEmpty() ? result : content));
        } catch (EvalError e) {
            e.printStackTrace();

            String error = ToStringBuilder.reflectionToString(e.getStackTrace());
            error = error.substring(1, error.length() - 1).replace(",", "\n");

            request.reply(e.getMessage() + "\n" + error);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            request.reply(e.getMessage());
        }
    }
}
