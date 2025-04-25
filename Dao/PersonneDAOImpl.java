package Dao;
import modele.Personne;
import modele.Cryptage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Vue.*;

import javax.swing.*;

public class PersonneDAOImpl implements PersonneDAO {
    private DaoFactory daoFactory;
    private message_erreur message;
    // constructeur dépendant de la classe DaoFactory
    public PersonneDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        message = new message_erreur();
    }


    @Override
    /*
        Ajouter un client à la table
    */
    public void ajouter(Personne client){
        Cryptage cle = new Cryptage();
        String nom, mail, mdp,adresse_postal;
        boolean Identifiant_admin;

        try {
            // connexion
            Connection connexion = daoFactory.getConnection();

            // récupération du nom et prix de l'objet product en paramètre
            nom = client.getNom_client();
            mail = client.getMail_client();
            mdp = client.getMdp_client();
            adresse_postal= client.getAdresse_postale_client();
            Identifiant_admin= client.getIdentifiant_admin();

            mdp=cle.CryptageMotDePasse(mdp);
            // Exécution de la requête INSERT INTO de l'objet product en paramètre
            PreparedStatement preparedStatement = connexion.prepareStatement(
                    "INSERT INTO `personnes` (`nom_client`, `mail_client`, `mdp_client`, `adresse_postale_client`, `identifiant_admin`) " +
                            "VALUES (?, ?, ?, ?, ?);"
            );

            // Remplacement des "?" par les vraies valeurs
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, mail);
            preparedStatement.setString(3, mdp);
            preparedStatement.setString(4, adresse_postal);
            preparedStatement.setBoolean(5, Identifiant_admin);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            message.message_pb_connection();
        }
    }

    @Override
    /*
        Chercher un client à la table avec l'adresse mail
    */
    public Personne chercher_adresse_mail(String adresse_mail) {
        Personne client = null;

        // Requête SQL sécurisée avec un paramètre
        String sql = "SELECT * FROM personnes WHERE mail_client = ?";

        try (Connection connexion = daoFactory.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {

            // Paramètre sécurisé pour éviter les injections SQL
            statement.setString(1, adresse_mail);
            ResultSet resultats = statement.executeQuery();

            // Vérifier si un résultat est trouvé
            if (resultats.next()) {
                int clientId = resultats.getInt("id_personne");
                String clientNom = resultats.getString("nom_client");
                String clientMail = resultats.getString("mail_client");
                String clientMdp = resultats.getString("mdp_client");
                String clientAdresse_postal = resultats.getString("adresse_postale_client");
                boolean identifiantAdmin = resultats.getBoolean("identifiant_admin");

                // Création de l'objet Personne
                client = new Personne(clientId, clientNom, clientMail, clientMdp, clientAdresse_postal, identifiantAdmin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            message.message_pb_connection();
        }

        return client;
    }

    @Override
    /*
        Chercher un client à la table avec l'adresse mail
    */
    public Personne chercher_id(int id)  {
        Personne client = null;
        int clientId, compteur_secu=0;
        String clientNom,clientMail,clientMdp,clientAdresse_postal;
        boolean Identifiant_admin;
        try
        {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // Exécution de la requête SELECT pour récupérer le client de l'id dans la base de données
            ResultSet resultats = statement.executeQuery("select * from personnes where ID_personne=" + id);

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next() && compteur_secu < 100000000)
            {
                // récupérer les 6 champs de la table personnes dans la base de données
                clientId = resultats.getInt(1);
                clientNom = resultats.getString(2);
                clientMail = resultats.getString(3);
                clientMdp = resultats.getString(4);
                clientAdresse_postal = resultats.getString(5);
                Identifiant_admin = resultats.getBoolean(6);

                // Si l'id du client est trouvé, l'instancier et sortir de la boucle
                if (id==clientId)
                {
                    // instancier un objet de Produit avec ces 3 champs en paramètres
                    client = new Personne(clientId, clientNom, clientMail, clientMdp, clientAdresse_postal, Identifiant_admin);
                    break;
                }
                else
                {
                    compteur_secu++;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            message.message_pb_connection();
        }

        return client;
    }


    @Override
    /*
       supprime un client dans la table
   */
    public void supprimer (Personne client) {
        Scanner scanner = new Scanner(System.in);
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête SELECT pour récupérer le produit de l'id dans la base de données
            ResultSet resultats = statement.executeQuery("select * from personnes where ID_personne="+client.getID_personne());

            if (resultats.next()) {
                // Si le produit existe, on demande la confirmation avant de le supprimer
                pop_up_interrup message= new pop_up_interrup();
                int retour=message.ConfirmationSuppression(client.getNom_client());
                if (retour==0) {
                    // Requête SQL pour supprimer le produit
                    String deleteSQL = "DELETE FROM personnes WHERE ID_personne= ?";

                    // Créer un PreparedStatement pour l'exécution de la requête
                    try (PreparedStatement preparedStatement = connexion.prepareStatement(deleteSQL)) {
                        preparedStatement.setInt(1, client.getID_personne());  // Remplacer le ? par l'ID du produit à supprimer

                        // Exécuter la suppression
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            message.message_confirmation_personne(retour);
                        } else {
                            message.message_confirmation_personne(2);
                        }
                    }
                } else if (retour==1) {
                    message.message_confirmation_personne(retour);
                }
                else {
                    message.message_confirmation_personne(retour);
                }
            } else {
                message.message_entite_inconnue();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            message.message_pb_connection();
        }
        scanner.close();
    }

    @Override
    /*
       modifie le numero admin client
   */

    public int modifier_admin_nombre(int id, int nouveauRole){
        String update = "UPDATE personnes SET identifiant_admin = ? WHERE id_personne = ?";
        try
        {
            // connexion
            Connection connexion = daoFactory.getConnection();
            PreparedStatement updateStmt = connexion.prepareStatement(update);
            updateStmt.setInt(1, nouveauRole);
            updateStmt.setInt(2, id);
            int updated = updateStmt.executeUpdate();
            return updated;


        }
        catch (SQLException e) {
            e.printStackTrace();
            message.message_pb_connection();
        }
        return -1;
    }


    public boolean mettreAJourClient(int id, String nom, String email, String adresse, String nouveauMdp) {
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
            message.message_pb_connection();
            return false;
        }
    }
}
