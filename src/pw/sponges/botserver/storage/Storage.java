package pw.sponges.botserver.storage;

public interface Storage {

    RoomData load(String room);

    void save(String room);

}
