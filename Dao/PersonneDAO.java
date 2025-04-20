package Dao;
import modele.Personne;
import java.util.ArrayList;

public interface PersonneDAO {
    public ArrayList<Personne> getAll();
    public void ajouter(Personne client) ;
    public Personne chercher_adresse_mail(String adresse_mail);
    public Personne chercher_id(int id);
    public Personne modifier(Personne client);
    public void supprimer (Personne client);
}
