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
