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
