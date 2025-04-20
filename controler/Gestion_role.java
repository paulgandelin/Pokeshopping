package controler;

import Dao.DaoFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Gestion_role extends JFrame {

    public void ouvrirFenetreGestionRoles(DaoFactory daoFactory) {

        String emailCible = JOptionPane.showInputDialog(this, "Entrez l'adresse mail de l'utilisateur à modifier :");

        if (emailCible == null || emailCible.trim().isEmpty()) {
            return;
        }

        try {
            Connection connexion = daoFactory.getConnection();
            String sql = "SELECT id_personne, nom_client, identifiant_admin FROM personnes WHERE mail_client = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, emailCible);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_personne");
                String nom = rs.getString("nom_client");
                boolean estAdmin = rs.getInt("identifiant_admin") == 1;

                String[] options = estAdmin ?
                        new String[]{"Rétrograder en utilisateur", "Annuler"} :
                        new String[]{"Promouvoir en admin", "Annuler"};

                int choix = JOptionPane.showOptionDialog(this,
                        "Utilisateur trouvé : " + nom + "\nEmail : " + emailCible + "\nActuellement : " + (estAdmin ? "Admin" : "Utilisateur"),
                        "Gérer le rôle",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choix == 0) {
                    int nouveauRole = estAdmin ? 0 : 1;
                    String update = "UPDATE personnes SET identifiant_admin = ? WHERE id_personne = ?";
                    PreparedStatement updateStmt = connexion.prepareStatement(update);
                    updateStmt.setInt(1, nouveauRole);
                    updateStmt.setInt(2, id);

                    int updated = updateStmt.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, "Le rôle a bien été mis à jour !");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du rôle.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Aucun utilisateur trouvé avec cet email.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur SQL lors de la recherche.");
        }
    }

}
