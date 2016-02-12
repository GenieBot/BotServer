package io.sponges.botserver.cmd.commands.info;

import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;

public class TestCommand extends Command {

    public TestCommand() {
        super("command.test", UserRole.USER, "like ping pong but without a ball", "test", "t");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        //if (request.getClient().getId().equals("skype")) request.reply("<ss type=\"monkey\">(monkey)</ss> <ss type=\"smile\">:)</ss> <ss type=\"sad\">:(</ss> <ss type=\"laugh\">:D</ss> <ss type=\"cwl\">(cwl)</ss> <ss type=\"cool\">(cool)</ss> <ss type=\"surprised\">:O</ss> <ss type=\"wink\">;)</ss> <ss type=\"cry\">;(</ss> <ss type=\"sweat\">(:|</ss> <ss type=\"speechless\">:|</ss> <ss type=\"kiss\">(kiss)</ss> <ss type=\"tongueout\">:P</ss> <ss type=\"blush\">:$</ss> <ss type=\"wonder\">:^)</ss> <ss type=\"sleepy\">|-)</ss> <ss type=\"dull\">|-(</ss> <ss type=\"inlove\">(inlove)</ss> <ss type=\"eg\">]:)</ss> <ss type=\"yawn\">(yawn)</ss> <ss type=\"puke\">(puke)</ss> <ss type=\"doh\">(doh)</ss> <ss type=\"angry\">(angry)</ss> <ss type=\"wasntme\">(wasntme)</ss> <ss type=\"party\">(party)</ss> <ss type=\"worry\">(worry)</ss> <ss type=\"mmm\">(mm)</ss> <ss type=\"nerdy\">(nerd)</ss> <ss type=\"lipssealed\">:x</ss> <ss type=\"devil\">(devil)</ss> <ss type=\"angel\">(angel)</ss> <ss type=\"envy\">(envy)</ss> <ss type=\"makeup\">(makeup)</ss> <ss type=\"movember\">(movember)</ss> <ss type=\"think\">(think)</ss> <ss type=\"rofl\">(rofl)</ss> <ss type=\"happy\">(happy)</ss> <ss type=\"smirk\">(smirk)</ss> <ss type=\"nod\">(nod)</ss> <ss type=\"shake\">(shake)</ss> <ss type=\"waiting\">(waiting)</ss> <ss type=\"emo\">(emo)</ss> <ss type=\"fingerscrossed\">(yn)</ss> <ss type=\"hi\">(wave)</ss> <ss type=\"facepalm\">(facepalm)</ss> <ss type=\"wait\">(wait)</ss> <ss type=\"giggle\">(chuckle)</ss> <ss type=\"clap\">(clap)</ss> <ss type=\"whew\">(whew)</ss> <ss type=\"highfive\">(highfive)</ss> <ss type=\"tmi\">(tmi)</ss> <ss type=\"call\">(call)</ss> <ss type=\"headbang\">(headbang)</ss> <ss type=\"idea\">(idea)</ss> <ss type=\"lalala\">(lalala)</ss> <ss type=\"punch\">(punch)</ss> <ss type=\"rock\">(rock)</ss> <ss type=\"swear\">(swear)</ss> <ss type=\"talk\">(talk)</ss> <ss type=\"headphones\">(headphones)</ss> <ss type=\"talktothehand\">(talktothehand)</ss> <ss type=\"sarcastic\">(sarcastic)</ss> <ss type=\"listening\">(listening)</ss> <ss type=\"slap\">(slap)</ss> <ss type=\"whistle\">(whistle)</ss> <ss type=\"donttalktome\">(donttalk)</ss> <ss type=\"nazar\">(nazar)</ss> <ss type=\"bandit\">(bandit)</ss> <ss type=\"learn\">(learn)</ss> <ss type=\"lips\">(lips)</ss> <ss type=\"heart\">(heart)</ss> <ss type=\"brokenheart\">(u)</ss> <ss type=\"joy\">(joy)</ss> <ss type=\"anger\">(anger)</ss> <ss type=\"sadness\">(sadness)</ss> <ss type=\"disgust\">(disgust)</ss> <ss type=\"fear\">(fear)</ss> <ss type=\"yes\">(y)</ss> <ss type=\"no\">(n)</ss> <ss type=\"ok\">(ok)</ss> <ss type=\"handshake\">(handshake)</ss> <ss type=\"fistbump\">(fistbump)</ss> <ss type=\"praying\">(pray)</ss> <ss type=\"poke\">(poke)</ss> <ss type=\"victory\">(victory)</ss> <ss type=\"handsinair\">(celebrate)</ss> <ss type=\"naturescall\">(ek)</ss> <ss type=\"muscle\">(flex)</ss> <ss type=\"man\">(man)</ss> <ss type=\"woman\">(woman)</ss> <ss type=\"bow\">(bow)</ss> <ss type=\"gottarun\">(gottarun)</ss> <ss type=\"stop\">(stop)</ss> <ss type=\"dance\">\\o/</ss> <ss type=\"discodancer\">(disco)</ss> <ss type=\"bhangra\">(bhangra)</ss> <ss type=\"zombie\">(zombie)</ss> <ss type=\"bertlett\">(bartlett)</ss> <ss type=\"footballfail\">(footballfail)</ss> <ss type=\"pullshot\">(pullshot)</ss> <ss type=\"bowled\">(bowled)</ss> <ss type=\"bike\">(bike)</ss> <ss type=\"suryannamaskar\">(suryannamaskar)</ss> <ss type=\"yoga\">(yoga)</ss> <ss type=\"ninja\">(ninja)</ss> <ss type=\"shopping\">(shopping)</ss> <ss type=\"muscleman\">(muscleman)</ss> <ss type=\"skipping\">(skipping)</ss> <ss type=\"bollylove\">(bollylove)</ss> <ss type=\"chappal\">(chappal)</ss> <ss type=\"nahi\">(nahi)</ss> <ss type=\"promise\">(promise)</ss> <ss type=\"kaanpakadna\">(kaanpakadna)</ss> <ss type=\"computerrage\">(computerrage)</ss> <ss type=\"cat\">(cat)</ss> <ss type=\"dog\">(dog)</ss> <ss type=\"hug\">(hug)</ss> <ss type=\"heidy\">(heidy)</ss> <ss type=\"donkey\">(donkey)</ss> <ss type=\"snail\">(snail)</ss> <ss type=\"flower\">(F)</ss> <ss type=\"goodluck\">(goodluck)</ss> <ss type=\"sun\">(sun)</ss> <ss type=\"island\">(island)</ss> <ss type=\"rain\">(rain)</ss> <ss type=\"umbrella\">(umbrella)</ss> <ss type=\"rainbow\">(rainbow)</ss> <ss type=\"star\">(*)</ss> <ss type=\"tumbleweed\">(tumbleweed)</ss> <ss type=\"pizza\">(pi)</ss> <ss type=\"cake\">(^)</ss> <ss type=\"coffee\">(coffee)</ss> <ss type=\"beer\">(beer)</ss> <ss type=\"drink\">(d)</ss> <ss type=\"cheese\">(cheese)</ss> <ss type=\"chai\">(chai)</ss> <ss type=\"turkey\">(turkey)</ss> <ss type=\"tandoorichicken\">(tandoori)</ss> <ss type=\"laddu\">(laddu)</ss> <ss type=\"bell\">(bell)</ss> <ss type=\"diya\">(diwali)</ss> <ss type=\"hanukkah\">(hanukkah)</ss> <ss type=\"fireworks\">(fireworks)</ss> <ss type=\"tubelight\">(tubelight)</ss> <ss type=\"canyoutalk\">(canyoutalk)</ss> <ss type=\"camera\">(camera)</ss> <ss type=\"plane\">(plane)</ss> <ss type=\"car\">(car)</ss> <ss type=\"rickshaw\">(rickshaw)</ss> <ss type=\"computer\">(computer)</ss> <ss type=\"wfh\">(wfh)</ss> <ss type=\"brb\">(brb)</ss> <ss type=\"games\">(games)</ss> <ss type=\"phone\">(mp)</ss> <ss type=\"holdon\">(holdon)</ss> <ss type=\"letsmeet\">(letsmeet)</ss> <ss type=\"mail\">(e)</ss> <ss type=\"confidential\">(confidential)</ss> <ss type=\"bomb\">(bomb)</ss> <ss type=\"cash\">(cash)</ss> <ss type=\"movie\">(~)</ss> <ss type=\"music\">(music)</ss> <ss type=\"time\">(o)</ss> <ss type=\"whatsgoingon\">(whatsgoingon)</ss> <ss type=\"skype\">(skype)</ss> <ss type=\"mlt\">(malthe)</ss> <ss type=\"taur\">(tauri)</ss> <ss type=\"toivo\">(toivo)</ss> <ss type=\"priidu\">(zilmer)</ss> <ss type=\"oliver\">(oliver)</ss> <ss type=\"poolparty\">(poolparty)</ss> <ss type=\"mooning\">(mooning)</ss> <ss type=\"drunk\">(drunk)</ss> <ss type=\"smoke\">(smoking)</ss> <ss type=\"bug\">(bug)</ss> <ss type=\"sheep\">(sheep)</ss> <ss type=\"win10\">(win10)</ss> <ss type=\"outlook\">(outlook)</ss> <ss type=\"access\">(access)</ss> <ss type=\"bing\">(bing)</ss> <ss type=\"excel\">(excel)</ss> <ss type=\"internetexplorer\">(internetexplorer)</ss> <ss type=\"microsoft\">(microsoft)</ss> <ss type=\"onenote\">(onenote)</ss> <ss type=\"onedrive\">(onedrive)</ss> <ss type=\"powerpoint\">(powerpoint)</ss> <ss type=\"publisher\">(publisher)</ss> <ss type=\"sharepoint\">(sharepoint)</ss> <ss type=\"skypebiz\">(skypebiz)</ss> <ss type=\"word\">(word)</ss> <ss type=\"xbox\">(xbox)</ss> <ss type=\"wtf\">(wtf)</ss> <ss type=\"finger\">(finger)</ss> <ss type=\"ghost\">(ghost)</ss> <ss type=\"vampire\">(vampire)</ss> <ss type=\"skull\">(skull)</ss> <ss type=\"pumpkin\">(pumpkin)</ss> <ss type=\"ladyvampire\">(ladyvamp)</ss> <ss type=\"abe\">(abe)</ss> <ss type=\"golmaal\">(golmaal)</ss> <ss type=\"oye\">(oye)</ss> <ss type=\"poop\">(poop)</ss> <ss type=\"kya\">(kya)</ss> <ss type=\"ontheloo\">(ontheloo)</ss> <ss type=\"neil\">(neil)</ss> <ss type=\"santamooning\">(santamooning)</ss> <ss type=\"santa\">(santa)</ss> <ss type=\"xmastree\">(xmastree)</ss> <ss type=\"champagne\">(champagne)</ss> <ss type=\"reindeer\">(reindeer)</ss> <ss type=\"snowangel\">(snowangel)</ss> <ss type=\"polarbear\">(polarbear)</ss> <ss type=\"penguin\">(penguin)</ss> <ss type=\"gift\">(gift)</ss> <ss type=\"holidayspirit\">(holidayspirit)</ss> <ss type=\"festiveparty\">(festiveparty)</ss> <ss type=\"hungover\">(morningafter)</ss> <ss type=\"shivering\">(shivering)</ss>");
        /*else*/ request.reply("Testing, 1 2 3");

        /*request.reply(new Message(request.getClient(), "ok") {
            @Override
            public JSONObject toJson() {
                return JSONBuilder.create(this)
                        .withValue("testing", 123)
                        .withNewObject("info")
                        .withValue("ok", "lol ok")
                        .withValue("no", "but the coco???")
                        .build()
                        .withValue("do you like waffles", "yes")
                        .build();
            }
        }.toString());*/
    }
}