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
