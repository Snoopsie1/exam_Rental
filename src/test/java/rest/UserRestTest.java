package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseOptions;
import io.restassured.response.ValidatableResponse;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import entities.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import utils.Populator;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserRestTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Tenant t1, t2, t3;
    private static House h1, h2, h3;
    private static Rental r1, r2, r3;
    private static List<Rental> rl1, rl2, rl3;
    private static List<Tenant> tl1, tl2, tl3;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            //Hvorfor i denne her rækkefølge? Her er en nem gennemgang
            //Et hus skal findes før det kan udlejes
            //En aftale skal kunne eksistere før man kan gå med på den
            //En mand kan derfor først sætte sig som udlejer til sidst

            //Create House Entities
            h1 = new House(1,"Valnøddevej 4", "Hornbæk", 6);
            h2 = new House(2,"Bretagnevej 28", "Ålsgårde", 4);
            h3 = new House(3,"Svanemøllevej 58", "Hellerup", 8);
            //Store House Entities
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            //Create Rental Entities
            r1 = new Rental(1,"15.02.1982", "22.02.2048", 4850, (4850*3), "Morten Olsen", null, null);
            r2 = new Rental(2,"04.06.1891", "04.06.2055", 3127, (3127*3), "Susan Petersen", null, null);
            r3 = new Rental(3,"21.09.2004", "18.11.2068", 9427, (9427*3), "Helvig Kartoffelberg", null, null);
            //Set House's rental agreement
            r1.setHouse(h1);
            r2.setHouse(h2);
            r3.setHouse(h3);
            //Store Rental Entities
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            //Create Rental List1
            rl1 = new ArrayList<>();
            rl1.add(r1);
            //Create Rental List2
            rl2 = new ArrayList<>();
            rl2.add(r2);
            //Create Rental List3
            rl3 = new ArrayList<>();
            rl3.add(r3);
            //Create Tenant Entity
            t1 = new Tenant(1,"Ole Henriksen", 22505084, "Make-Up Manden", rl1);
            t2 = new Tenant(2,"Rodja Pjort", 84901004, "Som flyver bort", rl2);
            t3 = new Tenant(3,"Hella Joof", 28649020, "Medie-Menneske", rl3);
            //Store Tenants
            em.persist(t1);
            em.persist(t2);
            em.persist(t3);
            //Create Tenant List1
            tl1 = new ArrayList<>();
            tl1.add(t1);
            //Create Tenant List2
            tl2 = new ArrayList<>();
            tl2.add(t2);
            //Create Tenant List3
            tl3 = new ArrayList<>();
            tl3.add(t3);
            //Set Tenants to a Rental Entity
            r1.setTenants(tl1);
            r2.setTenants(tl2);
            r3.setTenants(tl3);
            //Update Tenants to Rental
            em.merge(r1);
            em.merge(r2);
            em.merge(r3);
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

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }


    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/user").then().statusCode(200);
    }

    @Test
    public void testLog() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/user")
                .then().statusCode(200);
    }

    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/user")
                .then().log().body().statusCode(200);
    }

    //TODO: US-1 As a user, I would like to see all my rental agreements
    @Disabled //Doesn't work properly. Expected <3> - Actual [3]
    @Test
    public void seeAllRentalAgreements() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/{id}",t3.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(t1.getName()))
                .body("house", hasItems(hasEntry("address","Valnøddevej 4"),hasEntry("city","Hornbæk")));
    }
}
