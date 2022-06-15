package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class Populator {
    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            //Hvorfor i denne her rækkefølge? Her er en nem gennemgang
            //En mand skal have et sted at have sin båd, derfor findes havnen først
            //En mand skal eje en båd, derfor skal båden eksistere først
            //En mand kan derfor sætte sig selv som ejer til sidst


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("All is not good :(");
        } finally {
            //Save and commit changes to DB
            em.getTransaction().commit();
            em.close();
            emf.close();
        }

        System.out.println("all is good:)");
    }
}
