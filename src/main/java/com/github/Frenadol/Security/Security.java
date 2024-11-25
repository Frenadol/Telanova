package com.github.Frenadol.Security;

import com.github.Frenadol.Utils.ErrorLog;
import javafx.scene.control.Alert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    /**
     * Converts a byte array to a hexadecimal string.
     * @param hash The byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    /**
     * Hashes a password using the SHA-3 cryptographic hash function.
     * @param password The password to hash.
     * @return The hashed password as a hexadecimal string.
     * @throws NoSuchAlgorithmException if the specified algorithm is not available.
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        String sha3Hex = bytesToHex(hashbytes);
        return sha3Hex;
    }
    public boolean checkPassword(String enteredPassword, String storedPassword) {
        try {
            String hashedEnteredPassword = Security.hashPassword(enteredPassword);
            return hashedEnteredPassword.equals(storedPassword);
        } catch (NoSuchAlgorithmException e) {
            String message = "Error al hashear la contraseña.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return false;
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
