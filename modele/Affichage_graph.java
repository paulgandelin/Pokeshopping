package modele;
import Dao.*;

import java.sql.*;
import java.util.*;


public class Affichage_graph {
    private DaoFactory daoFactory;

    Affichage_graph(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public int totalProduitsVendus() {
        int total = 0;
        String sql = "SELECT SUM(quantite) FROM commandes WHERE payer = true";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public double CAProduitsVendus() {
        double total = 0.0;

        String sql = "SELECT ID_produit, quantite FROM commandes WHERE payer = true";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);

            while (rs.next()) {
                int idProduit = rs.getInt("id_produit");
                int quantite = rs.getInt("quantite");

                Produit produit = produitDAO.chercher_id(idProduit);
                total += calculprix(produit, quantite);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int nb_client() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM personnes";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1); // La première colonne contient le COUNT(*)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int nb_commande() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM commandes WHERE payer = true";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1); // La première colonne contient le COUNT(*)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public Map<String, Integer> recupererVentesParProduit() {
        CommandeDAOImpl commandeDAO = new CommandeDAOImpl(daoFactory);
        ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);

        Map<Integer, Integer> ventesParId = new HashMap<>();
        Map<String, Integer> ventesParNom = new HashMap<>();

        ArrayList<Commande> commandesPayees = commandeDAO.chercherCommandesPayees();

        if (commandesPayees.isEmpty()) {
            System.out.println("Aucune commande payée trouvée.");
        }

        // Calcul des ventes par produit (id)
        for (Commande cmd : commandesPayees) {
            int idProduit = cmd.getIdProduit();
            int quantite = cmd.getQuantite();

            // Ajout de la quantité pour chaque produit dans ventesParId
            ventesParId.put(idProduit, ventesParId.getOrDefault(idProduit, 0) + quantite);
        }

        // Conversion de ventesParId en ventesParNom avec les noms de produits
        for (Map.Entry<Integer, Integer> entry : ventesParId.entrySet()) {
            Produit produit = produitDAO.chercher_id(entry.getKey());

            if (produit != null) {
                String nom = produit.getProduit_nom();
                ventesParNom.put(nom, entry.getValue());
            } else {
                System.out.println("Produit avec ID " + entry.getKey() + " non trouvé.");
            }
        }

        // Si ventesParNom est vide, cela peut indiquer un problème avec la récupération des produits
        if (ventesParNom.isEmpty()) {
            System.out.println("Aucune vente par produit n'a été trouvée.");
        }

        return ventesParNom;
    }

    public Map<String, Integer> getComportementClients() {
        Map<String, Integer> comportement = new HashMap<>();
        Map<Integer, Integer> clientCommandes = new HashMap<>();

        String sql = "SELECT ID_client FROM commandes WHERE payer = true";

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int clientId = rs.getInt("ID_client");
                clientCommandes.put(clientId, clientCommandes.getOrDefault(clientId, 0) + 1);
            }

            int achatsRepetes = 0;
            int commandesUniques = 0;

            for (int count : clientCommandes.values()) {
                if (count > 1) achatsRepetes++;
                else commandesUniques++;
            }

            comportement.put("Achats Répétés", achatsRepetes);
            comportement.put("Commandes Uniques", commandesUniques);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comportement;
    }

    public Map<String, Integer> recupererTypeProduitsVendus() {
        Map<String, Integer> resultats = new HashMap<>();
        resultats.put("Pokemon", 0);
        resultats.put("Légendaire", 0);
        resultats.put("Objet", 0);
        resultats.put("Non défini", 0);

        CommandeDAOImpl commandeDAO = new CommandeDAOImpl(daoFactory);
        ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);
        List<Commande> commandesPayees = commandeDAO.chercherCommandesPayees();

        if (commandesPayees.isEmpty()) {
            System.out.println("Aucune commande payée trouvée.");
        }

        for (Commande cmd : commandesPayees) {
            Produit produit = produitDAO.chercher_id(cmd.getIdProduit());

            if (produit == null) {
                System.out.println("Produit introuvable pour l'ID : " + cmd.getIdProduit());
                continue;
            }

            String description = produit.getDescription_courte();
            if (description == null) {
                System.out.println("Description manquante pour le produit ID : " + produit.getId_produit());
                resultats.put("Non défini", resultats.get("Non défini") + cmd.getQuantite());
                continue;
            }

            description = description.toLowerCase();
            int quantite = cmd.getQuantite();

            if (description.contains("légendaire") || description.contains("legend")) {
                resultats.put("Légendaire", resultats.get("Légendaire") + quantite);
            } else if (description.contains("pokemon")) {
                resultats.put("Pokemon", resultats.get("Pokemon") + quantite);
            } else if (description.contains("objet")) {
                resultats.put("Objet", resultats.get("Objet") + quantite);
            } else {
                resultats.put("Non défini", resultats.get("Non défini") + quantite);
            }
        }

        return resultats;
    }







    private double calculprix(Produit produit, int quantite) {
        int seuil = produit.getQuantite_reduit();
        double prixReduit = produit.getPrix_reduit();
        double prixNormal = produit.getProduit_prix();
        double prix=0;
        if (seuil==0) {
            prix =quantite*prixNormal;
        }
        else{
            int packsReduits = quantite / seuil;
            int resteNormal = quantite % seuil;
            prix = (packsReduits *seuil* prixReduit) + (resteNormal * prixNormal);
        }
        return prix;
    }

}
