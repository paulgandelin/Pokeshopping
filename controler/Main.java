package controler;

import Dao.DaoFactory;
import Vue.*;

public class Main
{
    public static void main(String[] args)
    {
        DaoFactory dao = DaoFactory.getInstance("amar", "root", "root");
        new Ecran_de_connexion(dao);
    }
}