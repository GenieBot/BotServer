package io.sponges.bot.server.entities.data;

import io.sponges.bot.api.entities.data.ClientData;

import java.util.Date;
import java.util.Optional;

public class ClientDataImpl implements ClientData {

    private Optional<String> name = Optional.empty();
    private Optional<Date> connectedDate = Optional.empty();

    @Override
    public Optional<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Optional.of(name);
    }

    @Override
    public Optional<Date> getConnectedDate() {
        return connectedDate;
    }

    public void setConnectedDate(Date connectedDate) {
        this.connectedDate = Optional.of(connectedDate);
    }
}
