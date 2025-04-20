package Vue;

import Dao.CommandeDAOImpl;
import Dao.DaoFactory;
import modele.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

public class Ecran_principal extends JFrame {
    private JTextField barreRecherche;
    private JPanel panelProduits;
    private JScrollPane scrollPaneProduits;
    private Affichage affichageProduits;
    private DaoFactory daoFactory;
    private  Personne client;

    public Ecran_principal(Personne client, DaoFactory dao) {
        this.daoFactory = dao;
        this.affichageProduits = new Affichage(dao);
        this.client = client;
        // Configuration de la fenêtre
        setTitle("Accueil");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Barre de recherche
        JPanel panelRechercheContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelRechercheContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        barreRecherche = new JTextField(20);
        JButton boutonRecherche = new JButton("🔍");

        boutonRecherche.addActionListener(e -> {
            String recherche = barreRecherche.getText().trim();
            if (!recherche.isEmpty()) {
                ArrayList<Produit> resultats = affichageProduits.affichage_produit_cherhcer(recherche);
                afficherProduits(resultats);
            } else {
                afficherProduits(affichageProduits.affichage_produit_accueil());
            }
        });

        panelRechercheContainer.add(barreRecherche);
        panelRechercheContainer.add(boutonRecherche);

        // 🔹 Menu de navigation
        JPanel panelMenuContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelMenu = new JPanel(new GridLayout(1, 0, 10, 10));
        String[] categories = {"🏠 Accueil", "Pokemon", "🐉 Légendaire", "🎒 Objet", "🛒 Panier", "Mon Compte"};

        for (String cat : categories) {
            JButton bouton = new JButton(cat);
            bouton.addActionListener(new MenuListener(cat, client));
            panelMenu.add(bouton);
        }

        panelMenuContainer.add(panelMenu);

        // 🔹 Zone produits
        panelProduits = new JPanel();
        panelProduits.setLayout(new GridLayout(0, 3, 20, 20));
        panelProduits.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        afficherProduits(affichageProduits.affichage_produit_accueil());

        scrollPaneProduits = new JScrollPane(panelProduits);
        scrollPaneProduits.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneProduits.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 🔹 Filtres à gauche
        JPanel panelFiltres = new JPanel();
        panelFiltres.setLayout(new BoxLayout(panelFiltres, BoxLayout.Y_AXIS));
        panelFiltres.setBorder(BorderFactory.createTitledBorder("Filtres"));

        JLabel labelPrix = new JLabel("💰 Filtrer par prix maximal");
        labelPrix.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFiltres.add(labelPrix);

// Valeurs logarithmiques filtrées
        int[] puissances = {0, 2, 3, 4, 5, 6, 9};
        JSlider sliderPrix = new JSlider(0, puissances.length - 1, puissances.length - 1);
        sliderPrix.setMajorTickSpacing(1);
        sliderPrix.setPaintTicks(true);
        sliderPrix.setPaintLabels(true);
        sliderPrix.setAlignmentX(Component.CENTER_ALIGNMENT);

// Création des labels
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("1"));
        labelTable.put(1, new JLabel("100"));
        labelTable.put(2, new JLabel("1k"));
        labelTable.put(3, new JLabel("10k"));
        labelTable.put(4, new JLabel("100k"));
        labelTable.put(5, new JLabel("1M"));
        labelTable.put(6, new JLabel("1Md"));
        sliderPrix.setLabelTable(labelTable);

        panelFiltres.add(sliderPrix);

        JLabel valeurPrix = new JLabel("Prix max : 1 000 000 000 €");
        valeurPrix.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFiltres.add(valeurPrix);
        // 🔹 Image selon la gamme de prix
        JLabel imagePrix = new JLabel();
        imagePrix.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon("C:\\Users\\alexi\\Java\\Projet_v1\\images\\Saint_bidoof.png");
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imagePrix.setIcon(new ImageIcon(img));
        panelFiltres.add(Box.createVerticalStrut(10)); // un petit espace visuel
        panelFiltres.add(imagePrix);

