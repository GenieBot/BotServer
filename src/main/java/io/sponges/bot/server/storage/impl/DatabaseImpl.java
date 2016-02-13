package io.sponges.bot.server.storage.impl;

import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Storage;

import java.util.HashMap;
import java.util.Map;

public class DatabaseImpl implements Database {

    private final Map<String, RoomData> data = new HashMap<>();

    private final Storage storage;

    public DatabaseImpl() {
        this.storage = new RedisStorage();
    }

    @Override
    public RoomData load(Room room) {
        RoomData data = storage.load(room);
        this.data.put(room.getId(), data);
        return data;
    }

    @Override
    public void save(Room room) {
        storage.save(room.getRoomData());
    }

    @Override
    public boolean isLoaded(Room room) {
        return data.containsKey(room.getId());
    }
}
