package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import dtos.TenantDTO;
import entities.Rental;
import entities.Tenant;
import repository.AdminRepo;
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

@Path("admin")
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final AdminRepo adminREPO = AdminRepo.getAdminRepo(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello Admin!\"}";
    }


    //TODO: US-3 As an admin, I would like to see all tenants currently living in a particular house
    @GET
    @Path("/house/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTenantsByHouseId(@PathParam("id") int id) throws EntityNotFoundException{
        List<Tenant> tenantList = adminREPO.getTenantsInSpecificHouse(id);
        List<TenantDTO> tenantDTOS = new ArrayList<>();
        for (Tenant tenant : tenantList) {
            tenantDTOS.add(new TenantDTO(tenant));
        }

        return Response.ok().entity(GSON.toJson(tenantDTOS)).build();
    }

}
