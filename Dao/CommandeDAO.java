package Dao;
import modele.Commande;

import java.util.ArrayList;

public interface CommandeDAO {
    public void ajouter(Commande produit) ;
    public ArrayList<Commande> chercherCommandesPayeesParClient(int idClient);
    public ArrayList<Commande> chercherCommandesNonPayeesParClient(int idClient);
    public void marquerCommePayee(int id);
    public void supprimer (Commande produit);
    public void modifierQuantite(int idCommande, int nouvelleQuantite);
    public void modifierAdresse(int idCommande, String nouvelleAdresse);
    public ArrayList<Commande> chercherCommandesPayees();
}
