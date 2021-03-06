package entities;

import dtos.TenantDTO;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.List;

@Entity
public class Tenant {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) //AUTO -> Identity
    private int id;

    private String name;
    private int phoneNum;
    private String job;

    @ManyToMany(targetEntity = Rental.class)
    private List<Rental> rentals;

    public Tenant() {}

    //For Admin to create a Tenant.
    public Tenant(String name, int phoneNum, String job, List<Rental> rentals) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.job = job;
        this.rentals = rentals;
    }

    //For DTO Conversion
    public Tenant(TenantDTO tenantDTO){
        this.id = tenantDTO.getId();
        this.name = tenantDTO.getName();
        this.phoneNum = tenantDTO.getPhoneNum();
        this.job = tenantDTO.getJob();
        tenantDTO.getRentals().forEach(rentalDTO -> this.rentals.add(new Rental(rentalDTO)));
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

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public String toString() {
        return "Tenant" +
                "\n{" +
                "id: " + id +
                "\nName: " + name + '\'' +
                "\n PhoneNum: " + phoneNum +
                "\n Job: " + job + '\'' +
                "\n Rentals: " + rentals +
                "\n}";
    }
}
