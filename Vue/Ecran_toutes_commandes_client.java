package Vue;

import Dao.CommandeDAOImpl;
import Dao.DaoFactory;
import Dao.ProduitDAOImpl;
import modele.Commande;
import modele.Personne;
import modele.Produit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Ecran_toutes_commandes_client extends JFrame {
    DaoFactory dao;
    Personne client;

    public Ecran_toutes_commandes_client(Personne client, DaoFactory daoFactory) {
        this.client = client;
        this.dao = daoFactory;

        setTitle("📋 Historique des Commandes Payées");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        CommandeDAOImpl commandeDAO = new CommandeDAOImpl(daoFactory);
        ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);

        ArrayList<Commande> commandes = commandeDAO.chercherCommandesPayeesParClient(client.getID_personne());

        JPanel panelCommandes = new JPanel();
        panelCommandes.setLayout(new BoxLayout(panelCommandes, BoxLayout.Y_AXIS));
        panelCommandes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (commandes.isEmpty()) {
            JLabel labelVide = new JLabel("Aucune commande payée trouvée.");
            labelVide.setFont(new Font("Arial", Font.BOLD, 18));
            labelVide.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCommandes.add(labelVide);
        } else {
            for (Commande cmd : commandes) {
                Produit produit = produitDAO.chercher_id(cmd.getIdProduit());
                JPanel carteCommande = new JPanel();
                carteCommande.setLayout(new BoxLayout(carteCommande, BoxLayout.Y_AXIS));
                carteCommande.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                carteCommande.setBackground(Color.WHITE);

                JLabel label = new JLabel(
                        "<html><b>Commande n°" + cmd.getIdCommande() + "</b><br>" +
                                "Produit : " + produit.getProduit_nom() + "<br>" +
                                "Quantité : " + cmd.getQuantite() + "<br>" +
                                "Prix unitaire : " + produit.getProduit_prix() + " €<br>" +
                                "Adresse de livraison : " + cmd.getLieuLivraison() +
                                "Note :"+ cmd.getNote()+"⭐"+
                                "</html>"
                );
                label.setFont(new Font("SansSerif", Font.PLAIN, 14));
                carteCommande.add(label);

                // 🆕 Bouton pour noter le produit
                JButton btnNoter = new JButton("⭐ Noter ce produit");
                btnNoter.addActionListener(e -> {
                    String noteStr = JOptionPane.showInputDialog(
                            Ecran_toutes_commandes_client.this,
                            "Attribuez une note à ce produit (1 à 5) :",
                            "Noter le produit",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (noteStr != null) {
                        try {
                            int note = Integer.parseInt(noteStr.trim());
                            if (note >= 1 && note <= 5) {
                                // TODO : Enregistrer la note dans la base de données
                                JOptionPane.showMessageDialog(
                                        Ecran_toutes_commandes_client.this,
                                        "Merci pour votre note de " + note + " ⭐ pour le produit \"" + produit.getProduit_nom() + "\" !");
                                commandeDAO.mettreajournote(note,cmd);
                                dispose();
                                new Ecran_toutes_commandes_client(client,daoFactory);
                            } else {
                                JOptionPane.showMessageDialog(
                                        Ecran_toutes_commandes_client.this,
                                        "Veuillez entrer une note entre 1 et 5.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(
                                    Ecran_toutes_commandes_client.this,
                                    "Veuillez entrer un nombre valide.");
                        }
                    }
                });

                carteCommande.add(Box.createVerticalStrut(5));
                carteCommande.add(btnNoter);

                panelCommandes.add(carteCommande);
                panelCommandes.add(Box.createVerticalStrut(10));
            }

        }

        JScrollPane scrollPane = new JScrollPane(panelCommandes);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton de retour
        JButton btnRetour = new JButton("⬅ Retour");
        btnRetour.addActionListener(e -> dispose());
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);

        setVisible(true);
    }
}
