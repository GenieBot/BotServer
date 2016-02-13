package io.sponges.bot.server.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Util class for File I/O
 */
public final class FileUtils {

    /**
     * Reads the file contents as a String
     * @param file the file to read
     * @return file contents
     */
    public static String readFile(File file) {
        StringBuilder str = new StringBuilder();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(file));
            String input;
            while ((input = in.readLine()) != null) {
                str.append(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return str.toString();
    }

    /**
     * Writes the specified contents to a File
     * @param file the file to write to
     * @param contents the contents to write to the file
     */
    public static void writeFile(File file, String contents) {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(file));
            out.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read the contents of a URL
     * Assumes debug mode is false
     * @param url the url to read from
     * @return contents of the url
     */
    public static String readUrl(URL url) {
        return readUrl(false, url);
    }

    /**
     * Read the contents of a URL with an option for debug mode
     * @param debug print debug messages?
     * @param url the url to read from
     * @return contents of the URL
     */
    public static String readUrl(boolean debug, URL url) {
        HttpURLConnection con;

        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (debug) Msg.debug("Got response code " + con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder str = new StringBuilder();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                str.append(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            con.disconnect();
        }

        if (debug) Msg.debug("Data: " + str.toString());

        return str.toString();
    }

}
