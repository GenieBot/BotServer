package io.sponges.botserver.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Strawpoll {

    private final String title;
    private final String[] options;
    private final boolean multi, captcha;
    private final String duplicateCheck;

    private int id = 0;

    public Strawpoll(String title, String[] options, boolean multi, boolean captcha, String duplicateCheck) {
        this.title = title;
        this.options = options;
        this.multi = multi;
        this.captcha = captcha;
        this.duplicateCheck = duplicateCheck;

        JSONObject json = new JSONObject()
                .put("title", StringEscapeUtils.unescapeJson(title))
                .put("options", options)
                .put("multi", multi)
                .put("captcha", captcha)
                .put("dupcheck", duplicateCheck);

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("http://strawpoll.me/api/v2/polls").openConnection();
            con.setRequestMethod("POST");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            int code = con.getResponseCode();

            if (code != 201) {
                System.out.println("Creating poll returned response code " + code + " instead of expected 201?");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        StringBuilder response = new StringBuilder();

        String input;
        try {
            while ((input = reader.readLine()) != null) {
                response.append(input).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        con.disconnect();

        this.id = new JSONObject(response.toString()).getInt("id");
    }

    public String getTitle() {
        return title;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isMulti() {
        return multi;
    }

    public boolean isCaptcha() {
        return captcha;
    }

    public String getDuplicateCheck() {
        return duplicateCheck;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return "http://strawpoll.me/" + id;
    }

}
