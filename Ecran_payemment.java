package Vue;

import Dao.CommandeDAOImpl;
import Dao.DaoFactory;
import Dao.ProduitDAOImpl;
import modele.Commande;

import modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Ecran_payemment extends JFrame {
    private DaoFactory daoFactory;
    private Personne client;
    private ArrayList<Commande> commandesNonPayees;

    public Ecran_payemment(DaoFactory daoFactory, ArrayList<Commande> commandesNonPayees, Personne client) {
        this.daoFactory = daoFactory;
        this.commandesNonPayees = commandesNonPayees;
        this.client = client;

        setTitle("💳 Paiement par Carte Bancaire");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNumCarte = new JLabel("Numéro de carte :");
        JTextField txtNumCarte = new JTextField();

        JLabel lblDateExp = new JLabel("Date d'expiration (MM/AA) :");
        JTextField txtDateExp = new JTextField();

        JLabel lblCVV = new JLabel("CVV :");
        JPasswordField txtCVV = new JPasswordField();

        JLabel lblNomTitulaire = new JLabel("Nom du titulaire :");
        JTextField txtNomTitulaire = new JTextField();

        panelForm.add(lblNumCarte);
        panelForm.add(txtNumCarte);
        panelForm.add(lblDateExp);
        panelForm.add(txtDateExp);
        panelForm.add(lblCVV);
        panelForm.add(txtCVV);
        panelForm.add(lblNomTitulaire);
        panelForm.add(txtNomTitulaire);

        // 🧾 Total
        double total = 0.0;
        ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);
        for (Commande cmd : commandesNonPayees) {
            Produit p = produitDAO.chercher_id(cmd.getIdProduit());
            total += calculprix(p, cmd.getQuantite());
        }

        JLabel lblTotal = new JLabel("Total : " + String.format("%.2f", total) + " €");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelForm.add(lblTotal);
        panelForm.add(new JLabel()); // vide

        add(panelForm, BorderLayout.CENTER);
         // 🔘 Boutons
        JPanel panelButtons = new JPanel();
        JButton btnPayer = new JButton("✅ Payer");
        JButton btnAnnuler = new JButton("❌ Annuler");

        btnPayer.addActionListener(e -> {
            String numCarte = txtNumCarte.getText().trim();
            String dateExp = txtDateExp.getText().trim();
            String cvv = new String(txtCVV.getPassword()).trim();
            String nom = txtNomTitulaire.getText().trim();

            if (numCarte.isEmpty() || dateExp.isEmpty() || cvv.isEmpty() || nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }
            // 🧠 Vérif numéro de carte : 16 chiffres
            if (!numCarte.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, "Le numéro de carte doit contenir 16 chiffres.");
                return;
            }

            // 📆 Vérif date d’expiration (MM/AA)
            if (!dateExp.matches("\\d{2}/\\d{2}")) {
                JOptionPane.showMessageDialog(this, "La date d'expiration doit être au format MM/AA.");
                return;
            }
            try {
                String[] dateParts = dateExp.split("/");
                int mois = Integer.parseInt(dateParts[0]);
                int annee = Integer.parseInt(dateParts[1]) + 2000; // conversion 25 -> 2025

                if (mois < 1 || mois > 12) {
                    throw new NumberFormatException();
                }

                java.util.Calendar now = java.util.Calendar.getInstance();
                int moisActuel = now.get(java.util.Calendar.MONTH) + 1;
                int anneeActuelle = now.get(java.util.Calendar.YEAR);

                if (annee < anneeActuelle || (annee == anneeActuelle && mois < moisActuel)) {
                    JOptionPane.showMessageDialog(this, "La carte est expirée.");
                    return;
                }

                if (!cvv.matches("\\d{3}")) {
                JOptionPane.showMessageDialog(this, "Le CVV doit contenir 3 chiffres.");
                return;
            }

            // 💳 Paiement simulé
            CommandeDAOImpl commandeDAO = new CommandeDAOImpl(daoFactory);
            ProduitDAOImpl produiDAO = new ProduitDAOImpl(daoFactory);
            Produit produit;
            int nouvelle_quantite;
            for (Commande cmd : commandesNonPayees) {
                commandeDAO.marquerCommePayee(cmd.getIdCommande());
                produit = produitDAO.chercher_id(cmd.getIdProduit());
                nouvelle_quantite = produit.getQuantite()-cmd.getQuantite();
                produiDAO.modifierQuantiteProduit(cmd.getIdProduit(), nouvelle_quantite);
            }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Date d'expiration invalide.");
                return;
            }
