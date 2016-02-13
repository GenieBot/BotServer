package io.sponges.bot.server.cmd.commands.info;

import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.cmd.framework.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCommand extends Command {

    public TimeCommand() {
        super("command.time", UserRole.USER, "shows the time across the world", "time", "worldtime", "clock", "worldclock");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        StringBuilder stringBuilder = new StringBuilder("Times across the world:\n");

        Date gmt = new Date();
        SimpleDateFormat gmtFormat = new SimpleDateFormat("HH:mm:ss");
        String gmtTime = gmtFormat.format(gmt);
        stringBuilder.append("GMT: ").append(gmtTime);

        Date est = new Date(); est.setHours(est.getHours() - 5);
        SimpleDateFormat estFormat = new SimpleDateFormat("HH:mm:ss");
        String estTime = estFormat.format(est);
        stringBuilder.append("\nEST: ").append(estTime);

        Date pst = new Date(); pst.setHours(pst.getHours() - 8);
        SimpleDateFormat pstFormat = new SimpleDateFormat("HH:mm:ss");
        String pstTime = pstFormat.format(pst);
        stringBuilder.append("\nPST: ").append(pstTime);

        request.reply(stringBuilder.toString());
    }
}
