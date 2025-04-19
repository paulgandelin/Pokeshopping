package Vue;
import Dao.DaoFactory;
import Dao.PersonneDAOImpl;
import Controleur.SQL_securite;
import modele.Personne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ecran_de_connexion extends JFrame {
    private JTextField champUtilisateur;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion, boutonInscription;
    private SQL_securite secu=new SQL_securite();
    private Personne client=null;
    private PersonneDAOImpl pers=null;

    public Ecran_de_connexion(DaoFactory daoFactory) {
        setTitle("Écran de Connexion");
        setExtendedState(JFrame.MAXIMIZED_BOTH);;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Titre "S'identifier"
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titre = new JLabel("S'identifier");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        add(titre, gbc);

        // Champ utilisateur
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Adresse mail:"), gbc);
        champUtilisateur = new JTextField(15);
        gbc.gridx = 1;
        add(champUtilisateur, gbc);

        // Champ mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Mot de passe:"), gbc);
        champMotDePasse = new JPasswordField(15);
        gbc.gridx = 1;
        add(champMotDePasse, gbc);

        // Bouton Connexion
        boutonConnexion = new JButton("Connexion");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(boutonConnexion, gbc);

        // Bouton Créer un compte
        boutonInscription = new JButton("Créer un compte");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(boutonInscription, gbc);

