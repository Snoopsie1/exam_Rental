package repository;

import entities.House;
import entities.Rental;
import entities.Tenant;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.Populator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminTest {
    private static EntityManagerFactory emf;
    private static AdminRepo adminREPO;

    private House demoHouse;
    private House demoHouse2;
    private Rental demoRental;
    private Tenant demoTenant;
    private List<Tenant> demoTenantList;
    private List<Rental> demoRentalList;

    public AdminTest() {}

    @BeforeAll
    public static void setupClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        adminREPO = AdminRepo.getAdminRepo(emf);
        Populator.populateLocalTEST();
    }

    //TODO: Is unnecessary, remove later
    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    @BeforeEach
    public void setup() {
        demoTenantList = new ArrayList<>();
        demoRentalList = new ArrayList<>();

        demoHouse = new House(1, "Valnøddevej 4", "Hornbæk", 6);
        demoHouse2 = new House(2, "Bretagnevej 28", "Ålsgårde", 4);
        demoRental = new Rental(1, "15.02.1982", "22.02.2048", 4850, 14550, "Morten Olsen", null, demoHouse);
        demoRentalList.add(demoRental);
        demoTenant = new Tenant(1, "Ole Henriksen", 22505084, "Make-Up Manden", demoRentalList);
        demoTenantList.add(demoTenant);
        demoRental.setTenants(demoTenantList);
    }

    //TODO: US-3 As an admin, I would like to see all tenants currently living in a particular house
    //Process
    //1. Admin clicks on house
    //2. Click registers house's ID
    //3. Use House ID to find Rental Entity
    //4. Use Rental Entity's ID to see all Tenant IDs that is on Tenant_Rental table
    @Test
    public void US3_seeTenantsLivingInHouseTest() throws Exception{
        List<Tenant> expectedTenants = demoTenantList;
        List<Tenant> actualTenants = adminREPO.getTenantsInSpecificHouse(1);

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

    //TODO: US-4 As an admin I would like to create new rental agreements, tenants and houses
    //Process
    //1. Create Tenants List
    //2. Create Rental List
    //3. Create House
    //4. Create Rental (With null value on Tenants)
    //5. Create Tenant
    //6. Add Tenant to Tenant List
    //7. Set Tenants on Rental
    //8. Add Rental to Rental List
    //9. ???
    //10. Profit.

    //After thought: Split these steps up into multiple methods,
    // so admin can on the website create houses, tenants and rentals independently?
    @Test
    public void US4_createHouseTest() {
        House createdHouse = adminREPO.createHouse("Nørrebrogade 44", "KBH N", 2);

        System.out.println("House created with these details: ");
        System.out.println("Address: " + createdHouse.getAddress());
        System.out.println("City: " + createdHouse.getCity());
        System.out.println("NumOfRooms: " + createdHouse.getNumberOfRooms());
    }

    @Test
    public void US4_createRental() {
        Rental createdRental =
                adminREPO.createRental(
                        "00.00.0000","14.08.1928",
                        1234,1234*3,
                        "Ole Olsen",null,null
                );


        System.out.println("Rental Agreement created with these details: ");
        System.out.println("Start Date: " + createdRental.getStartDate());
        System.out.println("End Date: " + createdRental.getEndDate());
        System.out.println("Annual Price: " + createdRental.getPriceAnnual());
        System.out.println("Deposit: " + createdRental.getDeposit());
        System.out.println("Contact Person: " + createdRental.getContactPerson());
        System.out.println("Tenants: " + createdRental.getTenants());
        System.out.println("House: " + createdRental.getHouse());

        EntityManager em = emf.createEntityManager();
        try {
            Rental checkRental = em.find(Rental.class, createdRental.getId());
            if(checkRental == null)
                throw new EntityNotFoundException("TEST - Your entity was not found");
            System.out.println("Rental Entity with ID: " + createdRental.getId() +" was found!");
        } finally {
            em.close();
        }
    }

    @Test
    public void US4_createTenant(){

        Tenant createdTenant = adminREPO.createTenant("SvenErik", 22505084, "Tømmer",null);

        System.out.println("Tenant created with these details: ");
        System.out.println("Name: " + createdTenant.getName());
        System.out.println("Phone Number: " + createdTenant.getPhoneNum());
        System.out.println("Job: " + createdTenant.getJob());
        System.out.println("Rental Agreements: " + createdTenant.getRentals());

        EntityManager em = emf.createEntityManager();
        try {
            Tenant checkTenant = em.find(Tenant.class, createdTenant.getId());
            if(checkTenant == null)
                throw new EntityNotFoundException("TEST - Your entity was not found");
            System.out.println("Tenant Entity with ID: " + createdTenant.getId() +" was found!");
        } finally {
            em.close();
        }
    }
}
