package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.UserImpl;
import io.sponges.bot.server.entities.data.UserDataImpl;
import org.json.JSONObject;

public final class UserInitializer {

    public static User createUser(Storage storage, Network network, JSONObject json) {
        String id = json.getString("id");
        boolean isAdmin = json.getBoolean("admin");
        boolean isOp = json.getBoolean("op");
        User user = new UserImpl(id, network, isAdmin, isOp, storage);
        UserDataImpl userData = (UserDataImpl) user.getUserData();
        if (!json.isNull("username")) userData.setUsername(json.getString("username"));
        if (!json.isNull("display-name")) userData.setDisplayName(json.getString("display-name"));
        return user;
    }

}
