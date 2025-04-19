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
