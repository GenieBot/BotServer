package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.server.entities.UserImpl;
import org.json.JSONObject;

public final class UserInitializer {

    public static User createUser(Network network, JSONObject json) {
        String id = json.getString("id");
        boolean isAdmin = json.getBoolean("admin");
        boolean isOp = json.getBoolean("op");
        UserImpl user = new UserImpl(id, network, isAdmin, isOp);
        if (!json.isNull("username")) user.setUsername(json.getString("username"));
        if (!json.isNull("display-name")) user.setUsername(json.getString("display-name"));
        return user;
    }

}
