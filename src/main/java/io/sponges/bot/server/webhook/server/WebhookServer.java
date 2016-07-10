package io.sponges.bot.server.webhook.server;

import io.sponges.bot.api.webhook.WebhookManager;
import io.sponges.bot.server.webhook.WebhookImpl;
import io.sponges.bot.server.webhook.WebhookManagerImpl;
import spark.Service;
import spark.Spark;

public class WebhookServer {

    private final WebhookManager webhookManager = new WebhookManagerImpl(this);

    private final Service service;

    public WebhookServer(int port) {
        this.service = Service.ignite();
        this.service.port(port);
        this.service.exception(Exception.class, (e, request, response) -> e.printStackTrace());
    }

    public void register(WebhookImpl webhook) {
        service.post(webhook.getUrl(), ((request, response) -> {
            webhook.accept(request);
            response.status(200);
            return "OK";
        }));
    }

    public void stop() {
        Spark.stop();
    }

    public WebhookManager getWebhookManager() {
        return webhookManager;
    }
}
