package Vue;

import Controleur.Affichage_graph;
import Dao.DaoFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Ecran_Statistiques_Admin extends JFrame {
    private DaoFactory daoFactory;

    public Ecran_Statistiques_Admin(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        Affichage_graph vision =new Affichage_graph(daoFactory);
        setTitle("Statistiques de Vente");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("📊 Statistiques globales", creerPanelStatsGlobales(vision));
        tabbedPane.addTab("🏆 Top Ventes", creerHistogrammeTopVentes(vision));
        tabbedPane.addTab("🛒 Comportement d'achat", creerCamembertComportement(vision));
        tabbedPane.addTab("🧍 Produits", creerCamembertTypesProduits(vision));

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel creerPanelStatsGlobales(Affichage_graph vision) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Ces données doivent venir de DAO (à implémenter)
        int totalProduitsVendus = vision.totalProduitsVendus();
        double totalChiffreAffaires = vision.CAProduitsVendus();
        int nombreClients = vision.nb_client();
        int commandesEffectuees = vision.nb_commande();

        panel.add(new JLabel("📦 Total produits vendus : " + totalProduitsVendus));
        panel.add(new JLabel("💶 Chiffre d'affaires total : " + String.format("%.2f €", totalChiffreAffaires)));
        panel.add(new JLabel("👥 Nombre de clients : " + nombreClients));
        panel.add(new JLabel("🧾 Commandes effectuées : " + commandesEffectuees));

        return panel;
    }

    private JPanel creerHistogrammeTopVentes(Affichage_graph vision) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        message_erreur messageErreur = new message_erreur();
        Map<String, Integer> ventes = vision.recupererVentesParProduit();

        // Si aucune donnée n'est récupérée, afficher un message
        if (ventes.isEmpty()) {
            messageErreur.abs_donnee();
        }

        // Ajouter les données au dataset
        for (Map.Entry<String, Integer> entry : ventes.entrySet()) {
            dataset.addValue(entry.getValue(), "Ventes", entry.getKey());
        }

        // Créer le graphique
        JFreeChart chart = ChartFactory.createBarChart(
                "Top Produits Vendus",    // Titre
                "Produit",                // Axe des X
                "Quantité Vendue",        // Axe des Y
                dataset,                  // Données
                PlotOrientation.VERTICAL, // Orientation du graphique
                false,                    // Inclure la légende
                true,                     // Inclure les infos dans les ToolTips
                false                     // Inclure les URLs
        );

        // Créer et ajouter le chartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);

        // Retourner le JPanel contenant le graphique
        return panel;
    }



    private JPanel creerCamembertComportement(Affichage_graph vision) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Integer> comportement = vision.getComportementClients(); // appel DAO

        for (Map.Entry<String, Integer> entry : comportement.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Répartition des comportements d'achat",
                dataset,
                true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));

        ChartPanel chartPanel = new ChartPanel(chart);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }


    private JPanel creerCamembertTypesProduits(Affichage_graph vision) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> data = vision.recupererTypeProduitsVendus();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Répartition des types de produits achetés",
                dataset,
                true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));

        ChartPanel chartPanel = new ChartPanel(chart);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

}
