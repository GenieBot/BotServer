package pw.sponges.botserver.storage;

import org.json.JSONObject;

public interface RoomData {

    RoomSettings getSettings();

    RoomGroups getGroups();

    JSONObject toJson();

}
