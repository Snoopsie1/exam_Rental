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
import javax.xml.bind.SchemaOutputResolver;
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



    public UserTest() {}

    @BeforeAll
    public static void setupClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        userREPO = UserRepo.getUserRepo(emf);
        Populator.populateLocalTEST();
    }

    @BeforeEach
    public void setup() {

        demoTenantList = new ArrayList<>();
        demoRentalList = new ArrayList<>();

        demoHouse = new House("Valnøddevej 4", "Hornbæk",6);
        demoHouse2 = new House("Bretagnevej 28", "Ålsgårde", 4);
        demoRental = new Rental("15.02.1982", "22.02.2048", 4850, 14550, "Morten Olsen",null, demoHouse);
        demoRentalList.add(demoRental);
        demoTenant =  new Tenant("Ole Henriksen", 22505084, "Make-Up Manden", demoRentalList);
        demoTenantList.add(demoTenant);
        demoRental.setTenants(demoTenantList);
    }

    //TODO: US-1 As a user, I would like to see all my rental agreements
    @Test
    public void US1_seeAllAgreementsTest() throws Exception {
        List<Rental> expectedRentalList = demoRentalList;

        //Testing with Tenant with ID 1
        List<Rental> actualRentalList = userREPO.seeAllTenantsRentals(1);
        System.out.println(actualRentalList);
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
    public void US2_seeDetailsAboutHouseFromAgreementTest() throws Exception {
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

}
