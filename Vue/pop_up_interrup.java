package Vue;

import javax.swing.*;

public class pop_up_interrup {
    private int retour;

    public pop_up_interrup() {
        retour = 100000000;
    }

    public int ConfirmationSuppression(String nom) {
        int choix = JOptionPane.showConfirmDialog(
                null,
                "Êtes-vous sûr de vouloir supprimer le client avec le nom ?" + nom,
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choix == JOptionPane.YES_OPTION) {
            return retour=0;
        } else if (choix == JOptionPane.NO_OPTION) {
            return retour=1;
        } else {
            return retour;
        }
    }

    public void message_confirmation_personne(int choix) {
        if (choix == 0) {
            JOptionPane.showMessageDialog(null, "La personne a été supprimée !", "Suppression", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (choix == 1) {
            JOptionPane.showMessageDialog(null, "Suppression annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (choix == 2) {
            JOptionPane.showMessageDialog( null,"la personne n'a pas pu être supprimé","Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "Erreur durant la supression", "Message erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void message_confirmation_produit(int choix) {
        if (choix == 0) {
            JOptionPane.showMessageDialog(null, "Le produit a été supprimée !", "Suppression", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (choix == 1) {
            JOptionPane.showMessageDialog(null, "Suppression annulée.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (choix == 2) {
            JOptionPane.showMessageDialog( null,"le produit n'a pas pu être supprimé","Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "Erreur durant la supression", "Message erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public int ConfirmationSuppression_commande(int id) {
        int choix = JOptionPane.showConfirmDialog(
                null,
                "Êtes-vous sûr de vouloir supprimer la commande avec le numéro ?" + id,
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choix == JOptionPane.YES_OPTION) {
            return retour=0;
        } else if (choix == JOptionPane.NO_OPTION) {
            return retour=1;
        } else {
            return retour;
        }
    }

    public String email_gestion_role(){
        return JOptionPane.showInputDialog("Adresse mail", "Entrez l'adresse mail de l'utilisateur à modifier :");
    }

    public int gestion_role(boolean estAdmin, String nom, String emailCible) {
        String[] options = estAdmin ?
                new String[]{"Rétrograder en utilisateur", "Annuler"} :
                new String[]{"Promouvoir en admin", "Annuler"};

        return  JOptionPane.showOptionDialog(null,
                "Utilisateur trouvé : " + nom + "\nEmail : " + emailCible + "\nActuellement : " + (estAdmin ? "Admin" : "Utilisateur"),
                "Gérer le rôle",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

    }
}