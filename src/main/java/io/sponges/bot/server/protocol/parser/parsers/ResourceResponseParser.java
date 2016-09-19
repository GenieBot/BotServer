package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Entity;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.entities.data.NetworkDataImpl;
import io.sponges.bot.server.entities.data.UserDataImpl;
import io.sponges.bot.server.entities.manager.NetworkManagerImpl;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import io.sponges.bot.server.protocol.parser.initalizer.ChannelInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.NetworkInitializer;
import io.sponges.bot.server.protocol.parser.initalizer.UserInitializer;
import org.json.JSONObject;

import java.util.Map;
import java.util.function.Consumer;

public final class ResourceResponseParser extends MessageParser {

    private final Bot bot;

    public ResourceResponseParser(Bot bot) {
        super("RESOURCE_RESPONSE");
        this.bot = bot;
    }

    @Override
    public void parse(Client client, long time, JSONObject content) {
        ResourceRequestMessage.ResourceType type = ResourceRequestMessage.ResourceType
                .valueOf(content.getString("resource").toUpperCase());
        String id = content.getString("id");
        Map<String, Consumer<Entity>> requests = ResourceRequestMessage.REQUEST_MANAGER.getRequests();
        if (!requests.containsKey(id)) {
            Bot.LOGGER.log(Logger.Type.WARNING, "Invalid response id \"" + id + "\".");
            return;
        }
        Consumer<Entity> consumer = requests.get(id);
        requests.remove(id);
        if (!content.getBoolean("found")) {
            consumer.accept(null);
            return;
        }
        String networkId = content.getString("network");
        JSONObject parameters = content.getJSONObject("params");
        NetworkManagerImpl networkManager = (NetworkManagerImpl) client.getNetworkManager();
        switch (type) {
            case NETWORK: {
                Network network;
                try {
                    network = NetworkInitializer.createNetwork(bot, client, networkId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                NetworkDataImpl data = (NetworkDataImpl) network.getNetworkData();
                if (!parameters.isNull("name")) data.setName(parameters.getString("name"));
                if (!parameters.isNull("description")) data.setDescription(parameters.getString("description"));
                if (!parameters.isNull("image")) data.setImage(parameters.getString("image"));
                consumer.accept(network);
                break;
            }
            case CHANNEL: {
                networkManager.loadNetwork(networkId, network -> {
                    Channel channel;
                    try {
                        channel = ChannelInitializer.createChannel(bot.getDatabase(), network, parameters.getString("id"),
                                Boolean.parseBoolean(parameters.getString("private")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    ChannelDataImpl data = (ChannelDataImpl) channel.getChannelData();
                    if (!parameters.isNull("name")) data.setName(parameters.getString("name"));
                    if (!parameters.isNull("topic")) data.setName(parameters.getString("topic"));
                    consumer.accept(channel);
                });
                break;
            }
            case USER: {
                networkManager.loadNetwork(networkId, network -> {
                    User user;
                    try {
                        user = UserInitializer.createUser(bot.getDatabase(), network, parameters.getString("id"),
                                Boolean.parseBoolean(parameters.getString("admin")),
                                Boolean.parseBoolean(parameters.getString("op")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    UserDataImpl data = (UserDataImpl) user.getUserData();
                    if (!parameters.isNull("username")) data.setUsername(parameters.getString("username"));
                    if (!parameters.isNull("display-name")) data.setDisplayName(parameters.getString("display-name"));
                    if (!parameters.isNull("status")) data.setStatus(parameters.getString("status"));
                    if (!parameters.isNull("mood")) data.setMood(parameters.getString("mood"));
                    if (!parameters.isNull("profile-url")) data.setProfileUrl(parameters.getString("profile-url"));
                    if (!parameters.isNull("profile-image")) data.setProfileImage(parameters.getString("profile-image"));
                    if (!parameters.isNull("private-network")) {
                        String privateNetworkId = parameters.getString("private-network");
                        String privateChannelId = parameters.getString("private-channel");
                        networkManager.loadNetwork(privateNetworkId, privateNetwork -> {
                            privateNetwork.getChannelManager().loadChannel(privateChannelId, channel -> {
                                if (channel != null) {
                                    data.setPrivateChannel((PrivateChannel) channel);
                                }
                            });
                        });
                    }
                    consumer.accept(user);
                });
                break;
            }
            default: {
                Bot.LOGGER.log(Logger.Type.WARNING, "Invalid resource type \"" + type.name() + "\".");
            }
        }
    }
}
