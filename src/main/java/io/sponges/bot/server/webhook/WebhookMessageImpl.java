package io.sponges.bot.server.webhook;

import io.sponges.bot.api.webhook.WebhookMessage;
import spark.Request;

import java.util.Set;

public class WebhookMessageImpl implements WebhookMessage {

    private final Request request;

    protected WebhookMessageImpl(Request request) {
        this.request = request;
    }

    @Override
    public String getBody() {
        return request.body();
    }

    @Override
    public Set<String> getHeaders() {
        return request.headers();
    }

    @Override
    public boolean isHeader(String s) {
        return request.headers().contains(s);
    }

    @Override
    public String getHeader(String s) {
        return request.headers(s);
    }
}
