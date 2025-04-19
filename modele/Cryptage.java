package modele;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptage {

    public Cryptage() {
    }
    public String CryptageMotDePasse(String mdp)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(mdp.getBytes());

            // Convertir en format hexadécimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }

}
