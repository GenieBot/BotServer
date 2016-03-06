package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.PrivateChannel;

import java.util.List;
import java.util.Optional;

public class UserImpl implements User {

    private Optional<PrivateChannel> privateChannel = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<String> displayName = Optional.empty();
    private Optional<String> profileUrl = Optional.empty();
    private Optional<String> profileImage = Optional.empty();
    private Optional<String> profileMood = Optional.empty();

    private final String id;
    private final Network network;

    public UserImpl(String id, Network network) {
        this.id = id;
        this.network = network;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public List<Channel> getChannels() {
        return network.getChannelManager().getChannels(this);
    }

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
    public Optional<String> getProfileURL() {
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
    public Optional<String> getProfileMood() {
        return profileMood;
    }

    public void setProfileMood(String profileMood) {
        this.profileMood = Optional.of(profileMood);
    }
}
