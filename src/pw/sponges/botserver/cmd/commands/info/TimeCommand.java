package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCommand extends Command {

    public TimeCommand() {
        super("command.time", UserRole.USER, "shows the time across the world", "time", "worldtime", "clock", "worldclock");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        Date gmt = new Date();
        SimpleDateFormat gmtFormat = new SimpleDateFormat("HH:mm:ss");
        String gmtTime = gmtFormat.format(gmt);
        stringBuilder.append("GMT: " + gmtTime);

        Date est = new Date(); est.setHours(est.getHours() - 5);
        SimpleDateFormat estFormat = new SimpleDateFormat("HH:mm:ss");
        String estTime = estFormat.format(est);
        stringBuilder.append("\nEST: " + estTime);

        Date pst = new Date(); pst.setHours(pst.getHours() - 8);
        SimpleDateFormat pstFormat = new SimpleDateFormat("HH:mm:ss");
        String pstTime = pstFormat.format(pst);
        stringBuilder.append("\nPST: " + pstTime);

        request.reply("Times across the world:\n" + stringBuilder.toString());
    }
}
