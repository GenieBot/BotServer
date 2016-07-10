package io.sponges.bot.server.webhook;

import io.sponges.bot.api.webhook.Webhook;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.webhook.server.WebhookServer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebhookManagerImpl implements WebhookManager {

    private final Map<String, Webhook> webhooks = new ConcurrentHashMap<>();

    private final WebhookServer webhookServer;

    public WebhookManagerImpl(WebhookServer webhookServer) {
        this.webhookServer = webhookServer;
    }

    @Override
    public Collection<Webhook> getWebhooks() {
        return Collections.unmodifiableCollection(webhooks.values());
    }

    @Override
    public boolean isWebhook(String s) {
        return webhooks.containsKey(s);
    }

    @Override
    public Webhook getWebhook(String s) {
        return webhooks.get(s);
    }

    @Override
    public Webhook create(String s, String s1) {
        Webhook webhook = new WebhookImpl(s, s1);
        register(webhook);
        return webhook;
    }

    @Override
    public void register(Webhook webhook) {
        webhooks.put(webhook.getId(), webhook);
        webhookServer.register((WebhookImpl) webhook);
    }
}
