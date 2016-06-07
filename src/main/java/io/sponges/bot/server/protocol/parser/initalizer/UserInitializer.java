package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.UserImpl;

public final class UserInitializer {

    public static User createUser(Storage storage, Network network, String id, boolean isAdmin, boolean isOp) {
        return new UserImpl(id, network, isAdmin, isOp, storage);
    }

}
