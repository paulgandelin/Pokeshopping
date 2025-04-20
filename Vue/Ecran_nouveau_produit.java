package Vue;

import Dao.DaoFactory;
import Dao.ProduitDAOImpl;
import modele.Produit;

import javax.swing.*;
import java.awt.*;

public class Ecran_nouveau_produit extends JFrame {
    private DaoFactory daoFactory;

    public Ecran_nouveau_produit(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;

        setTitle("Ajouter un nouveau produit");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(10, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Champs
        JTextField txtNom = new JTextField();
        JTextField txtMarque = new JTextField();
        JTextField txtPrix = new JTextField();
        JTextField txtPrixReduit = new JTextField();
        JTextField txtQuantite = new JTextField();
        JTextField txtSeuilReduction = new JTextField();
        JTextField txtDescription = new JTextField();
        JTextField txtImage = new JTextField();
        JTextField txtDescriptionCourte = new JTextField();

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
        JButton btnAjouter = new JButton("✅ Ajouter");
        JButton btnAnnuler = new JButton("❌ Annuler");

        btnAjouter.addActionListener(e -> {
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

                Produit produit = new Produit(0,nom,marque,prix,prixReduit,quantite,seuilReduction,description,image,descriptionCourte);

                ProduitDAOImpl produitDAO = new ProduitDAOImpl(daoFactory);
                produitDAO.ajouter(produit);

                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès !");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnAnnuler.addActionListener(e -> dispose());

        panelButtons.add(btnAjouter);
        panelButtons.add(btnAnnuler);
        add(panelButtons, BorderLayout.SOUTH);

        setVisible(true);
    }
}
