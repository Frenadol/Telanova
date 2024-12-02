package com.github.Frenadol.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLog {

    private static final String LOG_FILE = "error_log.txt";

    /**
     * Logs an error message to the log file.
     *
     * @param message Message describing the error.
     */
    public static void logMessage(String message) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String timestamp = getCurrentTimestamp();
            printWriter.println("[" + timestamp + "] " + message);

        } catch (IOException e) {
            System.err.println("Error writing to the log file: " + e.getMessage());
        }
    }

    /**
     * Logs the stack trace of an exception to the log file.
     *
     * @param exception Exception to log.
     */
    public static void fileRead(Exception exception) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String timestamp = getCurrentTimestamp();
            printWriter.println("[" + timestamp + "] " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
            exception.printStackTrace(printWriter);

        } catch (IOException e) {
            System.err.println("Error writing to the log file: " + e.getMessage());
        }
    }

    /**
     * Gets the current timestamp in a readable format.
     *
     * @return Timestamp as a String.
     */
    private static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}