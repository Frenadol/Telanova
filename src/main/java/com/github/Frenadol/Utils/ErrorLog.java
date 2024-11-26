package com.github.Frenadol.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLog {

    private static final String LOG_FILE = "error_log.txt";

    /**
     * Registra un mensaje de error en el archivo de registro.
     *
     * @param message Mensaje que describe el error.
     */
    public static void logMessage(String message) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String timestamp = getCurrentTimestamp();
            printWriter.println("[" + timestamp + "] " + message);

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }
    }

    /**
     * Registra el stack trace de una excepción en el archivo de registro.
     *
     * @param exception Excepción a registrar.
     */
    public static void fileRead(Exception exception) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String timestamp = getCurrentTimestamp();
            printWriter.println("[" + timestamp + "] " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
            exception.printStackTrace(printWriter);

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }
    }

    /**
     * Obtiene la marca de tiempo actual en un formato legible.
     *
     * @return Marca de tiempo como String.
     */
    private static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
