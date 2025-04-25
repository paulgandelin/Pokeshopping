package Controleur;

import Dao.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Vue.message_erreur;
import Vue.message_reussite;
import Vue.pop_up_interrup;
import modele.Personne;

import static java.lang.Boolean.FALSE;

public class Gestion_role extends JFrame {
    message_erreur message_e;
    message_reussite message_r;
    pop_up_interrup pop;
    SQL_securite mail;
    PersonneDAOImpl pdao;
    public void ouvrirFenetreGestionRoles(DaoFactory daoFactory) {
        pdao=new PersonneDAOImpl(daoFactory);
        pop=new pop_up_interrup();
        message_e=new message_erreur();
        message_r=new message_reussite();
        String emailCible = pop.email_gestion_role();
        Personne client=null;


        if (emailCible == null || emailCible.trim().isEmpty() || mail.valid_mail(emailCible)==FALSE) {
            return;
        }
        else {
            client=pdao.chercher_adresse_mail(emailCible);

                if (client!=null) {
                    int id = client.getID_personne();
                    String nom = client.getNom_client();
                    boolean estAdmin = client.getIdentifiant_admin();

                    int choix=pop.gestion_role(estAdmin,nom,emailCible);

                    if (choix == 0) {
                        int nouveauRole = estAdmin ? 0 : 1;
                        int updated=pdao.modifier_admin_nombre(id,nouveauRole);
                        if (updated > 0) {
                            message_r.message_gestion_role_ok();
                        } else {
                            message_e.erreur_gestion_role();
                        }
                    }
                } else {
                    message_e.adresse_mail_pas_dans_bdd();
                }
            }

        }

    }
