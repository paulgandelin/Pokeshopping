package Vue;

import javax.swing.*;

public class message_erreur {
    public void message_pb_connection(){
        JOptionPane.showMessageDialog(null, "Problème(s) de connexion avec la base de donnée", "Erreur critique", JOptionPane.INFORMATION_MESSAGE);
    }
    public void message_entite_inconnue(){
        JOptionPane.showMessageDialog(null, "Désolé Pas de corresondance dans la base de donnée", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_saisie_inscription(){
        JOptionPane.showMessageDialog(null, "Il doit avoir au moins un nom,un mot de passe et une adresse mail", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_adresse_mail(){
        JOptionPane.showMessageDialog(null, "adresse mail invalide", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_commande_paye(){
        JOptionPane.showMessageDialog(null, "Aucune commande payée trouvée.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_recherche_produit(int id){
        JOptionPane.showMessageDialog(null, "Produit avec ID "+ id + " non trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_vente_produit(){
        JOptionPane.showMessageDialog(null, "Aucune vente par produit n'a été trouvée.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_description_produit(int id){
        JOptionPane.showMessageDialog(null, "Description manquante pour le produit ID : " + id, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_gestion_role(){
        JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du rôle.");
    }

    public void adresse_mail_pas_dans_bdd(){
        JOptionPane.showMessageDialog(null, "Aucun utilisateur trouvé avec cet email.");
    }
    public void abs_donnee(){
        JOptionPane.showMessageDialog(null, "Aucune donnée de vente disponible.");
    }

}
