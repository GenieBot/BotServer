package pw.sponges.botserver.util;

import org.json.JSONObject;
import pw.sponges.botserver.Client;

import java.util.HashMap;
import java.util.Map;

public class JSONBuilder {

    public static Builder create(Client client) {
        return new Builder(client);
    }

    public static class Builder {

        private String clientId;

        private String type = null;
        private Map<String, String> values = new HashMap<>();

        public Builder(Client client) {
            this.clientId = client.getId();
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }
        
        public Builder withValues(Map<String, String> values) {
            this.values = values;
            return this;
        }

        public Builder withValue(String key, String value) {
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