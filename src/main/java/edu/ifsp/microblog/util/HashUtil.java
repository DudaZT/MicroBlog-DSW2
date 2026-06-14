package edu.ifsp.microblog.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// senhas armazenadas com SHA-256.
public class HashUtil {

    private HashUtil() {}

    
    // Calcula o SHA-256 de uma string (hash de senha).
    public static String sha256(String input) {
        return hash("SHA-256", input.getBytes());
    }


    // Calcula o MD5 de um InputStream (usado para deduplicação de uploads).
    public static String md5(InputStream input) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int read;
            
            while ((read = input.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            
            return bytesToHex(md.digest());
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 não disponível", e);
        }
    }

    private static String hash(String algorithm, byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            
            return bytesToHex(md.digest(data));
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + " não disponível", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        
        for (byte b : bytes) sb.append(String.format("%02x", b));
        
        return sb.toString();
    }
}