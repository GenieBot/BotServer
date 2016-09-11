package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Entity;
import io.sponges.bot.server.protocol.manager.ResourceRequestManager;
import org.json.JSONObject;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * This message sends a request to the client to get more information about a resource or load in a resource
 */
public final class ResourceRequestMessage extends Message {

    private static final String TYPE = "RESOURCE_REQUEST";
    public static final ResourceRequestManager REQUEST_MANAGER = new ResourceRequestManager();

    private final ResourceType type;
    private final String networkId;
    private final String channelId;
    private final String userId;
    private final String requestId;

    public ResourceRequestMessage(Client client, String networkId, Consumer<Entity> callback) {
        super(client, TYPE);
        this.type = ResourceType.NETWORK;
        this.networkId = networkId;
        this.channelId = null;
        this.userId = null;
        this.requestId = UUID.randomUUID().toString();
        REQUEST_MANAGER.getRequests().put(this.requestId, callback);
    }

    public ResourceRequestMessage(Client client, String networkId, ResourceType type, String id, Consumer<Entity> callback) {
        super(client, TYPE);
        this.type = type;
        this.networkId = networkId;
        switch (type) {
            case CHANNEL:
                this.channelId = id;
                this.userId = null;
                break;
            case USER:
                this.channelId = null;
                this.userId = id;
                break;
            default:
                this.channelId = null;
                this.userId = null;
        }
        this.requestId = UUID.randomUUID().toString();
        REQUEST_MANAGER.getRequests().put(this.requestId, callback);
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("resource", type.name());
        object.put("id", requestId);
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
