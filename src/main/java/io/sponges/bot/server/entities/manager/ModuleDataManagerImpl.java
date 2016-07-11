package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.manager.ModuleDataManager;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.ModuleDataObject;
import io.sponges.bot.api.storage.Storage;

public class ModuleDataManagerImpl implements ModuleDataManager {

    private static final String DATA_KEY = "module_data_store";

    private final Storage storage;
    private final DataObject networkObject;
    private final DataObject store;

    public ModuleDataManagerImpl(Storage storage, DataObject networkObject) {
        this.storage = storage;
        this.networkObject = networkObject;
        if (networkObject.exists(DATA_KEY)) {
            this.store = (DataObject) networkObject.get(DATA_KEY);
        } else {
            this.store = new DataObject();
            networkObject.set(DATA_KEY, this.store);
        }
    }

    @Override
    public ModuleDataObject getData(String s) {
        if (store.exists(s)) {
            return (ModuleDataObject) store.get(s);
        }
        ModuleDataObject object = new ModuleDataObject();
        object.setEnabled(true); // default enabling the module TODO find way to configure this per module
        store.set(s, object);
        networkObject.save(storage);
        return object;
    }
}
