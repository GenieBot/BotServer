package pw.sponges.botserver.storage;

import pw.sponges.botserver.permissions.Group;

import java.util.List;
import java.util.Map;

public interface RoomGroups {

    List<Group> getGroups();

    Map<String, Group> getUsers();

    void setValues(List<Group> groups);

    void setValues(Map<String, Group> users);

    List<Group> getDefaultGroups(String room);

}
