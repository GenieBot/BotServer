package io.sponges.bot.server.protocol.parser.initalizer;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.server.entities.NetworkImpl;

public final class NetworkInitializer {

    public static Network createNetwork(Client client, String id) {
        return new NetworkImpl(id, client);
    }

}
