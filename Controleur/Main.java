package Controleur;

import Dao.DaoFactory;
import Vue.Ecran_de_connexion;

public class Main
{
    public static void main(String[] args)
    {
        DaoFactory dao = DaoFactory.getInstance("amar", "root", "root");
        new Ecran_de_connexion(dao);
    }
}