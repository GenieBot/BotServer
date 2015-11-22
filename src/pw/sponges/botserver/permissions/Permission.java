package pw.sponges.botserver.permissions;

public enum Permission {

    ADMIN(false), MOD(false), USER(true);

    private boolean allowed;

    Permission(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

}
