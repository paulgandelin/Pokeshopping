package Dao;
import modele.Personne;
import modele.Cryptage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import Vue.*;

public class PersonneDAOImpl implements PersonneDAO {
    private DaoFactory daoFactory;
    private message_erreur messsage;
    // constructeur dépendant de la classe DaoFactory
    public PersonneDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        messsage = new message_erreur();
    }

    @Override
    /*
        Récupérer la liste des clients de la base de données dans listeClients
    */
    public ArrayList<Personne> getAll() {
        ArrayList<Personne> listeClients = new ArrayList<Personne>();
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des produits de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from personnes");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int clientId = resultats.getInt(1);
                String clientNom = resultats.getString(2);
                String clientMail = resultats.getString(3);
                String clientMdp = resultats.getString(4);
                String clientAddress = resultats.getString(5);
                int identifiant_admin=resultats.getInt(6);

                // instancier un objet de Produit avec ces 3 champs en paramètres
                Personne client = new Personne(clientId,clientNom,clientMail,clientMdp,clientAddress, identifiant_admin);

                // ajouter ce produit à listeProduits
                listeClients.add(client);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de clients impossible");
        }
        return listeClients;
    }
    @Override
    /*
        Ajouter un client à la table
    */
    public void ajouter(Personne client){
        Cryptage cle = new Cryptage();
        String nom, mail, mdp,adresse_postal;
        int Identifiant_admin;

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
            preparedStatement.setInt(5, Identifiant_admin);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
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
                int identifiantAdmin = resultats.getInt("identifiant_admin");

                // Création de l'objet Personne
                client = new Personne(clientId, clientNom, clientMail, clientMdp, clientAdresse_postal, identifiantAdmin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }

        return client;
    }

    @Override
    /*
        Chercher un client à la table avec l'adresse mail
    */
    public Personne chercher_id(int id)  {
        Personne client = null;
        int clientId,Identifiant_admin, compteur_secu=0;
        String clientNom,clientMail,clientMdp,clientAdresse_postal;
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
                Identifiant_admin = resultats.getInt(6);

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
            messsage.message_pb_connection();
        }

        return client;
    }
    /*
        modifie un client dans la table
    */
    public Personne modifier(Personne client)
    {
        Scanner scanner = new Scanner(System.in);
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // Exécution de la requête SELECT pour récupérer le produit de l'id dans la base de données
            ResultSet resultats = statement.executeQuery("select * from clients where clientID="+client.getID_personne());
            if (resultats.next()) {
                // Si le client existe -> on modifie ses attributs
                System.out.println("Donnée un nouveau nom client");
                String new_nom = scanner.nextLine();  // Récupérer le nouveau nom du produit
                System.out.println("Donnée un nouveau mail client");
                String new_mail = scanner.nextLine();  // Récupérer le nouveau prix

                // Mettre à jour le client dans la base de données
                String updateSQL = "UPDATE clients SET clientNom = ?, clientMail = ? WHERE clientID = ?";

                // Créer un PreparedStatement pour l'exécution de la requête
                try (PreparedStatement preparedStatement = connexion.prepareStatement(updateSQL)) {
                    preparedStatement.setString(1, new_nom);  // Remplacer le ? par le nouveau nom
                    preparedStatement.setString(2, new_mail);  // Remplacer le ? par le nouveau prix
                    preparedStatement.setInt(3, client.getID_personne());  // Remplacer le ? par l'ID du produit à modifier

                    // Exécuter la mise à jour
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        client.setNom_client(new_nom);
                        client.setMail_client(new_mail);
                        System.out.println("CLient mis à jour avec succès!");
                    } else {
                        System.out.println("Aucun client trouvé avec l'ID " + client.getID_personne());
                    }
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Client non trouvé dans la base de données");
        }
        scanner.close();
        return client;
    }// à retravailler avant pour avoir une idée des fonctionnalité à mettre

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
                            message.message_confirmation(retour);
                        } else {
                            message.message_confirmation(2);
                        }
                    }
                } else if (retour==1) {
                    message.message_confirmation(retour);
                }
                else {
                    message.message_confirmation(retour);
                }
            } else {
                messsage.message_entite_inconnue();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
        scanner.close();
    }
}
