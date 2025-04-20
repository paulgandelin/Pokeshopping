package Vue;

import Dao.*;
import modele.Cryptage;
import Controleur.Gestion_role;
import Controleur.Maj_client;
import modele.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Ecran_profil_client extends JFrame {
    private JLabel labelNom, labelEmail, labelAdresse;
    private JButton boutonModifier, boutonDeconnexion, boutonAdmin, boutonAjouterProduit,boutonModifierProduit, boutonRetour,boutonToutesCommandes,boutonGererRoles;;
    private Personne client;
    private Connection connexion;
    private DaoFactory dao;

    public Ecran_profil_client(Personne client, DaoFactory daoFactory) {
        this.client = client;
        dao = daoFactory;
        try {
            this.connexion = daoFactory.getConnection();
        }
        catch (SQLException e) {
            System.out.println("erreur de connexion");
        }

        setTitle("Profil Utilisateur");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(creerPanelInfos(), BorderLayout.CENTER);
        add(creerPanelBoutons(daoFactory), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel creerPanelInfos() {
        JPanel panelInfos = new JPanel();
        panelInfos.setLayout(new BoxLayout(panelInfos, BoxLayout.Y_AXIS));
        panelInfos.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));
        panelInfos.setBackground(Color.LIGHT_GRAY);

        labelNom = new JLabel("👤 Nom : " + client.getNom_client());
        labelEmail = new JLabel("📧 Email : " + client.getMail_client());
        labelAdresse = new JLabel("📍 Adresse : " + client.getAdresse_postale_client());

        setLabelStyle(labelNom);
        setLabelStyle(labelEmail);
        setLabelStyle(labelAdresse);

        panelInfos.add(labelNom);
        panelInfos.add(Box.createVerticalStrut(10));
        panelInfos.add(labelEmail);
        panelInfos.add(Box.createVerticalStrut(10));
        panelInfos.add(labelAdresse);

        boutonToutesCommandes = new JButton("📜 Voir l'historique de toutes commandes");
        setButtonStyle(boutonToutesCommandes);
        boutonToutesCommandes.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonToutesCommandes.addActionListener(e -> {
            new Ecran_toutes_commandes_client(client, dao);  // 👈 cette classe doit exister
        });

        panelInfos.add(Box.createVerticalStrut(20));
        panelInfos.add(boutonToutesCommandes);

        if (client.getIdentifiant_admin()) {
            Gestion_role role=new Gestion_role();
            boutonAdmin = new JButton("⚙️ Stats de Vente");
            setButtonStyle(boutonAdmin);
            boutonAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInfos.add(Box.createVerticalStrut(20));
            panelInfos.add(boutonAdmin);
            boutonAjouterProduit = new JButton("➕ Ajouter un produit");
            setButtonStyle(boutonAjouterProduit);
            boutonAjouterProduit.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInfos.add(Box.createVerticalStrut(10));
            panelInfos.add(boutonAjouterProduit);
            ///     //////
            boutonModifierProduit = new JButton("➕ Modifier un produit");
            setButtonStyle(boutonModifierProduit);
            boutonModifierProduit.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInfos.add(Box.createVerticalStrut(10));
            panelInfos.add(boutonModifierProduit);

            boutonGererRoles = new JButton("👥 Gérer rôles utilisateurs");
            setButtonStyle(boutonGererRoles);
            boutonGererRoles.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInfos.add(Box.createVerticalStrut(20));
            panelInfos.add(boutonGererRoles);


            boutonAjouterProduit.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Interface d’ajout de produit");
                new Ecran_nouveau_produit(dao);
            });

            boutonModifierProduit.addActionListener(e -> {
                String nomRecherche = JOptionPane.showInputDialog(null, "Entrez le nom du produit à modifier :");

                if (nomRecherche != null && !nomRecherche.trim().isEmpty()) {
                    ProduitDAOImpl produitDAO = new ProduitDAOImpl(dao);
                    try {
                        // Recherche floue dans les noms de produits
                        ArrayList<Produit> resultats = produitDAO.chercher_par_nom(nomRecherche.trim());

                        if (resultats.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Aucun produit trouvé avec ce nom.");
                        } else if (resultats.size() == 1) {
                            // Un seul produit trouvé → modification directe
                            new Ecran_modifier_produit(dao, resultats.get(0));
                        } else {
                            // Plusieurs produits trouvés → choix utilisateur
                            Produit choix = (Produit) JOptionPane.showInputDialog(
                                    null,
                                    "Sélectionnez le produit à modifier :",
                                    "Choix du produit",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    resultats.toArray(),
                                    resultats.get(0)
                            );

                            if (choix != null) {
                                new Ecran_modifier_produit(dao, choix);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors de la recherche des produits.");
                    }
                }
            });



            boutonAdmin.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Ouverture du panneau d'administration...");
                new Ecran_Statistiques_Admin(dao);
            });
            boutonGererRoles.addActionListener(e -> role.ouvrirFenetreGestionRoles(dao));
        }

        return panelInfos;
    }

    private JPanel creerPanelBoutons(DaoFactory daoFactory) {
        JPanel panelBoutons = new JPanel(new BorderLayout());
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        boutonModifier = new JButton("✏️ Modifier le profil");
        boutonDeconnexion = new JButton("🚪 Déconnexion");
        boutonRetour = new JButton("🔙 Retour");

        setButtonStyle(boutonModifier);
        setButtonStyle(boutonDeconnexion);
        setButtonStyle(boutonRetour);

        boutonModifier.addActionListener(e -> modifierProfil(daoFactory));
        boutonDeconnexion.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Déconnexion réussie !");
            dispose();
            System.exit(0);
        });
        boutonRetour.addActionListener(e -> {
            dispose(); // ferme la fenêtre actuelle
            new Ecran_principal(client, daoFactory); // ouvre la fenêtre principale
        });

        JPanel panelGauche = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGauche.add(boutonRetour);
        panelGauche.add(boutonModifier);

        JPanel panelDroit = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDroit.add(boutonDeconnexion);

        panelBoutons.add(panelGauche, BorderLayout.WEST);
        panelBoutons.add(panelDroit, BorderLayout.EAST);

        return panelBoutons;
    }


    private void modifierProfil(DaoFactory daoFactory) {
        JTextField newNom = new JTextField(client.getNom_client());
        JTextField newEmail = new JTextField(client.getMail_client());
        JTextField newAdresse = new JTextField(client.getAdresse_postale_client());
        JPasswordField newPassword = new JPasswordField();
        Cryptage cryptage = new Cryptage();

        Object[] message = {
                "Nom :", newNom,
                "Email :", newEmail,
                "Adresse :", newAdresse,
                "Nouveau mot de passe (laisser vide si inchangé) :", newPassword
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Modifier le profil", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nomModifie = newNom.getText();
            String emailModifie = newEmail.getText();
            String adresseModifiee = newAdresse.getText();
            String nouveauMdp = new String(newPassword.getPassword());

            if (!nomModifie.isEmpty() && !emailModifie.isEmpty()) {
                boolean modifierMdp = !nouveauMdp.isEmpty();
                Maj_client modifclient = new Maj_client();
                if (modifclient.mettreAJourClient(client.getID_personne(),nomModifie, emailModifie, adresseModifiee, modifierMdp ? nouveauMdp : null,dao)) {
                    client.setNom_client(nomModifie);
                    client.setMail_client(emailModifie);
                    client.setAdresse_postale_client(adresseModifiee);
                    if (modifierMdp) {
                        client.setMdp_client(nouveauMdp);
                    }

                    labelNom.setText("👤 Nom : " + nomModifie);
                    labelEmail.setText("📧 Email : " + emailModifie);
                    labelAdresse.setText("📍 Adresse : " + adresseModifiee);

                    JOptionPane.showMessageDialog(null, "Profil mis à jour avec succès !");
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du profil.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis !");
            }
        }
    }



    private void setLabelStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void setButtonStyle(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}
