package pw.sponges.botserver.storage;

public interface Database {

    RoomData getData(String room);

    RoomData load(String room);

    void save(String room);

    boolean isLoaded(String room);

}
