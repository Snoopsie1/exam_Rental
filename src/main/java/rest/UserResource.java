package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import entities.Rental;
import repository.UserRepo;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final UserRepo userREPO = UserRepo.getUserRepo(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello User!\"}";
    }

    //TODO: US-1 As a user, I would like to see all my rental agreements
    @GET
    @Path("/tenantsRentals/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        List<Rental> rentalList = userREPO.seeAllTenantsRentals(id);
        List<RentalDTO> rentalDTOList = new ArrayList<>();
        for (Rental rental : rentalList) {
            rentalDTOList.add(new RentalDTO(rental));
        }
        System.out.println(rentalDTOList);
        return Response.ok().entity(GSON.toJson(rentalDTOList)).build();
    }

    //TODO: US-2 As a user, I would like to click on a rental agreement and see all details about the house
    @GET
    @Path("/house/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response seeDetailsOnHouseById(@PathParam("id") int id) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(new HouseDTO(userREPO.seeDetailsAboutHouseFromRentalID(id)))).build();
    }

}
