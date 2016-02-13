package io.sponges.bot.server.cmd.commands.mc;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class MinecraftStatusCommand extends Command {

    public MinecraftStatusCommand() {
        super("command.mcstatus", UserRole.USER, "returns the status of mojang servers", "mcstatus", "minecraftstatus", "sessionservers", "sessions", "mcsessions", "mojangsessions", "mojangstatus");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        String user = request.getUser().getId();

        request.reply("Mojang server statuses:\n" +
            "Minecraft Website - " + getStatus("minecraft.net", user) + "\n" +
            "Minecraft Session Service - " + getStatus("session.minecraft.net", user) + "\n" +
            "Mojang Account Service - " + getStatus("account.mojang.com", user) + "\n" +
            "Mojang Login Service - " + getStatus("auth.mojang.com", user) + "\n" +
            "Minecraft Skin Service - " + getStatus("skins.minecraft.net", user) + "\n" +
            "Mojang Session Service - " + getStatus("sessionserver.mojang.com", user) + "\n" +
            "Mojang API Service - " + getStatus("api.mojang.com", user) + "\n" +
            "Minecraft Texture Service - " + getStatus("textures.minecraft.net", user));
    }

    private String getStatus(String url, String user) {
        String result;

        try {
            result = Jsoup.connect("http://status.mojang.com/check?service=" + url).ignoreContentType(true).header("X - Request - By - Username", user).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return "Something went wrong :L";
        }

        JSONObject jso;
        try {
            jso = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong man :(";
        }

        return jso.getString(url);
    }
}
