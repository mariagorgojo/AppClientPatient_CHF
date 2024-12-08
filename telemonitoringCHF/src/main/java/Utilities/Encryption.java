/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author maria
 */
public class Encryption {
    
    // MD5 has a hash representationn, a fixed-length hexadecimal string
    
    public static String encryptPasswordMD5(String password) {
        try {           
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Password string is converted into a byte array
            // digest() computes the MD5 hash of the byte array
            byte[] hashBytes = md.digest(password.getBytes());

            // Each byte of the hash is converted to a 2-character
            // hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña: " + e.getMessage());
        }
    }
    
    
}
