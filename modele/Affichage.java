package modele;

import Dao.DaoFactory;
import Dao.ProduitDAOImpl;

import java.util.ArrayList;
import java.util.HashSet;

public class Affichage {
    DaoFactory daoFactory;
    public Affichage(DaoFactory daoFactory) {
        this.daoFactory=daoFactory;
    }

    public ArrayList<Produit> affichage_produit_accueil() {
        ProduitDAOImpl produitDAO;
        ArrayList<Produit> produits;


        produitDAO=new ProduitDAOImpl(daoFactory);
        produits=produitDAO.getAll();
        return produits;
    }
    public ArrayList<Produit> affichage_produit_cherhcer(String chercher) {
        ProduitDAOImpl produitDAO;
        ArrayList<Produit> produits1, produits2, produits3;
        ArrayList<Produit> produits = new ArrayList<>();

        produitDAO = new ProduitDAOImpl(daoFactory);
        produits1 = produitDAO.chercher_par_nom(chercher);
        produits2 = produitDAO.chercher_par_marque(chercher);
        produits3 = produitDAO.chercher_par_description_courte(chercher);

        // Utiliser un HashSet pour éviter les doublons
        HashSet<Produit> produitSet = new HashSet<>();
        produitSet.addAll(produits1);
        produitSet.addAll(produits2);
        produitSet.addAll(produits3);

        // Convertir le HashSet en ArrayList
        produits.addAll(produitSet);

        return produits;

    }
    public ArrayList<Produit> affichage_produit_cate(String chercher) {
        ProduitDAOImpl produitDAO;
        ArrayList<Produit> produits = new ArrayList<>();

        produitDAO = new ProduitDAOImpl(daoFactory);
        produits = produitDAO.chercher_par_description_courte(chercher);

        return produits;

    }
}
