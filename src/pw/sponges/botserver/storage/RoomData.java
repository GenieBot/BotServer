package pw.sponges.botserver.storage;

import org.json.JSONObject;

public interface RoomData {

    RoomSettings getSettings();

    JSONObject toJson();

}
