package Dao;
import modele.Commande;

import java.sql.*;
import java.util.ArrayList;

import Vue.*;

public class CommandeDAOImpl implements CommandeDAO {
    private DaoFactory daoFactory;
    private message_erreur message;


    // constructeur dépendant de la classe DaoFactory
    public CommandeDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        message = new message_erreur();
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
            message.message_pb_connection();
        }
    }


    @Override

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
            message.message_pb_connection(); // toujours avec les 3 "s" dans "messsage" ?
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
            message.message_pb_connection(); // <- tu as une classe messsage apparemment ? (avec 3 's')
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
            message.message_pb_connection();
        }

        return commandesPayees;
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
                    message.message_entite_inconnue();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.message_pb_connection();
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
            message.message_pb_connection();
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
            message.message_pb_connection();
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
            message.message_pb_connection();
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
            message.message_pb_connection();
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
            message.message_pb_connection();
        }

        return moyenne;
    }


}
