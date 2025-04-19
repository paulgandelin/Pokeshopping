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
                });
