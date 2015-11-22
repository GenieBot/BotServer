package pw.sponges.botserver.internal;

import pw.sponges.botserver.Bot;
import pw.sponges.botserver.util.Msg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015 Joe Burnard ("SpongyBacon").
 * By viewing this code, you agree to the terms
 * in the enclosed license.txt file.
 */
public class Server {

    private Bot bot;
    private ServerSocket socket;

    public static List<ServerThread> threads;

    public static boolean ACCEPTING = true;

    public Server(Bot bot) {
        this.bot = bot;
        threads = new ArrayList<>();
    }

    public void start() {
        try {
            socket = new ServerSocket(9090);

            while (ACCEPTING) {
                if (!ACCEPTING) break;

                ServerThread thread;
                Socket socket;

                try {
                    if (this.socket.isClosed()) {
                        Msg.warning("The ServerSocket is closed? (Server.java)");
                        return;
                    }

                    socket = this.socket.accept();
                } catch (SocketException e) {
                    e.printStackTrace();
                    return;
                }

                thread = new ServerThread(bot, this, socket);
                thread.start();

                threads.add(thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String msg) {
        for (ServerThread thread : threads) {
            thread.print(msg);
        }
    }

    public void remove(ServerThread thread) {
        threads.remove(thread);
    }

    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}