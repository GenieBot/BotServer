package pw.sponges.botserver.messages;

public enum ResponseOption {

    TEXT, KICK;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
