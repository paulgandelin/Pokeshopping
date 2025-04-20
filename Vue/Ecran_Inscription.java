package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Dao.*;
import modele.Cryptage;
import Controleur.SQL_securite;
import modele.Personne;

public class Ecran_Inscription extends JFrame {
    private JTextField champID, champNom, champEmail, champAdresse;
    private JPasswordField champMotDePasse;
    private JButton boutonCreerCompte, boutonRetour;



    public Ecran_Inscription(DaoFactory daoFactory) {
        PersonneDAOImpl compte = new PersonneDAOImpl(daoFactory);
        setTitle("Créer un compte");
        setExtendedState(JFrame.MAXIMIZED_BOTH);;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titre = new JLabel("Créer un compte");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, gbc);

        // Champ Nom (OBLIGATOIRE)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Nom (*):"), gbc);
        champNom = new JTextField(15);
        gbc.gridx = 1;
        add(champNom, gbc);

        // Champ Email (OBLIGATOIRE)
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Email (*):"), gbc);
        champEmail = new JTextField(15);
        gbc.gridx = 1;
        add(champEmail, gbc);

        // Champ Mot de Passe (OBLIGATOIRE)
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Mot de passe (*):"), gbc);
        champMotDePasse = new JPasswordField(15);
        gbc.gridx = 1;
        add(champMotDePasse, gbc);

        // Champ Adresse Postale (FACULTATIF)
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Adresse postale:"), gbc);
        champAdresse = new JTextField(20);
        gbc.gridx = 1;
        add(champAdresse, gbc);

        // Bouton Créer un compte
        boutonCreerCompte = new JButton("Créer un compte");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(boutonCreerCompte, gbc);

        // Bouton Retour
        boutonRetour = new JButton("Retour");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(boutonRetour, gbc);

        // Action du bouton "Créer un compte"
        boutonCreerCompte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Personne personne = null;
                SQL_securite securite = new SQL_securite();
                message_erreur erreur = new message_erreur();
                message_reussite reussite = new message_reussite();
                Cryptage cryptage = new Cryptage();
                if (new String(champMotDePasse.getPassword()).isEmpty()||champNom.getText().trim().isEmpty()||champEmail.getText().trim().isEmpty())
                {
                    erreur.erreur_saisie_inscription();
                }
                else{
                    if (SQL_securite.valid_mail(champEmail.getText().trim())&& securite.email_dans_BD(champEmail.getText().trim(),daoFactory))
                    {
                        String mdp = cryptage.CryptageMotDePasse(new String(champMotDePasse.getPassword()));
                        personne = new Personne(0, champNom.getText().trim(), champEmail.getText().trim(), mdp, champAdresse.getText().trim(), false);
                        compte.ajouter(personne);
                        reussite.message_creation_compte_ok();

                    }
                    else
                    {
                        erreur.erreur_adresse_mail();
                    }
                    }
                }

        });

        // Action du bouton "Retour"
        boutonRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme l'écran actuel
                new Ecran_de_connexion(daoFactory); // Retour à l'écran de connexion
            }
        });

        setVisible(true);
    }
}