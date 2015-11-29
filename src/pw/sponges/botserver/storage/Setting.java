package pw.sponges.botserver.storage;

public enum Setting {

    ADMIN_ONLY(false), BOT_NAME("SpongyBot"), PREFIX("$");

    private Object object;

    Setting(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase().replace("_", "-");
    }
}