package io.sponges.bot.server.event.framework;

import io.sponges.bot.api.event.framework.CancelListener;
import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventManagerImpl implements EventManager {

    // TODO add event priority

    private final Map<Module, List<Consumer>> consumers = new ConcurrentHashMap<>();
    private final List<CancelListener> cancelListeners = new CopyOnWriteArrayList<>();

    private final EventBus eventBus;

    public EventManagerImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public <T extends Event> boolean register(Module module, Class<T> aClass, Consumer<T> consumer) {
        List<Consumer> consumers = new ArrayList<>();
        if (this.consumers.containsKey(module)) {
            consumers = this.consumers.get(module);
        }
        consumers.add(consumer);
        this.consumers.put(module, consumers);
        return eventBus.register(aClass, consumer);
    }

    @Override
    public <T extends Event> boolean unregister(Consumer<T> consumer) {
        return eventBus.unregister(consumer);
    }

    @Override
    public void unregister(Module module) {
        if (!consumers.containsKey(module)) return;
        List<Consumer> consumers = this.consumers.get(module);
        consumers.forEach(this::unregister);
        this.consumers.remove(module);
    }

    @Override
    public <T extends Event> T post(T t) {
        for (CancelListener listener : cancelListeners) {
            if (listener.isCancelled(t)) {
                return null;
            }
        }
        return eventBus.post(t);
    }

    @Override
    public void registerCancelListener(CancelListener cancelListener) {
        cancelListeners.add(cancelListener);
    }

    @Override
    public void unregisterCancelListener(CancelListener cancelListener) {
        cancelListeners.remove(cancelListener);
    }
}
