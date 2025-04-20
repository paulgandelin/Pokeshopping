package modele;

public class Personne {
    private int ID_personne;
    private String nom_client;
    private String mail_client;
    private String mdp_client;
    private String adresse_postale_client;
    private int identifiant_admin;

    // constructeur
    public Personne(int ID_client, String nom_client, String mail_client, String mdp_client, String adresse_postale_client, int identifiant_admin)
    {
        this.ID_personne = ID_client;
        this.nom_client = nom_client;
        this.mail_client = mail_client;
        this.mdp_client = mdp_client;
        this.adresse_postale_client = adresse_postale_client;
        this.identifiant_admin=identifiant_admin;
    }
    public Personne(int ID_client, String nom_client, String mail_client, String mdp_client, int identifiant_admin)
    {
        this.ID_personne = ID_client;
        this.nom_client = nom_client;
        this.mail_client = mail_client;
        this.mdp_client = mdp_client;
        adresse_postale_client = "";
        this.identifiant_admin=identifiant_admin;
    }


    // getter de la classe client
    public int getID_personne() {
        return ID_personne;
    }
    public String getNom_client() {
        return nom_client;
    }
    public String getMail_client() {
        return mail_client;
    }
    public String getMdp_client() {
        return mdp_client;
    }
    public String getAdresse_postale_client() {
        return adresse_postale_client;
    }
    public int getIdentifiant_admin() {
        return identifiant_admin;
    }


    // setter de la classe client
    public void setID_personne(int ID_personne) {
        this.ID_personne = ID_personne;
    }
    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }
    public void setMail_client(String mail_client) {
        this.mail_client = mail_client;
    }
    public void setMdp_client(String mdp_client) {
        this.mdp_client = mdp_client;
    }
    public void setAdresse_postale_client(String adresse_postale_client) {
        this.adresse_postale_client = adresse_postale_client;
    }
    public void setidentifiant_admin(int identifiant_admin) {
        this.identifiant_admin = identifiant_admin;
    }
}
