package Dao;
import modele.Personne;
import java.util.ArrayList;

public interface PersonneDAO {
    public void ajouter(Personne client) ;
    public Personne chercher_adresse_mail(String adresse_mail);
    public Personne chercher_id(int id);
    public void supprimer (Personne client);
    public int modifier_admin_nombre(int id, int nouveauRole);
    public boolean mettreAJourClient(int id, String nom, String email, String adresse, String nouveauMdp);
}
