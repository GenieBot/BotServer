package pw.sponges.botserver.cmd.framework;

public abstract class Command {

    private final String permission;
    private final String[] names;

    public Command(String permission, String... names) {
        this.permission = permission;
        this.names = names;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getNames() {
        return names;
    }

    public abstract void onCommand(CommandRequest request, String[] args);
}