        // Méthode pour changer l'image
        Runnable updateImage = () -> {
            int index = sliderPrix.getValue();
            int prixMax = (int) Math.pow(10, puissances[index]);

            String cheminImage = "null";

            if (prixMax <= 1000) {
                cheminImage = "C:\\Users\\alexi\\Java\\Projet_v1\\images\\Bidoof_enerve.png";
            } else if (prixMax <= 100_000) {
                cheminImage = "C:\\Users\\alexi\\Java\\Projet_v1\\images\\bidoof_heureux.png";
            } else if (prixMax <= 100_000_000) {
                cheminImage = "C:\\Users\\alexi\\Java\\Projet_v1\\images\\bidoof_malheureux.png";
            } else {
                cheminImage = "C:\\Users\\alexi\\Java\\Projet_v1\\images\\Saint_bidoof.png";
            }

            ImageIcon icon2 = new ImageIcon(cheminImage);
            Image img2 = icon2.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imagePrix.setIcon(new ImageIcon(img2));
        };
// 🔹 Filtrer par note moyenne
        JLabel labelNote = new JLabel("⭐ Filtrer par note minimale");
        labelNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFiltres.add(Box.createVerticalStrut(20));
        panelFiltres.add(labelNote);

        JSlider sliderNote = new JSlider(0, 5, 0); // notes de 0 à 5
        sliderNote.setMajorTickSpacing(1);
        sliderNote.setPaintTicks(true);
        sliderNote.setPaintLabels(true);
        sliderNote.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Labels personnalisés
        Hashtable<Integer, JLabel> noteLabels = new Hashtable<>();
        for (int i = 0; i <= 5; i++) {
            noteLabels.put(i, new JLabel(i + "★"));
        }
        sliderNote.setLabelTable(noteLabels);
        panelFiltres.add(sliderNote);

        JLabel valeurNote = new JLabel("Note min : 0★");
        valeurNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFiltres.add(valeurNote);





// Listener pour le filtrage
        sliderPrix.addChangeListener(e -> {
                int indexPrix = sliderPrix.getValue();
                int prixMax = (int) Math.pow(10, puissances[indexPrix]);
                int noteMin = sliderNote.getValue();

                valeurPrix.setText("Prix max : " + String.format("%,d", prixMax).replace(",", " ") + " €");
                valeurNote.setText("Note min : " + noteMin + "★");

                filtrerProduits(prixMax, noteMin);
                updateImage.run();
            });


        sliderNote.addChangeListener(e -> {
            int indexPrix = sliderPrix.getValue();
            int prixMax = (int) Math.pow(10, puissances[indexPrix]);
            int noteMin = sliderNote.getValue();

            valeurPrix.setText("Prix max : " + String.format("%,d", prixMax).replace(",", " ") + " €");
            valeurNote.setText("Note min : " + noteMin + "★");

            filtrerProduits(prixMax, noteMin);
        });






        // 🔹 Assemblage final
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(panelRechercheContainer, BorderLayout.NORTH);
        topPanel.add(panelMenuContainer, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPaneProduits, BorderLayout.CENTER);
        add(panelFiltres, BorderLayout.WEST);

