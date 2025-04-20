package modele;

import java.util.Objects;

public class Produit {
    private int id_produit;
    private String produit_nom;
    private String produit_marque;
    private double produit_prix;
    private double prix_reduit;
    private int quantite;
    private int quantite_reduit;
    private String description;
    private String image;
    private String description_courte;

    public Produit(int id_produit,String produit_nom,String produit_marque,double produit_prix,double prix_reduit,int quantite,int quantite_reduit,String description,String image,String description_courte) {
        this.id_produit=id_produit;
        this.produit_nom=produit_nom;
        this.produit_marque=produit_marque;
        this.produit_prix=produit_prix;
        this.prix_reduit=prix_reduit;
        this.quantite=quantite;
        this.quantite_reduit=quantite_reduit;
        this.description=description;
        this.image=image;
        this.description_courte=description_courte;
    }

    // Getter et Setter pour id_produit
    public int getId_produit() {
        return id_produit;
    }
    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    // Getter et Setter pour produit_nom
    public String getProduit_nom() {
        return produit_nom;
    }
    public void setProduit_nom(String produit_nom) {
        this.produit_nom = produit_nom;
    }

    // Getter et Setter pour produit_marque
    public String getProduit_marque() {
        return produit_marque;
    }
    public void setProduit_marque(String produit_marque) {
        this.produit_marque = produit_marque;
    }

    // Getter et Setter pour produit_prix
    public double getProduit_prix() {
        return produit_prix;
    }
    public void setProduit_prix(double produit_prix) {
        this.produit_prix = produit_prix;
    }

    // Getter et Setter pour prix_reduit
    public double getPrix_reduit() {
        return prix_reduit;
    }
    public void setPrix_reduit(double prix_reduit) {
        this.prix_reduit = prix_reduit;
    }

    // Getter et Setter pour quantite
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    // Getter et Setter pour quantite_reduit
    public int getQuantite_reduit() {
        return quantite_reduit;
    }
    public void setQuantite_reduit(int quantite_reduit) {
        this.quantite_reduit = quantite_reduit;
    }

    // Getter et Setter pour description
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter et Setter pour image
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    // Getter et Setter pour image
    public String getDescription_courte() {
        return description_courte;
    }
    public void setDescription_courte(String description) {
        this.description_courte = description_courte;
    }

    // Méthode equals pour comparer les produits
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produit produit = (Produit) obj;
        return id_produit == produit.id_produit; // Comparaison basée sur id_produit
    }

    // Méthode hashCode pour générer un code unique basé sur id_produit
    @Override
    public int hashCode() {
        return Objects.hash(id_produit); // Utilisation de id_produit pour un hash unique
    }
    @Override
    public String toString() {
        return "ID: " + id_produit + " | " + produit_nom + " - " + produit_marque;
    }
}
