package modele;

public class Commande {
    private int id_commande;
    private int id_produit;
    private int id_client;
    private String lieu_livraison;
    private int quantite;
    private int note;
    private boolean payer;

    // Constructeur
    public Commande(int id_commande, int id_produit, int id_client, String lieu_livraison, int quantite, boolean payer,int note)
    {
        this.id_commande = id_commande;
        this.id_produit = id_produit;
        this.id_client = id_client;
        this.lieu_livraison = lieu_livraison;
        this.quantite = quantite;
        this.payer = payer;
        this.note = note;
    }

    // Getters
    public int getIdCommande() {
        return id_commande;
    }

    public int getIdProduit() {
        return id_produit;
    }

    public int getIdClient() {
        return id_client;
    }

    public String getLieuLivraison() {
        return lieu_livraison;
    }

    public int getQuantite() {
        return quantite;
    }
    public boolean getPayer() {
        return payer;
    }
    public int getNote() {
        return note;
    }

    // Setters

    public void setLieuLivraison(String lieu_livraison) {
        this.lieu_livraison = lieu_livraison;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
