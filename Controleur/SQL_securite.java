package Controleur;
import Dao.DaoFactory;
import modele.Cryptage;
import Vue.message_erreur;
import java.util.regex.Pattern;
import java.sql.*;

import static java.lang.Boolean.TRUE;


public class SQL_securite {
    protected static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    protected Cryptage cryptage=new Cryptage();
    private message_erreur mesage_e = new message_erreur();

    public static boolean valid_mail(String email) {
        return Pattern.matches(EMAIL_REGEX, email); // retourne 1 si invalide 0 si valide
    }

    public boolean email_dans_BD(String email, DaoFactory daoFactory) {
        boolean exists = true;
        String query = "SELECT COUNT(*) FROM personnes WHERE mail_client = ?";
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            PreparedStatement pstmt = connexion.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                exists = false;
            }
            else {
                exists = true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            mesage_e.message_pb_connection();
        }
        return exists;
    }

    public boolean connexion_compte_securite(DaoFactory daoFactory, String email, String mdp) {
        String buffer_mdp = cryptage.CryptageMotDePasse(mdp);
        String query = "SELECT COUNT(*) FROM personnes WHERE mail_client = ? AND mdp_client = ?";

        System.out.println(buffer_mdp);
        System.out.println(email);

        if (valid_mail(email)) {
            try {
                Connection connexion = daoFactory.getConnection();
                PreparedStatement pstmt = connexion.prepareStatement(query);
                pstmt.setString(1, email);
                pstmt.setString(2, buffer_mdp);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);  // récupère le COUNT(*)
                    return count > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                mesage_e.message_pb_connection();
            }
        }

        return false;
    }

    public boolean modification_mdp(DaoFactory daoFactory, String mdp) {
        String buffer_mdp;
        buffer_mdp = cryptage.CryptageMotDePasse(mdp);
        String query = "SELECT COUNT(*) FROM personnes WHERE mdp_client = ?";

        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            PreparedStatement pstmt = connexion.prepareStatement(query);
            pstmt.setString(1, buffer_mdp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mesage_e.message_pb_connection();
        }
        return false;
    }

}