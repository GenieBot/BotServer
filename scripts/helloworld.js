function onEnable() {
	var Command = Java.type("io.sponges.bot.api.cmd.Command");
	var cmd = new (Java.extend(Command, function (request, args) {
	      var user = request.getUser();
	      if (!user.hasPermission("moderation.clearchat")) {
	        request.reply("No permission.");
	        return;
	      }
	      request.getChannel().sendChatMessage("");
	})) ("clears the chat", ["clearchat", "chatclear"]);
	module.getCommandManager().registerCommand(module, cmd);
}

function onDisable() {
}