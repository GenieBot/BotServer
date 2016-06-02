package io.sponges.bot.server.entities.data;

import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.data.UserData;

import java.util.Optional;

public class UserDataImpl implements UserData {

    private Optional<PrivateChannel> privateChannel = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<String> displayName = Optional.empty();
    private Optional<String> profileUrl = Optional.empty();
    private Optional<String> profileImage = Optional.empty();
    private Optional<String> mood = Optional.empty();
    private Optional<String> status = Optional.empty();

    @Override
    public Optional<PrivateChannel> getPrivateChannel() {
        return privateChannel;
    }

    public void setPrivateChannel(PrivateChannel privateChannel) {
        this.privateChannel = Optional.of(privateChannel);
    }

    @Override
    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Optional.of(username);
    }

    @Override
    public Optional<String> getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = Optional.of(displayName);
    }

    @Override
    public Optional<String> getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = Optional.of(profileUrl);
    }

    @Override
    public Optional<String> getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = Optional.of(profileImage);
    }

    @Override
    public Optional<String> getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = Optional.of(mood);
    }

    @Override
    public Optional<String> getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = Optional.of(status);
    }

}
