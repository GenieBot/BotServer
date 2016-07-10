package io.sponges.bot.server.webhook;

import io.sponges.bot.api.webhook.WebhookMessage;

import java.util.Set;

public class WebhookMessageImpl implements WebhookMessage {

    private final String body;
    private final Set<String> headers;

    public WebhookMessageImpl(String body, Set<String> headers) {
        this.body = body;
        this.headers = headers;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public Set<String> getHeaders() {
        return headers;
    }
}
