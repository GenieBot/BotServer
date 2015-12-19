package pw.sponges.botserver.cmd.commands.info;

import org.json.JSONObject;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.Message;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.JSONBuilder;

public class TestCommand extends Command {

    public TestCommand() {
        super("command.test", UserRole.USER, "like ping pong but without a ball", "test", "t");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("Testing, 1 2 3!");

        request.reply(new Message(request.getClient(), "ok") {
            @Override
            public JSONObject toJson() {
                return JSONBuilder.create(this)
                        .withValue("testing", 123)
                        .withNewObject("info")
                        .withValue("ok", "lol ok")
                        .withValue("no", "but the coco???")
                        .build()
                        .withValue("do you like waffles", "yes")
                        .build();
            }
        }.toString());
    }
}
