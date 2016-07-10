function onEnable() {
	var UserJoinEvent = Java.type("io.sponges.bot.api.event.events.user.UserJoinEvent");
	module.getEventManager().register(module, UserJoinEvent.class, function(e) {
		var network = e.getNetwork();
		var user = e.getUser();
		if (network.getId().equals("19:6d890b9196fb445cae3fae0dc1510799@thread.skype") && user.getId().startsWith("guest:")) {
			network.kickUser(user);
		}
	});
}

function onDisable() {
	print("Disabled as fuck lol");
}
//19:7a2725ecbea140638d4eb17f0ea85819@thread.skype - spongybot chat
//19:6d890b9196fb445cae3fae0dc1510799@thread.skype - yive chat

