package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import dtos.TenantDTO;
import entities.House;
import entities.Rental;
import entities.Tenant;
import repository.AdminRepo;
import repository.UserRepo;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
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

    //TODO: US-4 As an admin I would like to create new rental agreements, tenants and houses
    //House Part
    @POST
    @Path("/house/")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createHouse(String content) {
        HouseDTO houseDTO = GSON.fromJson(content, HouseDTO.class);
        House house = new House(houseDTO);
        HouseDTO newHouseDTO = new HouseDTO(adminREPO.createHouse(house.getAddress(), house.getCity(), house.getNumberOfRooms()));
        return Response.ok().entity(GSON.toJson(newHouseDTO)).build();
    }

    //Rental Part
    @POST
    @Path("/rental/")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createRental(String content) {
        RentalDTO rentalDTO = GSON.fromJson(content, RentalDTO.class);
        Rental rental = new Rental(rentalDTO);
        RentalDTO newRentalDTO = new RentalDTO(adminREPO.createRental(
                rental.getStartDate(), rental.getEndDate(),
                rental.getPriceAnnual(), rental.getDeposit(),
                rental.getContactPerson(), rental.getTenants(), rental.getHouse()));
        return Response.ok().entity(GSON.toJson(newRentalDTO)).build();
    }

    //Tenant Part
    @POST
    @Path("/tenant/")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createTenant(String content) {
        TenantDTO tenantDTO = GSON.fromJson(content, TenantDTO.class);
        Tenant tenant = new Tenant(tenantDTO);
        TenantDTO newTenantDTO = new TenantDTO(adminREPO.createTenant(
                tenant.getName(), tenant.getPhoneNum(),
                tenant.getJob(), tenant.getRentals()));
        return Response.ok().entity(GSON.toJson(newTenantDTO)).build();
    }

    //TODO: US-5 As an admin, I would like to update a rental agreement to change a tenant
    @PUT
    @Path("/rental/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateTenantsOnSelectedRental(@PathParam("id") int id, String content) throws EntityNotFoundException {
        TenantDTO[] tenantDTOs = GSON.fromJson(content, TenantDTO[].class);
        List<TenantDTO> tenantDTOList = Arrays.asList(tenantDTOs);
        List<Tenant> tenants = new ArrayList<>();
        for (TenantDTO tenantDTO : tenantDTOList) {
            tenants.add(new Tenant(tenantDTO));
        }
        return Response.ok().entity(GSON.toJson(new RentalDTO(adminREPO.changeTenantsOnRentalAgreement(id, tenants)))).build();
    }
}
