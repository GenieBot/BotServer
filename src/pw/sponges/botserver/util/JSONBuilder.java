package pw.sponges.botserver.util;

import org.json.JSONObject;
import pw.sponges.botserver.messages.Message;

import java.util.HashMap;
import java.util.Map;

public class JSONBuilder {

    public static Builder create(Message message) {
        return new Builder(message);
    }

    public static class Builder {

        private final String clientId;
        private final String type;

        private Map<String, Object> values = new HashMap<>();

        public Builder(Message message) {
            this.clientId = message.getClient().getId();
            this.type = message.getType();
        }
        
        public Builder withValues(Map<String, Object> values) {
            this.values = values;
            return this;
        }

        public Builder withValue(String key, Object value) {
            this.values.put(key, value);
            return this;
        }

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