package io.sponges.botserver.cmd.commands.info;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.framework.Room;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.util.Reddit;
import io.sponges.botserver.util.Scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedditCommand extends Command {

    private enum FeedAction {
        SUBSCRIBE, UNSUBSCRIBE
    }

    private final Map<Room, String> feeds = new HashMap<>();
    private final Map<String, Double> last = new HashMap<>();

    private final Reddit reddit;
    private final Submissions submissions;

    public RedditCommand() {
        super("command.reddit", UserRole.ADMIN, "browses reddit", "reddit", "subreddit");

        this.reddit = Reddit.getInstance();
        this.submissions = new Submissions(reddit.getClient(), reddit.getUser());

        Scheduler.runAsyncTaskRepeat(() -> {
            if (feeds.isEmpty()) return;

            for (Room room : feeds.keySet()) {
                String sr = feeds.get(room);

                List<Submission> subs = submissions.ofSubreddit(sr, SubmissionSort.NEW, -1, 1, null, null, true);

                if (subs.size() == 0) {
                    continue;
                }

                if (last.containsKey(sr)) {
                    double last = this.last.get(sr);
                    double newLast = subs.get(0).getCreated();

                    if (last == newLast) {
                        continue;
                    }
                }

                StringBuilder builder = new StringBuilder("New submissions: ");

                last.put(sr, subs.get(0).getCreated());

                for (Submission submission : subs) {
                    builder.append("\n").append(submission.getTitle());

                    if (submission.isSelf()) {
                        builder.append("\n").append(submission.getSelftext());
                    } else {
                        builder.append(": ").append(submission.getURL());
                    }
                }

                room.sendMessage(builder.toString());
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    // 0      1    2           3
    //        0    1           2
    // reddit feed subscribe   subreddit
    // reddit feed unsubscribe

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("st00pid! Usage: TODO" /* TODO */);
            return;
        }

        if (args[0].equalsIgnoreCase("feed")) {
            if (args.length < 2) {
                request.reply("Invalid args fam!");
                return;
            }

            FeedAction action = FeedAction.valueOf(args[1].toUpperCase());
            if (action == null) {
                request.reply("Invalid action! Actions: subscribe, unsubscribe");
                return;
            }

            if (action == FeedAction.SUBSCRIBE) {
                if (feeds.containsKey(request.getRoom())) {
                    request.reply("You are already subscribed to " + feeds.get(request.getRoom()) + "!");
                    return;
                }

                if (args.length < 3) {
                    request.reply("Invalid args!");
                    return;
                }

                String subreddit = args[2].toLowerCase();
                feeds.put(request.getRoom(), subreddit);
                request.reply("Subscription to " + subreddit + " added!");
            } else {
                // TODO unsubbing
            }
        }
    }
}
