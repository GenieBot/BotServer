package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.database.statement.insert.InsertNetworkStatement;
import io.sponges.bot.server.database.statement.select.SelectNetworkIdStatement;
import io.sponges.bot.server.entities.NetworkImpl;

import java.util.UUID;

public final class NetworkInitializer extends Initializer {

    public static Network createNetwork(Bot bot, Client client, String sourceId) throws Exception {
        UUID id = new SelectNetworkIdStatement(bot.getDatabase(), client.getId(), sourceId).executeAsync().get();
        if (id == null) {
            id = generateNewUUID();
            new InsertNetworkStatement(bot.getDatabase(), id, sourceId, client.getId()).executeAsync();
        }
        return new NetworkImpl(bot, id, sourceId, client);
    }

}
