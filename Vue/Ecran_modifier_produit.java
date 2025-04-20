package Vue;

import Dao.DaoFactory;
import Dao.ProduitDAOImpl;
import modele.Produit;

import javax.swing.*;
import java.awt.*;

public class Ecran_modifier_produit extends JFrame {
    private DaoFactory daoFactory;
    private Produit produit;

    public Ecran_modifier_produit(DaoFactory daoFactory, Produit produit) {
        this.daoFactory = daoFactory;
        this.produit = produit;

        setTitle("Modifier un produit");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(10, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Champs préremplis
        JTextField txtNom = new JTextField(produit.getProduit_nom());
        JTextField txtMarque = new JTextField(produit.getProduit_marque());
        JTextField txtPrix = new JTextField(String.valueOf(produit.getProduit_prix()));
        JTextField txtPrixReduit = new JTextField(String.valueOf(produit.getPrix_reduit()));
        JTextField txtQuantite = new JTextField(String.valueOf(produit.getQuantite()));
        JTextField txtSeuilReduction = new JTextField(String.valueOf(produit.getQuantite_reduit()));
        JTextField txtDescription = new JTextField(produit.getDescription());
        JTextField txtImage = new JTextField(produit.getImage());
        JTextField txtDescriptionCourte = new JTextField(produit.getDescription_courte());

        // Labels
        panelForm.add(new JLabel("Nom du produit :"));
        panelForm.add(txtNom);
        panelForm.add(new JLabel("Marque :"));
        panelForm.add(txtMarque);
        panelForm.add(new JLabel("Prix :"));
        panelForm.add(txtPrix);
        panelForm.add(new JLabel("Prix réduit :"));
        panelForm.add(txtPrixReduit);
        panelForm.add(new JLabel("Quantité en stock :"));
        panelForm.add(txtQuantite);
        panelForm.add(new JLabel("Seuil de réduction :"));
        panelForm.add(txtSeuilReduction);
        panelForm.add(new JLabel("Description :"));
        panelForm.add(txtDescription);
        panelForm.add(new JLabel("Image (URL ou chemin) :"));
        panelForm.add(txtImage);
        panelForm.add(new JLabel("Description courte :"));
        panelForm.add(txtDescriptionCourte);

        add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelButtons = new JPanel();
        JButton btnModifier = new JButton("💾 Enregistrer");
        JButton btnAnnuler = new JButton("❌ Annuler");

        btnModifier.addActionListener(e -> {
            try {
                String nom = txtNom.getText().trim();
                String marque = txtMarque.getText().trim();
                double prix = Double.parseDouble(txtPrix.getText().trim());
                double prixReduit = Double.parseDouble(txtPrixReduit.getText().trim());
                int quantite = Integer.parseInt(txtQuantite.getText().trim());
                int seuilReduction = Integer.parseInt(txtSeuilReduction.getText().trim());
                String description = txtDescription.getText().trim();
                String image = txtImage.getText().trim();
                String descriptionCourte = txtDescriptionCourte.getText().trim();

                if (nom.isEmpty() || marque.isEmpty() || description.isEmpty() || image.isEmpty() || descriptionCourte.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                    return;
                }

                // Met à jour les champs de l'objet produit
                produit.setProduit_nom(nom);
                produit.setProduit_marque(marque);
                produit.setProduit_prix(prix);
                produit.setPrix_reduit(prixReduit);
                produit.setQuantite(quantite);
                produit.setQuantite_reduit(seuilReduction);
                produit.setDescription(description);
                produit.setImage(image);
                produit.setDescription_courte(descriptionCourte);

                // Mise à jour dans la base de données
                ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);
                produitDAO.mettreajourProduit(produit);

                JOptionPane.showMessageDialog(this, "Produit modifié avec succès !");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnAnnuler.addActionListener(e -> dispose());

        panelButtons.add(btnModifier);
        panelButtons.add(btnAnnuler);
        add(panelButtons, BorderLayout.SOUTH);

        setVisible(true);
    }
}
