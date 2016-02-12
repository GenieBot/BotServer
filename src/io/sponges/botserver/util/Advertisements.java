package io.sponges.botserver.util;

import java.util.ArrayList;
import java.util.List;

public class Advertisements {

    private static Advertisements instance = null;

    private final List<String> advertisements = new ArrayList<>();

    public Advertisements() {
    }

    public static Advertisements getInstance() {
        if (instance == null) {
            instance = new Advertisements();
        }

        return instance;
    }

    public List<String> getAdvertisements() {
        return advertisements;
    }

}
