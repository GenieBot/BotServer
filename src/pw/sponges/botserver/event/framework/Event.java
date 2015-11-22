package pw.sponges.botserver.event.framework;

public abstract class Event {

    /**
     * Will return the room id if the event will need room loaded checks!
     * @return room id
     */
    public abstract String needsChecks();

}
