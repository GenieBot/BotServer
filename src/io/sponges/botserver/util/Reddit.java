package io.sponges.botserver.util;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.PoliteHttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Reddit {

    private boolean connected = false;

    public static Reddit reddit = null;

    private final RestClient client;
    private final User user;

    public Reddit() {
        this.client = new PoliteHttpRestClient();
        this.client.setUserAgent("SpongyBot/2.0");
        this.user = new User(client, "spongybot", "LkthL$.ehF_NSd@n/A01#\\y\\Djc49|h:!oY\\#|fvm5em+xYYfe");
    }

    public static Reddit getInstance() {
        if (reddit == null) {
            reddit = new Reddit();
        }

        return reddit;
    }

    public RestClient getClient() {
        return client;
    }

    public User getUser() {
        if (!connected) {
            try {
                user.connect();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }

            connected = true;
        }

        return user;
    }

    public boolean isConnected() {
        return connected;
    }
}
