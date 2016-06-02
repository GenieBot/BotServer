function onEnable() {
	print("oo man another script");
	var Command = Java.type("io.sponges.bot.api.cmd.Command");
	var cmd = new (Java.extend(Command, function (request, args) {
	      request.reply("Testing da script lololXD!");
	})) ("this is a description", ["testscript14"]);
	module.getCommandManager().registerCommand(module, cmd);
	print("Registered the command for anotherScript ;o");
}

function onDisable() {
	print("Disabled AnothrSropt as fuck lol");
}

