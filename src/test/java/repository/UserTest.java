package repository;

import entities.House;
import entities.Rental;
import entities.Tenant;
import utils.EMF_Creator;

import javax.enterprise.inject.Typed;
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
    private House demoHouse2;
    private Rental demoRental;
    private Tenant demoTenant;
    private List<Tenant> demoTenantList;
    private List<Rental> demoRentalList;



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

        demoTenantList = new ArrayList<>();
        demoRentalList = new ArrayList<>();

        demoHouse = new House(1, "Valnøddevej 4", "Hornbæk",6);
        demoHouse2 = new House(2, "Bretagnevej 28", "Ålsgårde", 4);
        demoRental = new Rental(1,"15.02.1982", "22.02.2048", 4850, 14550, "Morten Olsen",null, demoHouse);
        demoRentalList.add(demoRental);
        demoTenant =  new Tenant(1, "Ole Henriksen", 22505084, "Make-Up Manden", demoRentalList);
        demoTenantList.add(demoTenant);
        demoRental.setTenants(demoTenantList);

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

    //TODO: US-1 As a user, I would like to see all my rental agreements
    @Test
    public void seeAllAgreementsTest() throws Exception {
        List<Rental> expectedRentalList = demoRentalList;

        expectedRentalList.add(demoRental);

        //Testing with Tenant with ID 1
        List<Rental> actualRentalList = userREPO.seeAllTenantsRentals(1);
        System.out.println(expectedRentalList.get(0).getHouse().getAddress());
        System.out.println(actualRentalList.get(0).getHouse().getAddress());

        assertEquals(expectedRentalList.equals(actualRentalList), actualRentalList.equals(expectedRentalList));
    }

    //TODO: US-2 As a user, I would like to click on a rental agreement and see all details about the house
    //Process:
    //1. User Clicks on Rental Agreement
    //2. Click gets the Rental Agreement's ID
    //3. Parse ID into method
    //4. Retrieve House Details via said, ID
    //5. ???
    //6. Profit.
    @Test
    public void seeDetailsAboutHouseFromAgreement() throws Exception {
        House expectedHouse = demoHouse2;
        House actualHouse = userREPO.seeDetailsAboutHouseFromRentalID(2);

        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("Expected House Details:");
        System.out.println("ID: " +expectedHouse.getId());
        System.out.println("Address: " +expectedHouse.getAddress());
        System.out.println("City: " +expectedHouse.getCity());
        System.out.println("Number of Rooms: " +expectedHouse.getNumberOfRooms());
        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("Actual House Details:");
        System.out.println("ID: " +actualHouse.getId());
        System.out.println("Address: " +actualHouse.getAddress());
        System.out.println("City: " +actualHouse.getCity());
        System.out.println("Number of Rooms: " +actualHouse.getNumberOfRooms());
        System.out.println("- - - - - - - - - - - - - - -");
        assertEquals(expectedHouse.equals(actualHouse), actualHouse.equals(expectedHouse));
    }

    //TODO: US-3 As an admin, I would like to see all tenants currently living in a particular house
    //Process
    //1. Admin clicks on house
    //2. Click registers house's ID
    //3. Use House ID to find Rental Entity
    //4. Use Rental Entity's ID to see all Tenant IDs that is on Tenant_Rental table
    @Test
    public void seeTenantsLivingInHouse() throws Exception{
        List<Tenant> expectedTenants = demoTenantList;

        EntityManager em = emf.createEntityManager();
        List<Tenant> actualTenants;
        int givenId = 1;

        try {
            House tenantsHouse = em.find(House.class, givenId);
            TypedQuery<Rental> rentalTQ = em.createQuery("SELECT r FROM Rental r WHERE r.house.id is not null and r.house.id = :givenHouseId", Rental.class);
            rentalTQ.setParameter("givenHouseId", tenantsHouse.getId());
            Rental tenantsRental = rentalTQ.getSingleResult();
            TypedQuery<Tenant> tenantTQ = em.createQuery("SELECT DISTINCT t FROM Tenant t join t.rentals tr where tr.id =:tenantsRentalId", Tenant.class);
            tenantTQ.setParameter("tenantsRentalId", tenantsRental.getId());
            actualTenants = tenantTQ.getResultList();
        } finally {
            em.close();
        }

        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("Expected Tenants List:");
        System.out.println("List Size: "+ expectedTenants.size());
        for (Tenant expectedTenant : expectedTenants) {
            System.out.println("ID: " + expectedTenant.getId());
            System.out.println("Name: " + expectedTenant.getName());
            System.out.println("PhoneNum: " + expectedTenant.getPhoneNum());
            System.out.println("Job : " + expectedTenant.getJob());
        }
        System.out.println("- - - - - - - - - - - - - - -");
        System.out.println("Actual Tenants List:");
        System.out.println("List Size: "+ actualTenants.size());
        for (Tenant actualTenant : actualTenants) {
            System.out.println("ID: " + actualTenant.getId());
            System.out.println("Name: " + actualTenant.getName());
            System.out.println("PhoneNum: " + actualTenant.getPhoneNum());
            System.out.println("Job : " + actualTenant.getJob());
        }
        System.out.println("- - - - - - - - - - - - - - -");
        assertEquals(expectedTenants.equals(actualTenants), actualTenants.equals(expectedTenants));

    }
}
