package io.sponges.bot.server.webhook;

import io.sponges.bot.api.webhook.Webhook;
import io.sponges.bot.api.webhook.WebhookMessage;
import spark.Request;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class WebhookImpl implements Webhook {

    private final List<Consumer<WebhookMessage>> consumers = new CopyOnWriteArrayList<>();

    private final String id;
    private final String url;

    protected WebhookImpl(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public void registerListener(Consumer<WebhookMessage> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void unregisterListener(Consumer<WebhookMessage> consumer) {
        consumers.remove(consumer);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void accept(Request request) {
        WebhookMessage message = new WebhookMessageImpl(request);
        consumers.forEach(consumer -> consumer.accept(message));
    }
}
