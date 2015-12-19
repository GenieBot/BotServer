package pw.sponges.botserver.cmd.commands.fun;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

import java.util.HashMap;
import java.util.Map;

public class ChatbotCommand extends Command {

    private ChatterBot chatbot;
    private Map<String, ChatterBotSession> sessions;

    public ChatbotCommand() {
        super("command.chatbot", UserRole.USER, "talk to a robot", "chatbot", "c", "chatterbot", "cleverbot", "talk", "person", "ai");

        this.sessions = new HashMap<>();

        try {
            this.chatbot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("say something then silly! usage: chat [message]");
            return;
        }

        String room = request.getRoom();

        ChatterBotSession session;
        if (sessions.containsKey(room)) {
            session = sessions.get(room);
        } else {
            session = chatbot.createSession();
            sessions.put(room, session);
        }

        StringBuilder question = new StringBuilder();

        for (String s : args) {
            question.append(s).append(" ");
        }

        String thought = null;

        try {
            thought = session.think(question.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (thought == null) {
            request.reply("Something went wrong :L Try again in a few moments.");
            return;
        }

        request.reply(thought);
    }
}
