package pw.sponges.botserver.util;

import org.json.JSONObject;
import pw.sponges.botserver.messages.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class to construct JSON messages for sending to clients
 */
public class JSONBuilder {

    /**
     * Create an instance of the Builder
     * @param message the Message in which JSON is required
     * @return Builder instance
     */
    public static Builder create(Message message) {
        return new Builder(message);
    }

    /**
     * Static Builder class with Builder pattern
     */
    public static class Builder {

        private final String clientId;
        private final String type;

        private Map<String, Object> values = new HashMap<>();

        public Builder(Message message) {
            this.clientId = message.getClient().getId();
            this.type = message.getType();
        }

        /**
         * Set the values Map
         * @param values the Map to set values to
         * @return Builder instance
         */
        public Builder withValues(Map<String, Object> values) {
            this.values = values;
            return this;
        }

        /**
         * Adds a value to the values Map
         * @param key the key to assign the value to
         * @param value the value to assign to the key
         * @return Builder instance
         */
        public Builder withValue(String key, Object value) {
            this.values.put(key, value);
            return this;
        }

        /**
         * Constructs the JSON from the set values & data from Message instance
         * @return JSONObject for message
         */
        public JSONObject build() {
            JSONObject object = new JSONObject();

            object.put("type", type);
            object.put("client-id", clientId);

            for (String key : values.keySet()) {
                object.put(key, values.get(key));
            }

            return object;
        }

    }

}