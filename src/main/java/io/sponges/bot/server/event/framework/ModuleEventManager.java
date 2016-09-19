package io.sponges.bot.server.event.framework;

import io.sponges.bot.api.entities.manager.NetworkModuleManager;
import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.api.event.framework.NetworkEvent;
import io.sponges.bot.api.module.Module;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Set;
import java.util.function.Consumer;

public class ModuleEventManager implements EventManager {

    private final Set<Consumer> consumers = new ConcurrentHashSet<>();

    private final EventBus eventBus;
    private final Module module;

    public ModuleEventManager(EventBus eventBus, Module module) {
        this.eventBus = eventBus;
        this.module = module;
    }

    @Override
    public <T extends Event> Consumer<T> register(Class<T> event, Consumer<T> consumer) {
        Consumer<T> c = e -> {
            if (!isModuleEnabled(e)) return;
            consumer.accept(e);
        };
        consumers.add(c);
        eventBus.register(event, c);
        return c;
    }

    @Override
    public <T extends Event> boolean unregister(Consumer<T> consumer) {
        consumers.remove(consumer);
        return eventBus.unregister(consumer);
    }

    @Override
    public void unregisterAll() {
        consumers.forEach(eventBus::unregister);
        consumers.clear();
    }

    private <T extends Event> boolean isModuleEnabled(T event) {
        if (!(event instanceof NetworkEvent)) {
            return false;
        }
        NetworkModuleManager moduleManager = ((NetworkEvent) event).getNetwork().getModuleManager();
        return moduleManager.isEnabled(module);
    }

    @Override
    public <T extends Event> T post(T event) {
        if (!isModuleEnabled(event)) return null;
        return eventBus.post(event);
    }

    @Override
    public <T extends Event> void postAsync(T event) {
        if (!isModuleEnabled(event)) return;
        eventBus.postAsync(event);
    }

    @Override
    public <T extends Event> void postAsync(T event, Consumer<Boolean> callback) {
        if (!isModuleEnabled(event)) return;
        eventBus.postAsync(event, callback);
    }
}
