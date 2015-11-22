package pw.sponges.botserver.internal;

import pw.sponges.botserver.Bot;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.event.events.ClientInputEvent;
import pw.sponges.botserver.util.Msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Copyright (c) 2015 Joe Burnard ("SpongyBacon").
 * By viewing this code, you agree to the terms
 * in the enclosed license.txt file.
 */
public class ServerThread extends Thread implements Runnable {

    private Bot bot;
    private Server server;
    private Socket socket;

    private PrintWriter out = null;
    private BufferedReader in = null;

    private Client client = null;

    public ServerThread(Bot bot, Server server, Socket socket) {
        super("ServerThread");

        this.bot = bot;
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Msg.log("Client connected.");
            out.println("Connected to Server.");

            try {
                String input;
                while ((input = in.readLine()) != null) {
                    processInput(input);
                }
            } catch (IOException e) {
                if (e.getMessage().equalsIgnoreCase("Stream closed")) {
                    Msg.warning("Stream is closed!");

                    try {
                        out.close();
                        in.close();
                    } catch (IOException e1) {
                        Msg.warning("Logging just for fun tbh! (Bot.java)");
                        e1.printStackTrace();
                    }

                    bot.getClients().remove(client.getId());
                    server.remove(this);
                    Msg.debug("Client inputs/outputs closed & thread removed from list. (2)");
                    return;
                } else {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            if (e.getMessage().equalsIgnoreCase("Connection reset")) {
                onDisconnect();
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();

            bot.getClients().remove(client.getId());
            server.remove(this);
            Msg.debug("Client inputs/outputs closed & thread removed from list. (3)");
        }
    }

    private void processInput(String input) {
        bot.getEventManager().handle(new ClientInputEvent(input, this));
    }

    public void print(String msg) {
        out.println(msg);
    }

    private void onDisconnect() {
        Msg.log("Client disconnected.");
        out.close();
        try {
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        bot.getClients().remove(client.getId());
        server.remove(this);
        Msg.debug("Client inputs/outputs closed & thread removed from list.");
    }

    public void stopThread() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
