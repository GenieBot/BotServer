package pw.sponges.botserver.event.framework;

import pw.sponges.botserver.event.events.*;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.util.Msg;

public class EventManager {

    private Database database;
    private PermissionsManager permissions;
    private Listener listener = null;

    public EventManager(Database database, PermissionsManager permissions) {
        this.database = database;
        this.permissions = permissions;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void handle(Event event) {
        runChecks(event);

        if (event instanceof ConnectEvent) {
            listener.onConnect((ConnectEvent) event);
        } else if (event instanceof ClientInputEvent) {
            listener.onClientInput((ClientInputEvent) event);
        } else if (event instanceof ChatMessageEvent) {
            listener.onChatMessage((ChatMessageEvent) event);
        } else if (event instanceof CommandRequestEvent) {
            listener.onCommandRequest((CommandRequestEvent) event);
        } else if (event instanceof LinkRequestEvent) {
            listener.onLinkRequest((LinkRequestEvent) event);
        }
    }

    /**
     * Checks to see if the room that the event is ran in has been loaded
     * @param event the event that may need checks
     */
    private void runChecks(Event event) {
        String room = event.needsChecks();

        if (room == null) return;

        if (!database.isLoaded(room)) {
            database.load(room);

            Msg.debug("Loaded settings for " + room + "!\n" + database.getData(room).toJson());
        }

        if (!permissions.isLoaded(room)) {
            permissions.loadPermissions(room);

            Msg.debug("Loaded permissions for " + room + "!");
        }
    }

}