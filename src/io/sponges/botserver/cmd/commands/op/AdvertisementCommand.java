package io.sponges.botserver.cmd.commands.op;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.util.Advertisements;

public class AdvertisementCommand extends Command {

    private final Advertisements advertisements;

    public AdvertisementCommand() {
        super("command.op.advertisement", UserRole.OP, null, "advertisement", "ad", "adv", "advertise", "promote", "advertisements");

        this.advertisements = Advertisements.getInstance();
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: advertise [add/list/remove/get]");
            return;
        }

        String subcommand = args[0];

        if (subcommand.equalsIgnoreCase("get")) {
            if (args.length < 2) {
                request.reply("Invalid usage!");
                return;
            }

            int index = -1;
            try {
                index = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                request.reply("Could not parse integer " + args[1] + "!");
                return;
            }

            if (index == -1) {
                request.reply("Invalid index -1! Are there any errors?");
                return;
            }

            if (advertisements.getAdvertisements().size() < index) {
                request.reply("Invalid index, max index smaller than inputted.");
                return;
            }

            request.reply(advertisements.getAdvertisements().get(index));
        } else if (subcommand.equalsIgnoreCase("list")) {
            StringBuilder builder = new StringBuilder("Running advertisements:");

            for (int i = 0; i < advertisements.getAdvertisements().size(); i++) {
                builder.append("\n").append(i).append(") ").append(advertisements.getAdvertisements().get(i));
            }

            request.reply(builder.toString());
        } else if (subcommand.equalsIgnoreCase("add")) {
            StringBuilder builder = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]);

                if (i != args.length - 1) {
                    builder.append(" ");
                }
            }

            advertisements.getAdvertisements().add(builder.toString());
            request.reply("Added new advertisement!");
        } else if (subcommand.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                request.reply("Invalid usage!");
                return;
            }

            int index = -1;
            try {
                index = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                request.reply("Could not parse integer " + args[1] + "!");
                return;
            }

            if (index == -1) {
                request.reply("Invalid index -1! Are there any errors?");
                return;
            }

            if (advertisements.getAdvertisements().size() < index) {
                request.reply("Invalid index, max index smaller than inputted.");
                return;
            }

            request.reply("Removed " + advertisements.getAdvertisements().remove(index) + "!");
        } else {
            request.reply("Invalid usage!");
        }
    }
}
