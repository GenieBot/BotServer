package io.sponges.botserver.event.framework;

import io.sponges.botserver.event.events.*;
import io.sponges.botserver.framework.Room;
import io.sponges.botserver.storage.Database;
import io.sponges.botserver.util.Msg;
import io.sponges.botserver.util.Scheduler;

public class EventManager {

    // TODO change to charries' EventBus

    private Database database;
    private Listener listener = null;

    public EventManager(Database database) {
        this.database = database;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void handle(Event event) {
        Scheduler.runAsyncTask(() -> handleEvent(event));
    }

    public void handleEvent(Event event) {
        runChecks(event);

        if (event instanceof ConnectEvent) {
            listener.onConnect((ConnectEvent) event);
        } else if (event instanceof ClientInputEvent) {
            listener.onClientInput((ClientInputEvent) event);
        } else if (event instanceof ChatMessageEvent) {
            listener.onChatMessage((ChatMessageEvent) event);
        } else if (event instanceof CommandRequestEvent) {
            listener.onCommandRequest((CommandRequestEvent) event);
        } else if (event instanceof UserJoinEvent) {
            listener.onUserJoin((UserJoinEvent) event);
        }
    }

    /**
     * Checks to see if the room that the event is ran in has been loaded
     * @param event the event that may need checks
     */
    private void runChecks(Event event) {
        Room room = event.needsChecks();

        if (room == null) {
            return;
        }

        if (!database.isLoaded(room)) {
            database.load(room);

            Msg.debug("Loaded settings for " + room + "!\n" + room.getRoomData().toJson());
        }
    }

}