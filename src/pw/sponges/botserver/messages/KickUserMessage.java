package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.framework.User;
import pw.sponges.botserver.util.JSONBuilder;

public class KickUserMessage extends Message {

    private final User user;

    public KickUserMessage(User user) {
        super(user.getClient(), "KICK");

        this.user = user;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("network", user.getNetwork().getId())
                .withValue("room", user.getRoom().getId())
                .withValue("user", user.getId())
                .build();
    }
}
