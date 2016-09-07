package io.sponges.bot.server;

import io.sponges.bot.api.Logger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerImpl implements Logger {

    private static final String FORMAT = "[%s - %s] %s\r\n";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private boolean debug = false;

    @Override
    public void log(Type type, String message) {
        if (type == Type.DEBUG && !debug) return;
        PrintStream stream;
        if (type == Type.WARNING) {
            stream = System.err;
        } else {
            stream = System.out;
        }
        stream.printf(FORMAT, TIME_FORMAT.format(new Date()), type.name(), message);
    }

    @Override
    public void log(String type, String message) {
        System.out.printf(FORMAT, TIME_FORMAT.format(new Date()), type, message);
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
