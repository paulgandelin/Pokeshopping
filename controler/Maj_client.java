package controler;

import Dao.DaoFactory;
import modele.Cryptage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Maj_client {

    public boolean mettreAJourClient(int id, String nom, String email, String adresse, String nouveauMdp, DaoFactory daoFactory) {
        String updateSQL;
        boolean modifierMdp = (nouveauMdp != null && !nouveauMdp.isEmpty());
        Cryptage cryptage = new Cryptage();
        String nouveauMdpCrypte = modifierMdp ? cryptage.CryptageMotDePasse(nouveauMdp) : null;

        if (modifierMdp) {
            updateSQL = "UPDATE personnes SET nom_client = ?, mail_client = ?, adresse_postale_client = ?, mdp_client = ? WHERE id_personne = ?";
        } else {
            updateSQL = "UPDATE personnes SET nom_client = ?, mail_client = ?, adresse_postale_client = ? WHERE id_personne = ?";
        }

        try (Connection connexion = daoFactory.getConnection()) {
            PreparedStatement preparedStatement = connexion.prepareStatement(updateSQL);
            connexion.setAutoCommit(false);

            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, adresse);

            if (modifierMdp) {
                preparedStatement.setString(4, nouveauMdpCrypte);
                preparedStatement.setInt(5, id);
            } else {
                preparedStatement.setInt(4, id);
            }

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connexion.commit();
                return true;
            } else {
                connexion.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                Connection connexion = daoFactory.getConnection();
                connexion.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour en base de données.");
            return false;
        }
    }
}
