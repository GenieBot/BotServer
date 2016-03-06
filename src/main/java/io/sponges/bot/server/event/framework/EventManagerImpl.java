package io.sponges.bot.server.event.framework;

import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.event.framework.EventManager;

import java.util.function.Consumer;

public class EventManagerImpl implements EventManager {

    // TODO add event priority
    // TODO add event cancelling

    private final EventBus eventBus;

    public EventManagerImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public <T extends Event> boolean register(Class<T> aClass, Consumer<T> consumer) {
        return eventBus.register(aClass, consumer);
    }

    @Override
    public <T extends Event> boolean unregister(Consumer<T> consumer) {
        return eventBus.unregister(consumer);
    }

    @Override
    public <T extends Event> T post(T t) {
        return eventBus.post(t);
    }
}
