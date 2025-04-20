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
        JOptionPane.showMessageDialog(null, "Il au moins un nom,un mot de passe et une adresse mail", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }
    public void erreur_adresse_mail(){
        JOptionPane.showMessageDialog(null, "adresse mail invalide", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }

}
