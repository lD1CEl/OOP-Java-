package ru.nsu.ccfit.factory.utils;

public class Logger {
    private final boolean enabled;
    private java.io.PrintWriter writer;
    private final java.text.SimpleDateFormat dateFormat;

    public Logger(boolean enabled) {
        this.enabled = enabled;
        this.dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        if (enabled) {
            try {
                this.writer = new java.io.PrintWriter(new java.io.FileWriter("factory_sales.log", true));
            } catch (java.io.IOException e) {
                System.err.println("ошибка при создании лог-файла: " + e.getMessage());
            }
        }
    }

    public synchronized void log(String message) {
        if (!enabled || writer == null) return;
        
        String time = dateFormat.format(new java.util.Date());
        String fullMessage = time + ": " + message;
        
        writer.println(fullMessage);
        writer.flush();
    }

    public synchronized void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
