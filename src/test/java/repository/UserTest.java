package repository;

import entities.House;
import entities.Rental;
import entities.Tenant;
import utils.EMF_Creator;

import javax.persistence.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Populator;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class UserTest {
    private static EntityManagerFactory emf;
    private static UserRepo userREPO;

    private House demoHouse;
    private Rental demoRental;
    private Tenant demoTenant;
    private List<Tenant> demoTenantList = new ArrayList<>();



    public UserTest() {

    }

    @BeforeAll
    public static void setupClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        userREPO = UserRepo.getUserRepo(emf);
    }

    //TODO: Is unnecessary, remove later
    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    @BeforeEach
    public void setup() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Populator.populateLocalTEST();
        } finally {
            em.close();
        }
    }

    //Not needed with drop-and-create
//    @AfterEach
//    public void tearDown() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//
//            Query q1 = em.createQuery("DELETE FROM House");
//            Query q2 = em.createQuery("DELETE FROM Rental ");
//            Query q3 = em.createQuery("DELETE FROM Tenant");
//            q1.executeUpdate();
//            q2.executeUpdate();
//            q3.executeUpdate();
//
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }

    //TODO: As a user, I would like to see all my rental agreements
    @Test
    public void seeAllAgreementsTest() throws Exception {
        List<Rental> expectedRentalList = new ArrayList<>();

        demoHouse = new House(1, "Valnøddevej 4", "Hornbæk",6);
        demoRental = new Rental(1,"15.02.1982", "22.02.2048", 4850, 14550, "Morten Olsen",null, demoHouse);
        demoTenant =  new Tenant(1, "Ole Henriksen", 22505084, "Make-Up Manden", expectedRentalList);
        demoTenantList.add(demoTenant);
        demoRental.setTenants(demoTenantList);

        expectedRentalList.add(demoRental);

        //Testing with Tenant with ID 1
        List<Rental> actualRentalList = userREPO.seeAllTenantsRentals(1);
        System.out.println(expectedRentalList.get(0).getHouse().getAddress());
        System.out.println(actualRentalList.get(0).getHouse().getAddress());

        assertEquals(expectedRentalList.equals(actualRentalList), actualRentalList.equals(expectedRentalList));
    }

}
