package Dao;

import Vue.message_erreur;
import Vue.pop_up_interrup;
import modele.Produit;

import java.sql.*;
import java.util.ArrayList;

public class ProduitDAOImpl implements ProduitDAO {
    private DaoFactory daoFactory;
    private message_erreur messsage;
    // constructeur dépendant de la classe DaoFactory
    public ProduitDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        messsage = new message_erreur();
    }

    @Override
    /*
        Récupérer la liste des produits de la base de données dans listeClients
    */
    public ArrayList<Produit> getAll() {
        ArrayList<Produit> listeProduit = new ArrayList<Produit>();
        int produitId,quantite,quantite_reduit;
        String produit_nom,produit_marque,produit_description,image,description_courte;
        double produit_prix,prix_reduit;
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des produits de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from produits");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                produitId = resultats.getInt(1);
                produit_nom = resultats.getString(2);
                produit_marque = resultats.getString(3);
                produit_prix = resultats.getDouble(4);
                prix_reduit= resultats.getDouble(5);
                quantite = resultats.getInt(6);
                quantite_reduit =resultats.getInt(7);
                produit_description = resultats.getString(8);
                image = resultats.getString(9);
                description_courte=resultats.getString(10);
                // instancier un objet de Produit avec ces 3 champs en paramètres
                Produit client = new Produit(produitId,produit_nom,produit_marque,produit_prix,prix_reduit, quantite,quantite_reduit,produit_description,image,description_courte);

                // ajouter ce produit à listeProduits
                listeProduit.add(client);
            }
            return listeProduit;
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de clients impossible");
        }
        return listeProduit;
    }
    @Override
    /*
        Ajouter un client à la table
    */
    public void ajouter(Produit produit){
        int produitId,quantite,quantite_reduit;
        String produit_nom,produit_marque,produit_description,image,description_courte;
        double produit_prix,prix_reduit;

        try {
            // connexion
            Connection connexion = daoFactory.getConnection();

            // récupération du nom et prix de l'objet product en paramètre
            produit_nom = produit.getProduit_nom();
            produit_marque = produit.getProduit_marque();
            produit_prix = produit.getProduit_prix();
            prix_reduit= produit.getPrix_reduit();
            quantite= produit.getQuantite();
            quantite_reduit = produit.getQuantite_reduit();
            image=produit.getImage();
            description_courte=produit.getDescription_courte();
            // Exécution de la requête INSERT INTO de l'objet product en paramètre
            PreparedStatement preparedStatement = connexion.prepareStatement(
                    "INSERT INTO `produits` (`produit_nom`, `produit_marque`, `produit_prix`, `prix_reduit`, `quantite`, `quantite_reduit`, `description`, `image`, `description_courte`) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );

// Remplacement des "?" par les vraies valeurs
            preparedStatement.setString(1, produit_nom);
            preparedStatement.setString(2,produit_marque );
            preparedStatement.setDouble(3, produit_prix);
            preparedStatement.setDouble(4, prix_reduit);
            preparedStatement.setInt(5, quantite);
            preparedStatement.setInt(6, quantite_reduit);
            preparedStatement.setString(7, produit.getDescription());
            preparedStatement.setString(8, image);
            preparedStatement.setString(9, description_courte);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
    }

    @Override
    /*
        Chercher un produit à la table avec le nom
    */
    public ArrayList<Produit> chercher_par_nom(String nom) {
        ArrayList<Produit> produits = new ArrayList<>();

        try {
            Connection connexion = daoFactory.getConnection();
            String requete = "SELECT * FROM produits WHERE produit_nom LIKE ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(requete);
            preparedStatement.setString(1, "%" + nom + "%");
            ResultSet resultats = preparedStatement.executeQuery();

            while (resultats.next()) {
                int produitId = resultats.getInt(1);
                String produit_nom = resultats.getString(2);
                String produit_marque = resultats.getString(3);
                double produit_prix = resultats.getDouble(4);
                double prix_reduit = resultats.getDouble(5);
                int quantite = resultats.getInt(6);
                int quantite_reduit = resultats.getInt(7);
                String produit_description = resultats.getString(8);
                String image = resultats.getString(9);
                String description_courte = resultats.getString(10);

                Produit produit = new Produit(produitId, produit_nom, produit_marque, produit_prix, prix_reduit, quantite, quantite_reduit, produit_description, image, description_courte);
                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }

        return produits;
    }

    /*
        Chercher un produit à la table avec la marque
    */
    @Override
    public ArrayList<Produit> chercher_par_marque(String marque) {
        ArrayList<Produit> produits = new ArrayList<>();

        try {
            Connection connexion = daoFactory.getConnection();
            String requete = "SELECT * FROM produits WHERE produit_marque LIKE ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(requete);
            preparedStatement.setString(1, "%" + marque + "%");
            ResultSet resultats = preparedStatement.executeQuery();

            while (resultats.next()) {
                int produitId = resultats.getInt(1);
                String produit_nom = resultats.getString(2);
                String produit_marque = resultats.getString(3);
                double produit_prix = resultats.getDouble(4);
                double prix_reduit = resultats.getDouble(5);
                int quantite = resultats.getInt(6);
                int quantite_reduit = resultats.getInt(7);
                String produit_description = resultats.getString(8);
                String image = resultats.getString(9);
                String description_courte = resultats.getString(10);

                Produit produit = new Produit(produitId, produit_nom, produit_marque, produit_prix, prix_reduit, quantite, quantite_reduit, produit_description, image, description_courte);
                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }

        return produits;
    }

    /*
    Chercher un client à la table avec le prix
*/
    @Override
    public ArrayList<Produit> chercher_par_prix(double prixMax) {
        ArrayList<Produit> produits = new ArrayList<>();

        try {
            Connection connexion = daoFactory.getConnection();
            String requete = "SELECT * FROM produits WHERE produit_prix < ?";
            PreparedStatement preparedStatement = connexion.prepareStatement(requete);
            preparedStatement.setDouble(1, prixMax);
            ResultSet resultats = preparedStatement.executeQuery();

            while (resultats.next()) {
                int produitId = resultats.getInt(1);
                String produit_nom = resultats.getString(2);
                String produit_marque = resultats.getString(3);
                double produit_prix = resultats.getDouble(4);
                double prix_reduit = resultats.getDouble(5);
                int quantite = resultats.getInt(6);
                int quantite_reduit = resultats.getInt(7);
                String produit_description = resultats.getString(8);
                String image = resultats.getString(9);
                String description_courte = resultats.getString(10);

                Produit produit = new Produit(produitId, produit_nom, produit_marque, produit_prix, prix_reduit, quantite, quantite_reduit, produit_description, image, description_courte);
                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }

        return produits;
    }
    /*
        Chercher un produit à la table avec le id
    */
    @Override
    public Produit chercher_id(int ID)  {
        Produit produit = null;
        int produitId,quantite,quantite_reduit, compteur_secu=0;
        String produit_nom,produit_marque,produit_description,image,description_courte;
        double produit_prix,prix_reduit;
        try
        {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // Exécution de la requête SELECT pour récupérer le client de l'id dans la base de données
            ResultSet resultats = statement.executeQuery("select * from produits where id_produit =" + ID);

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next() && compteur_secu < 100000000)
            {
                // récupérer les 10 champs de la table produit dans la base de données
                produitId = resultats.getInt(1);
                produit_nom = resultats.getString(2);
                produit_marque = resultats.getString(3);
                produit_prix = resultats.getDouble(4);
                prix_reduit= resultats.getDouble(5);
                quantite = resultats.getInt(6);
                quantite_reduit =resultats.getInt(7);
                produit_description = resultats.getString(8);
                image = resultats.getString(9);
                description_courte=resultats.getString(10);

                // Si l'id du client est trouvé, l'instancier et sortir de la boucle
                if (ID==produitId)
                {
                    // instancier un objet de Produit avec ces 3 champs en paramètres
                    produit = new Produit(produitId,produit_nom,produit_marque,produit_prix,prix_reduit, quantite,quantite_reduit,produit_description,image,description_courte);
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

        return produit;
    }
    /*
            Chercher un produit à la table avec le critère
        */
    public ArrayList<Produit> chercher_par_description_courte(String recherche) {
        ArrayList<Produit> produits = new ArrayList<>();

        try {
            Connection connexion = daoFactory.getConnection();


            // Découper la recherche en mots
            String[] mots = recherche.toLowerCase().split("\\s+");

            // Construction dynamique de la requête
            StringBuilder requete = new StringBuilder("SELECT * FROM produits WHERE ");
            for (int i = 0; i < mots.length; i++) {
                if (i > 0) {
                    requete.append(" OR ");
                }
                requete.append("LOWER(description_courte) LIKE ?");
            }

            PreparedStatement preparedStatement = connexion.prepareStatement(requete.toString());

            // Ajout des paramètres avec les wildcards %
            for (int i = 0; i < mots.length; i++) {
                preparedStatement.setString(i + 1, "%" + mots[i] + "%");
            }

            ResultSet resultats = preparedStatement.executeQuery();

            while (resultats.next()) {
                int produitId = resultats.getInt(1);
                String produit_nom = resultats.getString(2);
                String produit_marque = resultats.getString(3);
                double produit_prix = resultats.getDouble(4);
                double prix_reduit = resultats.getDouble(5);
                int quantite = resultats.getInt(6);
                int quantite_reduit = resultats.getInt(7);
                String produit_description = resultats.getString(8);
                String image = resultats.getString(9);
                String description_courte = resultats.getString(10);

                Produit produit = new Produit(produitId, produit_nom, produit_marque, produit_prix, prix_reduit, quantite, quantite_reduit, produit_description, image, description_courte);
                produits.add(produit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }

        return produits;
    }


    /*
        modifie un client dans la table
    */
    public void modifierQuantiteProduit(int idProduit, int nouvelleQuantite) {
    Connection connection = null;
    PreparedStatement statement = null;

    try {
        connection = daoFactory.getConnection();
        String requete = "UPDATE produits SET quantite = ? WHERE id_produit = ?";
        statement = connection.prepareStatement(requete);
        statement.setInt(1, nouvelleQuantite);
        statement.setInt(2, idProduit);

        int lignesModifiees = statement.executeUpdate();
        if (lignesModifiees > 0) {
            System.out.println("✅ Quantité mise à jour pour le produit ID: " + idProduit);
        } else {
            System.out.println("⚠ Aucun produit trouvé avec l'ID: " + idProduit);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    @Override
    /*
       supprime un client dans la table
   */
    public void supprimer (Produit article) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête SELECT pour récupérer le produit de l'id dans la base de données
            ResultSet resultats = statement.executeQuery("select * from produits where id_produit ="+article.getId_produit());

            if (resultats.next()) {
                // Si le produit existe, on demande la confirmation avant de le supprimer
                pop_up_interrup message= new pop_up_interrup();
                int retour=message.ConfirmationSuppression(article.getProduit_nom());
                if (retour==0) {
                    // Requête SQL pour supprimer le produit
                    String deleteSQL = "DELETE FROM produits WHERE id_produit= ?";

                    // Créer un PreparedStatement pour l'exécution de la requête
                    try (PreparedStatement preparedStatement = connexion.prepareStatement(deleteSQL)) {
                        preparedStatement.setInt(1, article.getId_produit());  // Remplacer le ? par l'ID du produit à supprimer

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
    }

    public void mettreajourProduit(Produit produit){
        String sql = "UPDATE produits SET " +
                "produit_nom = ?, " +
                "produit_marque = ?, " +
                "produit_prix = ?, " +
                "prix_reduit = ?, " +
                "quantite = ?, " +
                "quantite_reduit = ?, " +
                "description = ?, " +
                "image = ?, " +
                "description_courte = ? " +
                "WHERE id_produit = ?";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produit.getProduit_nom());
            stmt.setString(2, produit.getProduit_marque());
            stmt.setDouble(3, produit.getProduit_prix());
            stmt.setDouble(4, produit.getPrix_reduit());
            stmt.setInt(5, produit.getQuantite());
            stmt.setInt(6, produit.getQuantite_reduit());
            stmt.setString(7, produit.getDescription());
            stmt.setString(8, produit.getImage());
            stmt.setString(9, produit.getDescription_courte());
            stmt.setInt(10, produit.getId_produit());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            messsage.message_pb_connection();
        }
    }

}
