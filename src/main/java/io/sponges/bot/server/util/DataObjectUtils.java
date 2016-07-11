package io.sponges.bot.server.util;

import io.sponges.bot.api.storage.DataObject;
import org.json.JSONObject;

public final class DataObjectUtils {

    public static DataObject fromJson(JSONObject json) {
        return fromJson(null, json);
    }

    public static DataObject fromJson(String key, JSONObject json) {
        DataObject dataObject;
        if (key == null) {
            dataObject = new DataObject();
        } else {
            dataObject = new DataObject(key);
        }
        json.keySet().forEach(jsonKey -> dataObject.set(jsonKey, json.get(jsonKey)));
        return dataObject;
    }

}
