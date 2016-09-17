package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.insert.InsertUserStatement;
import io.sponges.bot.server.database.statement.select.SelectUserIdStatement;
import io.sponges.bot.server.entities.UserImpl;

import java.util.UUID;

public final class UserInitializer extends Initializer {

    public static User createUser(Database database, Network network, String sourceId, boolean isAdmin, boolean isOp)
            throws Exception {
        UUID id = new SelectUserIdStatement(database, network.getId(), sourceId).executeAsync().get();
        if (id == null) {
            id = generateNewUUID();
            new InsertUserStatement(database, id, sourceId, network.getId()).executeAsync();
        }
        return new UserImpl(id, sourceId, network, isAdmin, isOp);
    }

}
