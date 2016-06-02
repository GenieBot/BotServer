package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import org.json.JSONObject;

/**
 * This message sends a request to the client to get more information about a resource or load in a resource
 */
public final class ResourceRequestMessage extends Message {

    private static final String TYPE = "RESOURCE_REQUEST";

    private final ResourceType type;
    private final String networkId;
    private final String channelId;
    private final String userId;

    public ResourceRequestMessage(Client client, String networkId) {
        super(client, TYPE);
        this.type = ResourceType.NETWORK;
        this.networkId = networkId;
        this.channelId = null;
        this.userId = null;
    }

    public ResourceRequestMessage(Client client, String networkId, ResourceType type, String id) {
        super(client, TYPE);
        this.type = type;
        this.networkId = networkId;
        if (type == ResourceType.CHANNEL) {
            this.channelId = id;
            this.userId = null;
        } else if (type == ResourceType.USER) {
            this.userId = id;
            this.channelId = null;
        } else {
            this.channelId = null;
            this.userId = null;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("resource", type.name());
        switch (type) {
            case NETWORK: {
                object.put("network", networkId);
                break;
            }
            case CHANNEL: {
                object.put("network", networkId);
                object.put("channel", channelId);
                break;
            }
            case USER: {
                object.put("network", networkId);
                object.put("user", userId);
                break;
            }
        }
        return object;
    }

    public enum ResourceType {

        NETWORK, CHANNEL, USER

    }
}
