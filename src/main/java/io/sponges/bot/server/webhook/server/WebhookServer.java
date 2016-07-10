package io.sponges.bot.server.webhook.server;

import io.sponges.bot.api.webhook.Webhook;
import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.webhook.WebhookImpl;
import io.sponges.bot.server.webhook.WebhookManagerImpl;
import spark.Spark;

public class WebhookServer {

    private final WebhookManager webhookManager = new WebhookManagerImpl(this);

    public WebhookServer(int port) {
        Spark.port(port);
    }

    public void register(WebhookImpl webhook) {
        Spark.post(webhook.getUrl(), ((request, response) -> {
            webhook.accept(request);
            return "OK";
        }));
    }

    public void unregister(Webhook webhook) {
        // TODO webhook unregistration
    }

    public void stop() {
        Spark.stop();
    }

    public WebhookManager getWebhookManager() {
        return webhookManager;
    }
}
