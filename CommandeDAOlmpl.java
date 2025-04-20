package Dao;
import modele.Commande;
 
import java.sql.*;
import java.util.ArrayList;

import Vue.*;

public class CommandeDAOImpl implements CommandeDAO {
    private DaoFactory daoFactory;
    private message_erreur messsage;
    // constructeur dépendant de la classe DaoFactory
    public CommandeDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        messsage = new message_erreur();
    }

    @Override
    /*
        Récupérer la liste des clients de la base de données dans listeClients
    */
    public ArrayList<Commande> getAll() {
        ArrayList<Commande> listeClients = new ArrayList<Commande>();
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des produits de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from commandes");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int commandeID = resultats.getInt(1);
                int clientID = resultats.getInt(2);
                int produitID = resultats.getInt(3);
                String adresse = resultats.getString(4);
                int quantite = resultats.getInt(5);
                boolean payer = resultats.getBoolean(6);
                int note = resultats.getInt(7);
                // instancier un objet de Client2b2 avec ces 3 champs en paramètres
                Commande achat = new Commande(commandeID, produitID, clientID, adresse, quantite,payer,note);

                // ajouter ce produit à listeProduits
                listeClients.add(achat);
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
    public void ajouter(Commande achat){
        String adresse_postal;
        int IDclient, IDproduit, quantite;
        boolean payer;

        try {
            // connexion
            Connection connexion = daoFactory.getConnection();

            // récupération du nom et prix de l'objet product en paramètre
            IDclient = achat.getIdClient();
            IDproduit = achat.getIdProduit();
            quantite = achat.getQuantite();
            adresse_postal= achat.getLieuLivraison();
            payer= achat.getPayer();
            // Exécution de la requête INSERT INTO de l'objet product en paramètre
            PreparedStatement preparedStatement = connexion.prepareStatement(
                    "INSERT INTO commandes (ID_produit, ID_client, Adresse, quantite, payer, note) VALUES (?, ?, ?, ?, ?, ?);"

            );

// Remplacement des "?" par les vraies valeurs
            preparedStatement.setInt(1, IDproduit);
            preparedStatement.setInt(2, IDclient);
            preparedStatement.setString(3, adresse_postal);
            preparedStatement.setInt(4, quantite);
            preparedStatement.setBoolean(5, payer);
            preparedStatement.setInt(6, 5);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
    }


    @Override
    /*
        Chercher un client à la table avec l'id produit
    */

    public ArrayList<Commande> chercherCommandesParProduit(int idProduit) {
        ArrayList<Commande> commandes = new ArrayList<>();

        String requete = "SELECT * FROM commandes WHERE ID_produit = ?";

        try (
                Connection connexion = daoFactory.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(requete)
        ) {
            stmt.setInt(1, idProduit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int IDcommande = rs.getInt("ID_commande");
                int IDclient = rs.getInt("ID_client");
                String adresse_postale = rs.getString("Adresse");
                int quantite = rs.getInt("quantite");
                boolean payer = rs.getBoolean("payer");
                int note = rs.getInt("note");

                Commande commande = new Commande(IDcommande, idProduit, IDclient, adresse_postale, quantite, payer, note);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection(); // toujours ce messsage à 3 s ?
        }

        return commandes;
    }

    /*
        Chercher un client à la table avec l'id client (payer=true)
    */
    public ArrayList<Commande> chercherCommandesPayeesParClient(int idClient) {
        ArrayList<Commande> commandesPayees = new ArrayList<>();

        String requete = "SELECT * FROM commandes WHERE ID_client = ? AND payer = true";

        try (
                Connection connexion = daoFactory.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(requete)
        ) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int IDcommande = rs.getInt("ID_commande");
                int IDproduit = rs.getInt("ID_produit");
                int IDclient = rs.getInt("ID_client");
                String adresse_postale = rs.getString("Adresse");
                int quantite = rs.getInt("quantite");
                boolean payer = rs.getBoolean("payer");
                int note =rs.getInt("note");

                Commande commande = new Commande(IDcommande, IDproduit, IDclient, adresse_postale, quantite, payer, note);
                commandesPayees.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection(); // toujours avec les 3 "s" dans "messsage" ?
        }

        return commandesPayees;
    }

    @Override
    /*
        Chercher un client à la table avec l'id client (payer=false)
    */
    public ArrayList<Commande> chercherCommandesNonPayeesParClient(int idClient) {
        ArrayList<Commande> commandesNonPayees = new ArrayList<>();

        String requete = "SELECT * FROM commandes WHERE ID_client = ? AND payer = false";

        try (
                Connection connexion = daoFactory.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(requete)
        ) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int IDcommande = rs.getInt("ID_commande");
                int IDproduit = rs.getInt("ID_produit");
                int IDclient = rs.getInt("ID_client");
                String adresse_postale = rs.getString("Adresse");
                int quantite = rs.getInt("quantite");
                boolean payer = rs.getBoolean("payer");
                int note= rs.getInt("note");

                Commande commande = new Commande(IDcommande, IDproduit, IDclient, adresse_postale, quantite, payer,note);
                commandesNonPayees.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection(); // <- tu as une classe messsage apparemment ? (avec 3 's')
        }

        return commandesNonPayees;
    }

    public ArrayList<Commande> chercherCommandesPayees() {
        ArrayList<Commande> commandesPayees = new ArrayList<>();

        String requete = "SELECT * FROM commandes WHERE payer = true";

        try (
                Connection connexion = daoFactory.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(requete)
        ) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int IDcommande = rs.getInt("ID_commande");
                int IDproduit = rs.getInt("ID_produit");
                int IDclient = rs.getInt("ID_client");
                String adresse_postale = rs.getString("Adresse");
                int quantite = rs.getInt("quantite");
                boolean payer = rs.getBoolean("payer");
                int note = rs.getInt("note");
                Commande commande = new Commande(IDcommande, IDproduit, IDclient, adresse_postale, quantite, payer,note);
                commandesPayees.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }

        return commandesPayees;
    }


    /*
        Chercher un client à la table avec l'id commande
    */
    public Commande chercher_id_commande(int id)  {
        Commande client = null;
        int IDcommande, IDclient, IDproduit, quantite, compteur_secu=0, note;
        String adresse_postal;
        boolean payer;
        try
        {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // Exécution de la requête SELECT pour récupérer le client de l'id dans la base de données
            ResultSet resultats = statement.executeQuery("select * from commandes where ID_commande=" + id);

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next() && compteur_secu < 100000000)
            {
                // récupérer les 6 champs de la table personnes dans la base de données
                IDcommande = resultats.getInt(1);
                IDproduit = resultats.getInt(2);
                IDclient = resultats.getInt(3);
                adresse_postal = resultats.getString(4);
                quantite = resultats.getInt(5);
                payer = resultats.getBoolean(6);
                note = resultats.getInt(7);

                // Si l'id du client est trouvé, l'instancier et sortir de la boucle
                if (id==IDcommande)
                {
                    // instancier un objet de Produit avec ces 3 champs en paramètres
                    client = new Commande(IDcommande, IDproduit, IDclient, adresse_postal, quantite,payer,note);
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

    @Override
    /*
       supprime un client dans la table
   */
    public void supprimer(Commande achat) {
        try {
            Connection connexion = daoFactory.getConnection();

            // Vérifier si la commande existe
            String checkSQL = "SELECT * FROM commandes WHERE ID_commande = ?";
            try (PreparedStatement checkStmt = connexion.prepareStatement(checkSQL)) {
                checkStmt.setInt(1, achat.getIdCommande());
                ResultSet resultats = checkStmt.executeQuery();

                if (resultats.next()) {
                        String deleteSQL = "DELETE FROM commandes WHERE ID_commande = ?";
                        try (PreparedStatement deleteStmt = connexion.prepareStatement(deleteSQL)) {
                            deleteStmt.setInt(1, achat.getIdCommande());
                            int rowsAffected = deleteStmt.executeUpdate();
                        }
                    }
                 else {
                    messsage.message_entite_inconnue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
    }


    @Override
    public void marquerCommePayee(int id) {
        try {
            Connection connexion = daoFactory.getConnection();
            String requete = "UPDATE commandes SET payer = true WHERE ID_commande = ?";
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
    }
    public void modifierQuantite(int idCommande, int nouvelleQuantite) {
        try {
            Connection connexion = daoFactory.getConnection();
            String requete = "UPDATE commandes SET quantite = ? WHERE ID_commande = ?";
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, idCommande);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
    }
    public void modifierAdresse(int idCommande, String nouvelleAdresse) {
        try {
            Connection connexion = daoFactory.getConnection();
            String sql = "UPDATE commandes SET Adresse = ? WHERE ID_commande = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, nouvelleAdresse);
            stmt.setInt(2, idCommande);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mettreajournote(int note, Commande commande) {
        try {
            Connection connexion = daoFactory.getConnection();
            String sql = "UPDATE commandes SET note = ? WHERE ID_commande = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, note);
            stmt.setInt(2, commande.getIdCommande());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double calcul_moyenne_note(int id_produit) {
        double moyenne = 0;
        int totalNotes = 0;
        int nombreNotes = 0;

        try {
            Connection connexion = daoFactory.getConnection();
            String sql = "SELECT note FROM commandes WHERE payer = true AND ID_produit = ? AND note IS NOT NULL";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, id_produit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int note = rs.getInt("note");
                totalNotes += note;
                nombreNotes++;
            }

            rs.close();
            stmt.close();

            if (nombreNotes > 0) {
                moyenne = (double) totalNotes / nombreNotes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moyenne;
    }


}
