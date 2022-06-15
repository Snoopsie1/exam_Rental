package dtos;

import entities.Tenant;

import java.util.ArrayList;
import java.util.List;

public class TenantDTO {
    private int id;
    private String name;
    private int phoneNum;
    private String job;

    private List<RentalDTO> rentals = new ArrayList<>();


    public TenantDTO(Tenant tenant){
        if(tenant.getId()!=0)
            this.id = tenant.getId();
        this.name = tenant.getName();
        this.phoneNum = tenant.getPhoneNum();
        this.job = tenant.getJob();
        tenant.getRentals().forEach(rental -> this.rentals.add(new RentalDTO(rental)));
    }

    public TenantDTO(int id, String name, int phoneNum, String job, List<RentalDTO> rentals) {
        this.id = id;
        this.name = name;
        this.phoneNum = phoneNum;
        this.job = job;
        this.rentals = rentals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<RentalDTO> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }
}
