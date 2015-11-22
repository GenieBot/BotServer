package pw.sponges.botserver.storage.impl;

import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.storage.RoomGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomGroupsImpl implements RoomGroups {

    private List<Group> groups;
    private Map<String, Group> users;

    public RoomGroupsImpl() {
    }

    @Override
    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public Map<String, Group> getUsers() {
        return users;
    }

    @Override
    public void setValues(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public void setValues(Map<String, Group> users) {
        this.users = users;
    }

    @Override
    public List<Group> getDefaultGroups(String room) {
        Group defaultGroup = new Group("default");
        defaultGroup.addPermission("command.about");
        defaultGroup.addPermission("command.help");
        defaultGroup.addPermission("command.urban");
        defaultGroup.addPermission("command.chatinfo");
        defaultGroup.addPermission("command.stats");
        defaultGroup.addPermission("command.test");
        defaultGroup.addPermission("command.userinfo");
        defaultGroup.addPermission("command.mcnetworks");
        defaultGroup.addPermission("command.mcserver");

        Group admin = new Group("admin", "default");
        admin.addPermission("command.link");

        Group op = new Group("op", "admin");
        admin.addPermission("command.op.clients");
        admin.addPermission("command.op.joinroom");
        admin.addPermission("command.op.stop");

        return new ArrayList<Group>() {{
            add(defaultGroup);
            add(admin);
            add(op);
        }};
    }

}
