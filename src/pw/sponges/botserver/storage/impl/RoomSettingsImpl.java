package pw.sponges.botserver.storage.impl;

import pw.sponges.botserver.storage.RoomSettings;
import pw.sponges.botserver.storage.Setting;

import java.util.HashMap;
import java.util.Map;

public class RoomSettingsImpl implements RoomSettings {

    // Variable assigned with #setValues
    private Map<Setting, Object> settings = null;

    public RoomSettingsImpl() {
    }

    @Override
    public void setValues(Map<Setting, Object> settings) {
        this.settings = settings;
    }

    @Override
    public Map<Setting, Object> getSettings() {
        return settings;
    }

    @Override
    public Object get(Setting setting) {
        return settings.get(setting);
    }

    @Override
    public void set(Setting setting, Object value) {
        settings.put(setting, value);
    }

    @Override
    public Map<Setting, Object> getDefaults() {
        Map<Setting, Object> objects = new HashMap<>();

        for (Setting setting : Setting.values()) {
            objects.put(setting, setting.getObject());
        }

        return objects;
    }

}
