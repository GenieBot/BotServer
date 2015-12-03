package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.storage.Setting;
import pw.sponges.botserver.util.JSONBuilder;

public class SettingUpdateMessage extends Message {

    private final String room;
    private final Setting setting;
    private final Object value;

    public SettingUpdateMessage(Client client, String room, Setting setting, Object value) {
        super(client, "SETTING");
        this.room = room;
        this.setting = setting;
        this.value = value;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("room", room)
                .withValue("setting", setting.toString())
                .withValue("value", value)
                .build();
    }

}
