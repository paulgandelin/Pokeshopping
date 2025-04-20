package Dao;

import modele.Produit;

import java.util.ArrayList;

public interface ProduitDAO {
    public ArrayList<Produit> getAll();
    public void ajouter(Produit produit) ;
    public ArrayList<Produit> chercher_par_nom(String nom);
    public ArrayList<Produit> chercher_par_marque(String nom);
    public ArrayList<Produit> chercher_par_prix(double prix);
    public Produit chercher_id(int ID);
    public ArrayList<Produit> chercher_par_description_courte(String recherche);
    public void modifierQuantiteProduit(int idProduit, int quantiteARetirer);
    public void supprimer (Produit article);
    public void mettreajourProduit(Produit produit);
}