        revalidate();
        repaint();
        setVisible(true);
    }

    private void filtrerProduits(int prixMax, int noteMin) {
        ArrayList<Produit> tousProduits = affichageProduits.affichage_produit_accueil();
        ArrayList<Produit> filtres = new ArrayList<>();
        CommandeDAOImpl cdm = new CommandeDAOImpl(daoFactory);

        for (Produit p : tousProduits) {
            double moyenne = cdm.calcul_moyenne_note(p.getId_produit());
            if (p.getProduit_prix() <= prixMax && moyenne >= noteMin) {
                filtres.add(p);
            }
        }
        afficherProduits(filtres);
    }


    private void afficherProduits(ArrayList<Produit> produits) {
        panelProduits.removeAll();
        CommandeDAOImpl cmd=new CommandeDAOImpl(daoFactory);

        if (produits.isEmpty()) {
            JLabel aucunProduit = new JLabel("Désolé aucun produit trouvé", SwingConstants.CENTER);
            aucunProduit.setFont(new Font("Arial", Font.BOLD, 18));
            panelProduits.setLayout(new BorderLayout());
            panelProduits.add(aucunProduit, BorderLayout.CENTER);
        } else {
            panelProduits.setLayout(new GridLayout(0, 3, 20, 20));

            for (Produit p : produits) {
                JPanel carte = new JPanel();
                carte.setLayout(new BorderLayout());
                carte.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                // Image
                String cheminImage = p.getImage().replace("\\", "/");
                ImageIcon imageProduit = new ImageIcon(cheminImage);
                if (imageProduit.getIconWidth() == -1) {
                    imageProduit = new ImageIcon("C:\\Users\\alexi\\Java\\Projet_v1\\images\\zarbi.png");
                }

                Image imageRedim = imageProduit.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel labelImage = new JLabel(new ImageIcon(imageRedim));
                carte.add(labelImage, BorderLayout.NORTH);

                // Infos
                JPanel infos = new JPanel();
                infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
                infos.add(new JLabel("🧾 " + p.getProduit_nom()));
                infos.add(new JLabel("🏷️ " + p.getProduit_marque()));
                infos.add(new JLabel("💰 " + p.getProduit_prix() + " €"));

                if (p.getPrix_reduit() > 0)
                    infos.add(new JLabel("🔥 Promo : " + p.getPrix_reduit() + " €"));

                infos.add(new JLabel("📦 Qté : " + p.getQuantite()));
                if (p.getQuantite_reduit() > 0)
                    infos.add(new JLabel("🎯 Qté Promo : " + p.getQuantite_reduit()));

                JLabel labelDescription = new JLabel("<html><div style='width:200px;'>" + p.getDescription() + "</div> </br></html>");
                infos.add(labelDescription);
                infos.add(new JLabel("⭐ " + cmd.calcul_moyenne_note(p.getId_produit())));

                JButton btnCommander = new JButton("🛒 Commander");
                btnCommander.addActionListener(e -> {
                    CommandeDAOImpl commande_systeme=new CommandeDAOImpl(daoFactory);
                    Commande commande = new Commande(1,p.getId_produit(),client.getID_personne(), client.getAdresse_postale_client(), 1,false,5);
                    commande_systeme.ajouter(commande);
                    JOptionPane.showMessageDialog(null,
                            "Produit \"" + p.getProduit_nom() + "\" ajouté au panier !",
                            "Commande",
                            JOptionPane.INFORMATION_MESSAGE);
                });
                infos.add(btnCommander);

                carte.add(infos, BorderLayout.CENTER);
                panelProduits.add(carte);
            }
        }

        panelProduits.revalidate();
        panelProduits.repaint();
    }


    private class MenuListener implements ActionListener {
        private String category;
        private Personne client;

        public MenuListener(String category, Personne client) {
            this.category = category;
            this.client = client;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Bouton cliqué : " + category);
            //JOptionPane.showMessageDialog(null, "Vous avez cliqué sur : " + category);
            if (category.equals("Mon Compte")) {
                dispose();
                new Ecran_profil_client(client,daoFactory);
            }
            else if(category.equals("🏠 Accueil")){
                dispose();
                new Ecran_principal(client,daoFactory);
            }
            else if(category.equals("Pokemon")){
                afficherProduits(affichageProduits.affichage_produit_cate("Pokemon"));
            }
            else if(category.equals("🐉 Légendaire")){
                afficherProduits(affichageProduits.affichage_produit_cate("Legendaire"));
            }
            else if(category.equals("🎒 Objet")){
                afficherProduits(affichageProduits.affichage_produit_cate("Objet"));
            }
            else if(category.equals("🛒 Panier")){
                dispose();
                new Ecran_panier_client(client,daoFactory);
            }
        }
    }
}
