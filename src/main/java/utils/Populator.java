package utils;

import entities.House;
import entities.Rental;
import entities.Tenant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class Populator {

    public static void main(String[] args) {
        //populateLocal();
        populateLocalTEST();
    }

    public static void populateLocal() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            //Hvorfor i denne her rækkefølge? Her er en nem gennemgang
            //Et hus skal findes før det kan udlejes
            //En aftale skal kunne eksistere før man kan gå med på den
            //En mand kan derfor først sætte sig som udlejer til sidst

            //Create House Entities
            House house1 = new House(1,"Valnøddevej 4", "Hornbæk", 6);
            House house2 = new House(2,"Bretagnevej 28", "Ålsgårde", 4);
            House house3 = new House(3,"Svanemøllevej 58", "Hellerup", 8);
            //Store House Entities
            em.persist(house1);
            em.persist(house2);
            em.persist(house3);
            //Create Rental Entities
            Rental rental1 = new Rental(1,"15.02.1982", "22.02.2048", 4850, (4850*3), "Morten Olsen", null, null);
            Rental rental2 = new Rental(2,"04.06.1891", "04.06.2055", 3127, (3127*3), "Susan Petersen", null, null);
            Rental rental3 = new Rental(3,"21.09.2004", "18.11.2068", 9427, (9427*3), "Helvig Kartoffelberg", null, null);
            //Set House's rental agreement
            rental1.setHouse(house1);
            rental2.setHouse(house2);
            rental3.setHouse(house3);
            //Store Rental Entities
            em.persist(rental1);
            em.persist(rental2);
            em.persist(rental3);
            //Create Rental List1
            List<Rental> rentalList1 = new ArrayList<>();
            rentalList1.add(rental1);
            //Create Rental List2
            List<Rental> rentalList2 = new ArrayList<>();
            rentalList1.add(rental2);
            //Create Rental List3
            List<Rental> rentalList3 = new ArrayList<>();
            rentalList1.add(rental3);
            //Create Tenant Entity
            Tenant tenant1 = new Tenant(1,"Ole Henriksen", 22505084, "Make-Up Manden", rentalList1);
            Tenant tenant2 = new Tenant(2,"Rodja Pjort", 84901004, "Som flyver bort", rentalList2);
            Tenant tenant3 = new Tenant(3,"Hella Joof", 28649020, "Medie-Menneske", rentalList3);
            //Store Tenants
            em.persist(tenant1);
            em.persist(tenant2);
            em.persist(tenant3);
            //Create Tenant List1
            List<Tenant> tenantList1 = new ArrayList<>();
            tenantList1.add(tenant1);
            //Create Tenant List2
            List<Tenant> tenantList2 = new ArrayList<>();
            tenantList2.add(tenant2);
            //Create Tenant List3
            List<Tenant> tenantList3 = new ArrayList<>();
            tenantList3.add(tenant3);
            //Set Tenants to a Rental Entity
            rental1.setTenants(tenantList1);
            rental2.setTenants(tenantList2);
            rental3.setTenants(tenantList3);
            //Update Tenants to Rental
            em.merge(rental1);
            em.merge(rental2);
            em.merge(rental3);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("- - - - - - - - - - - - - - -");
            System.out.println("ERROR WHEN DROPPING & RECREATING DB :(");
            System.out.println("- - - - - - - - - - - - - - -");
        } finally {
            //Save and commit changes to DB
            em.close();
            emf.close();
        }
        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("DB dropped, then created! :)");
        System.out.println("- - - - - - - - - - - - - - -");
    }

    public static void populateLocalTEST() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            //Hvorfor i denne her rækkefølge? Her er en nem gennemgang
            //Et hus skal findes før det kan udlejes
            //En aftale skal kunne eksistere før man kan gå med på den
            //En mand kan derfor først sætte sig som udlejer til sidst

            //Create House Entities
            House house1 = new House(1,"Valnøddevej 4", "Hornbæk", 6);
            House house2 = new House(2,"Bretagnevej 28", "Ålsgårde", 4);
            House house3 = new House(3,"Svanemøllevej 58", "Hellerup", 8);
            //Store House Entities
            em.persist(house1);
            em.persist(house2);
            em.persist(house3);
            //Create Rental Entities
            Rental rental1 = new Rental(1,"15.02.1982", "22.02.2048", 4850, (4850*3), "Morten Olsen", null, null);
            Rental rental2 = new Rental(2,"04.06.1891", "04.06.2055", 3127, (3127*3), "Susan Petersen", null, null);
            Rental rental3 = new Rental(3,"21.09.2004", "18.11.2068", 9427, (9427*3), "Helvig Kartoffelberg", null, null);
            //Set House's rental agreement
            rental1.setHouse(house1);
            rental2.setHouse(house2);
            rental3.setHouse(house3);
            //Store Rental Entities
            em.persist(rental1);
            em.persist(rental2);
            em.persist(rental3);
            //Create Rental List1
            List<Rental> rentalList1 = new ArrayList<>();
            rentalList1.add(rental1);
            //Create Rental List2
            List<Rental> rentalList2 = new ArrayList<>();
            rentalList1.add(rental2);
            //Create Rental List3
            List<Rental> rentalList3 = new ArrayList<>();
            rentalList1.add(rental3);
            //Create Tenant Entity
            Tenant tenant1 = new Tenant(1,"Ole Henriksen", 22505084, "Make-Up Manden", rentalList1);
            Tenant tenant2 = new Tenant(2,"Rodja Pjort", 84901004, "Som flyver bort", rentalList2);
            Tenant tenant3 = new Tenant(3,"Hella Joof", 28649020, "Medie-Menneske", rentalList3);
            //Store Tenants
            em.persist(tenant1);
            em.persist(tenant2);
            em.persist(tenant3);
            //Create Tenant List1
            List<Tenant> tenantList1 = new ArrayList<>();
            tenantList1.add(tenant1);
            //Create Tenant List2
            List<Tenant> tenantList2 = new ArrayList<>();
            tenantList2.add(tenant2);
            //Create Tenant List3
            List<Tenant> tenantList3 = new ArrayList<>();
            tenantList3.add(tenant3);
            //Set Tenants to a Rental Entity
            rental1.setTenants(tenantList1);
            rental2.setTenants(tenantList2);
            rental3.setTenants(tenantList3);
            //Update Tenants to Rental
            em.merge(rental1);
            em.merge(rental2);
            em.merge(rental3);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("- - - - - - - - - - - - - - -");
            System.out.println("ERROR WHEN DROPPING & RECREATING TEST DB :(");
            System.out.println("- - - - - - - - - - - - - - -");
        } finally {
            //Save and commit changes to DB
            em.close();
            emf.close();
        }

        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("TEST DB dropped, then created! :)");
        System.out.println("- - - - - - - - - - - - - - -");
    }
}
