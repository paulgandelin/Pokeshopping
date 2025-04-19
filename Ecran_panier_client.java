package Vue;

import Dao.CommandeDAOImpl;
import Dao.DaoFactory;
import Dao.ProduitDAOImpl;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Ecran_panier_client extends JFrame {
    private DaoFactory daoFactory;
    private Personne client;

    public Ecran_panier_client(Personne client, DaoFactory dao) {
        this.daoFactory = dao;
        this.client = client;

        setTitle("🛒 Mon Panier");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        CommandeDAOImpl commandeDAO = new CommandeDAOImpl(daoFactory);
        ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);

        ArrayList<Commande> commandesNonPayees = commandeDAO.chercherCommandesNonPayeesParClient(client.getID_personne());

        // 🔝 Haut : bouton retour
        JButton btnRetour = new JButton("⬅ Retour");
        btnRetour.addActionListener(e -> {
            dispose();
            new Ecran_principal(client, daoFactory);
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnRetour);
        add(topPanel, BorderLayout.NORTH);

        // 🧾 Centre : commandes
        JPanel panelCommandes = new JPanel();
        panelCommandes.setLayout(new BoxLayout(panelCommandes, BoxLayout.Y_AXIS));

        double total = 0.0;
        String adresseLivraison = commandesNonPayees.isEmpty() ? "N/A" : commandesNonPayees.get(0).getLieuLivraison();

        if (commandesNonPayees.isEmpty()) {
            JLabel aucun = new JLabel("Aucune commande en attente de paiement.");
            aucun.setFont(new Font("Arial", Font.BOLD, 18));
            aucun.setHorizontalAlignment(SwingConstants.CENTER);
            panelCommandes.add(aucun);
        } else {
            for (Commande cmd : commandesNonPayees) {
                Produit produit = produitDAO.chercher_id(cmd.getIdProduit());
                double prixCommande = calculprix(produit,cmd.getQuantite());
                total += prixCommande;

                JPanel carteCommande = new JPanel(new BorderLayout());
                carteCommande.setBorder(BorderFactory.createTitledBorder("Commande #" + cmd.getIdCommande()));
                carteCommande.setPreferredSize(new Dimension(550, 150));

                JLabel infos = new JLabel(
                        "<html><b>Produit :</b> " + produit.getProduit_nom() +
                                "<br><b>Prix unitaire :</b> " + produit.getProduit_prix() + " €" +
                                "<br><b>Quantité :</b> " + cmd.getQuantite() +
                                "<br><b>Total :</b> " + prixCommande + " €" +
                                "</html>"
                );
                infos.setFont(new Font("SansSerif", Font.PLAIN, 14));
                carteCommande.add(infos, BorderLayout.CENTER);
                // ➕ Modifier quantité
                JButton btnModifier = new JButton("✏ Modifier");
                btnModifier.addActionListener(e -> {
                    String nouvelleQuantiteStr = JOptionPane.showInputDialog(
                            this,
                            "Nouvelle quantité pour " + produit.getProduit_nom(),
                            cmd.getQuantite()
                    );

                    try {
                        if (nouvelleQuantiteStr != null) {
                            int nouvelleQuantite = Integer.parseInt(nouvelleQuantiteStr);
                            if (nouvelleQuantite > 0) {
                                int stockDisponible = produit.getQuantite();
                                if (nouvelleQuantite <= stockDisponible) {
                                    cmd.setQuantite(nouvelleQuantite);
                                    commandeDAO.modifierQuantite(cmd.getIdCommande(), nouvelleQuantite);
                                    JOptionPane.showMessageDialog(this, "Quantité mise à jour !");
                                    dispose();
                                    new Ecran_panier_client(client, daoFactory); // refresh écran
                                } else {
                                    JOptionPane.showMessageDialog(this,
                                            "Stock insuffisant. Il reste seulement " + stockDisponible + " unité(s) en stock.",
                                            "Erreur de stock",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "La quantité doit être supérieure à 0.");
                            }

                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Quantité invalide.");
                    }
                });

                // 🗑 Supprimer commande
                JButton btnSupprimer = new JButton("🗑 Supprimer");
                btnSupprimer.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Voulez-vous vraiment supprimer cette commande ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        commandeDAO.supprimer(cmd);
                        JOptionPane.showMessageDialog(this, "Commande supprimée.");
                        dispose();
                        new Ecran_panier_client(client, daoFactory); // refresh
                    }

                               // Panel boutons à droite
                JPanel panelBoutons = new JPanel();
                panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));
                panelBoutons.add(btnModifier);
                panelBoutons.add(Box.createVerticalStrut(10));
                panelBoutons.add(btnSupprimer);

                carteCommande.add(panelBoutons, BorderLayout.EAST);

                panelCommandes.add(carteCommande);
                panelCommandes.add(Box.createVerticalStrut(10));
            }
        }
                });
        JScrollPane scrollPane = new JScrollPane(panelCommandes);
        add(scrollPane, BorderLayout.CENTER);

        // 🔻 Bas : total + bouton de paiement + adresse
        JPanel basPanel = new JPanel();
        basPanel.setLayout(new BoxLayout(basPanel, BoxLayout.Y_AXIS));
        basPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblAdresse = new JLabel("📦 Livraison à : " + adresseLivraison);
        lblAdresse.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAdresse.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTotal = new JLabel("💰 Total à payer : " + String.format("%.2f", total) + " €");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnPayerTout = new JButton("💳 Payer toutes les commandes");
        btnPayerTout.setFont(new Font("Arial", Font.BOLD, 16));
        btnPayerTout.setAlignmentX(Component.CENTER_ALIGNMENT);
        // ✏ Bouton modifier l'adresse
        JButton btnModifierAdresse = new JButton("✏ Modifier l'adresse de livraison");
        btnModifierAdresse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModifierAdresse.addActionListener(e -> {
            String nouvelleAdresse = JOptionPane.showInputDialog(
                    this,
                    "Nouvelle adresse de livraison :",
                    adresseLivraison
            );

            if (nouvelleAdresse != null && !nouvelleAdresse.trim().isEmpty()) {
                for (Commande cmd : commandesNonPayees) {
                    cmd.setLieuLivraison(nouvelleAdresse.trim());
                    commandeDAO.modifierAdresse(cmd.getIdCommande(), nouvelleAdresse.trim()); // 🔧 À implémenter
                }
                JOptionPane.showMessageDialog(this, "Adresse mise à jour !");
                dispose();
                new Ecran_panier_client(client, daoFactory);
            }
        });
        btnPayerTout.addActionListener(e -> {
            for (Commande cmd : commandesNonPayees) {
                commandeDAO.marquerCommePayee(cmd.getIdCommande());
            }
            JOptionPane.showMessageDialog(this, "Toutes les commandes ont été payées !");
            new Ecran_payemment( daoFactory,commandesNonPayees,client);
        });
        basPanel.add(lblAdresse);
        basPanel.add(Box.createVerticalStrut(5));
        basPanel.add(btnModifierAdresse);
        basPanel.add(lblAdresse);
        basPanel.add(Box.createVerticalStrut(10));
        basPanel.add(lblTotal);
        basPanel.add(Box.createVerticalStrut(10));
        basPanel.add(btnPayerTout);

        add(basPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private double calculprix(Produit produit, int quantite) {
        int seuil = produit.getQuantite_reduit();
        double prixReduit = produit.getPrix_reduit();
        double prixNormal = produit.getProduit_prix();
        double prix=0;
        if (seuil==0) {
            prix =quantite*prixNormal;
        }
        else{
            int packsReduits = quantite / seuil;
            int resteNormal = quantite % seuil;
            prix = (packsReduits *seuil* prixReduit) + (resteNormal * prixNormal);
        }
        return prix;
    }

}

