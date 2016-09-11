package io.sponges.bot.server.event.framework;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.sponges.bot.api.event.framework.Event;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public final class EventBus {

    private final Multimap<Class<? extends Event>, Consumer<Event>> consumerMap = ArrayListMultimap.create();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public EventBus() {
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> boolean register(Class<T> clazz, Consumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer cannot be null");
        Preconditions.checkNotNull(clazz, "clazz cannot be null");
        writeLock.lock();
        try {
            Consumer<Event> handler = (Consumer<Event>) consumer;
            return consumerMap.put(clazz, handler);
        } finally {
            writeLock.unlock();
        }
    }

    public <T extends Event> boolean unregister(Consumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer cannot be null");
        writeLock.lock();
        try {
            boolean removed = false;
            for(Class<? extends Event> clazz : consumerMap.keySet()) {
                Collection<Consumer<Event>> consumers = consumerMap.get(clazz);
                for (Iterator<Consumer<Event>> iterator = consumers.iterator(); iterator.hasNext(); ) {
                    if (iterator.next() == consumer) {
                        iterator.remove();
                        removed = true;
                    }
                }
            }
            return removed;
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> T post(T event) {
        Preconditions.checkNotNull(event, "event cannot be null");
        readLock.lock();
        try {
            for (Class zuper = event.getClass(); zuper != null && zuper != Event.class; zuper = zuper.getSuperclass()) {
                Collection<Consumer<Event>> consumers = consumerMap.get(zuper);
                for (Consumer<Event> consumer : consumers) {
                    consumer.accept(event);
                }
            }
            return event;
        } finally {
            readLock.unlock();
        }
    }
}