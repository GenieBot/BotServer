package pw.sponges.botserver.storage;

import java.util.Map;

public interface RoomSettings {

    Map<Setting, Object> getSettings();

    void setValues(Map<Setting, Object> settings);

    Object get(Setting setting);

    void set(Setting setting, Object value);

    Map<Setting, Object> getDefaults();

}
