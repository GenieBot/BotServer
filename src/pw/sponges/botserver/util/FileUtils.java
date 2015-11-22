package pw.sponges.botserver.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtils {

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

    public static String readUrl(URL url) {
        return readUrl(false, url);
    }

    public static String readUrl(boolean debug, URL url) {
        HttpURLConnection con;

        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (debug) System.out.println(con.getResponseCode());
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

        return str.toString();
    }

}
