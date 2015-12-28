package pw.sponges.botserver.cmd.commands.steam;

import org.json.JSONArray;
import org.json.JSONObject;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.FileUtils;
import pw.sponges.botserver.util.Msg;
import pw.sponges.botserver.util.Scheduler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SteamStatusCommand extends Command {

    private ServicesStatus servicesStatus = null;
    private MatchMakingStatus matchMakingStatus = null;
    private final List<DatacenterStatus> datacenters = new ArrayList<>();

    public SteamStatusCommand() {
        super("command.steamstatus", UserRole.USER, "Checks the status of steam servers", "steamstatus", "ss", "steam", "steamdown", "checksteam", "ststatus", "steamstats", "csgostatus", "csstatus", "cs:status", "csgoservers", "csservers");

        Scheduler.runAsyncTaskRepeat(this::reload, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                reload();
                request.reply("Reloaded steam!");
                return;
            }
        }

        if (datacenters.isEmpty() || servicesStatus == null || matchMakingStatus == null) {
            request.reply("Steam server status' hasn't been loaded yet! Please wait a few minutes!");
            return;
        }

        StringBuilder response = new StringBuilder();

        {
            response.append("Services:");
            response.append("\n- Items: ").append(servicesStatus.getItems().name().toLowerCase());
            response.append("\n- Sessions: ").append(servicesStatus.getSessions().name().toLowerCase());
            response.append("\n- Community: ").append(servicesStatus.getCommunity().name().toLowerCase());
            response.append("\n- LeaderBoards: ").append(servicesStatus.getLeaderboards().name().toLowerCase());
        }

        {
            response.append("\nCS:GO Matchmaking:");
            response.append("\n- Scheduler: ").append(matchMakingStatus.getScheduler().name().toLowerCase());
            response.append("\n- Online servers: ").append(matchMakingStatus.getOnlineServers());
            response.append("\n- Online players: ").append(matchMakingStatus.getOnlinePlayers());
            response.append("\n- Searching players: ").append(matchMakingStatus.getSearchingPlayers());
            response.append("\n- Avg search time (seconds): ").append(matchMakingStatus.getSearchSecondsAverage());
        }

        {
            response.append("\nDatacenters: (format = name: load, capacity)");

            for (DatacenterStatus status : datacenters) {
                response.append(String.format("\n- %s: %s, %s", status.getName(), status.getLoad().name().toLowerCase(), status.getCapacity().name().toLowerCase()));
            }
        }

        request.reply(response.toString());
    }

    private void reload() {
        Msg.debug("reloading steam status...");

        String result;
        try {
            result = FileUtils.readUrl(true, new URL("http://is.steam.rip/api/v1/?request=SteamStatus"));
        } catch (MalformedURLException e) {
            Msg.debug("malf url");
            e.printStackTrace();
            return;
        }
        JSONObject json = null;

        if (result != null) {
            Msg.debug("Parsing results...");
            json = new JSONObject(result).getJSONObject("result");
            Msg.debug("Parsed results!");
        } else {
            Msg.debug("Result is null????");
        }

        if (json == null || !json.getBoolean("success")) {
            Msg.debug("could not reload steam status!");
            return;
        }

        JSONObject statuses = json.getJSONObject("SteamStatus");
        Msg.debug(statuses.toString());

        {
            Msg.debug("setting up servicesstatus");
            ServicesStatus servicesStatus = new ServicesStatus(statuses.getJSONObject("services"));
            Msg.debug("set up up servicesstatus");
            this.servicesStatus = servicesStatus;

            Msg.debug(toString());
        }

        {
            MatchMakingStatus status = new MatchMakingStatus(statuses.getJSONObject("matchmaking"));
            this.matchMakingStatus = status;
            Msg.debug(status.toString());
        }

        {
            datacenters.clear();
            JSONArray array = statuses.getJSONArray("datacenters");

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                DatacenterStatus status = new DatacenterStatus(object);
                datacenters.add(status);
                Msg.debug(status.toString());
            }
        }

        Msg.debug("reloaded steam status!");
    }


} enum ServiceStatus {

    NORMAL, IDLE, OFFLINE

} class ServicesStatus {

    private final ServiceStatus sessions, community, items, leaderboards;

    public ServicesStatus(JSONObject json) {
        Msg.debug("services status instantiated");
        this.sessions = getStatus(json.getString("SessionsLogon"));
        this.community = getStatus(json.getString("SteamCommunity"));
        this.items = getStatus(json.getString("IEconItems"));
        this.leaderboards = getStatus(json.getString("LeaderBoards"));

        Msg.debug(toString());
    }

    private ServiceStatus getStatus(String status) {
        ServiceStatus s = ServiceStatus.valueOf(status.toUpperCase());

        if (s == null) {
            Msg.warning("invalid status type " + status);
            return ServiceStatus.OFFLINE;
        } else {
            return s;
        }
    }

    public ServiceStatus getSessions() {
        return sessions;
    }

    public ServiceStatus getCommunity() {
        return community;
    }

    public ServiceStatus getItems() {
        return items;
    }

    public ServiceStatus getLeaderboards() {
        return leaderboards;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("sessions", sessions.name().toLowerCase())
                .put("community", community.name().toLowerCase())
                .put("items", items.name().toLowerCase())
                .put("leaderboards", leaderboards.name().toLowerCase())
                .toString();
    }

} class MatchMakingStatus {

    private final ServiceStatus scheduler;
    private final int onlineServers, onlinePlayers, searchingPlayers, searchSecondsAverage;

    public MatchMakingStatus(JSONObject json) {
        this.scheduler = getStatus(json.getString("scheduler"));
        this.onlineServers = json.getInt("online_servers");
        this.onlinePlayers = json.getInt("online_players");
        this.searchingPlayers = json.getInt("searching_players");
        this.searchSecondsAverage = json.getInt("search_seconds_avg");
    }

    private ServiceStatus getStatus(String status) {
        ServiceStatus s = ServiceStatus.valueOf(status.toUpperCase());

        if (s == null) {
            Msg.warning("invalid status type " + status);
            return ServiceStatus.OFFLINE;
        } else {
            return s;
        }
    }

    public ServiceStatus getScheduler() {
        return scheduler;
    }

    public int getOnlineServers() {
        return onlineServers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getSearchingPlayers() {
        return searchingPlayers;
    }

    public int getSearchSecondsAverage() {
        return searchSecondsAverage;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("scheduler", scheduler.name().toLowerCase())
                .put("online-servers", onlineServers)
                .put("online-players", onlinePlayers)
                .put("searching-players", searchingPlayers)
                .put("search-seconds-average", searchSecondsAverage)
                .toString();
    }
} enum DCLoad {

    OFFLINE, HIGH, MEDIUM, LOW, IDLE

} enum DCCapacity {

    OFFLINE, FULL, MEDIUM, LOW, IDLE

} class DatacenterStatus {

    private final DCLoad load;
    private final String name;
    private final DCCapacity capacity;

    public DatacenterStatus(JSONObject json) {
        load = getLoad(json.getString("load"));
        name = json.getString("datacenter");
        capacity = getCapacity(json.getString("capacity"));
    }

    private DCLoad getLoad(String load) {
        DCLoad dcl = DCLoad.valueOf(load.toUpperCase());

        if (dcl == null) {
            Msg.warning("invalid load type " + load);
            return DCLoad.OFFLINE;
        } else {
            return dcl;
        }
    }

    private DCCapacity getCapacity(String capacity) {
        DCCapacity dcl = DCCapacity.valueOf(capacity.toUpperCase());

        if (dcl == null) {
            Msg.warning("invalid capacity type " + capacity);
            return DCCapacity.OFFLINE;
        } else {
            return dcl;
        }
    }

    public DCLoad getLoad() {
        return load;
    }

    public String getName() {
        return name;
    }

    public DCCapacity getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("load", load.name().toLowerCase())
                .put("name", name)
                .put("capacity", capacity.name().toLowerCase())
                .toString();
    }
}
