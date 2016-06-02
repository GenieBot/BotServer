package io.sponges.bot.server.entities.data;

import io.sponges.bot.api.entities.data.NetworkData;

import java.util.Optional;

public class NetworkDataImpl implements NetworkData {

    private Optional<String> name = Optional.empty();
    private Optional<String> image = Optional.empty();
    private Optional<String> description = Optional.empty();

    @Override
    public Optional<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Optional.of(name);
    }

    @Override
    public Optional<String> getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = Optional.of(image);
    }

    @Override
    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Optional.of(description);
    }
}
