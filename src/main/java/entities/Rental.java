package entities;

import dtos.RentalDTO;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Changed from AUTO -> IDENTITY
    private int id;

    private String startDate;
    private String endDate;
    private int priceAnnual;
    private int deposit;
    private String contactPerson;

    @ManyToMany(targetEntity = Tenant.class)
    private List<Tenant> tenants;

    @ManyToOne
    private House house;

    @Transient
    private List<String> tenantNames;

    public Rental(){}

    public Rental(int id, String startDate, String endDate, int priceAnnual, int deposit, String contactPerson, List<Tenant> tenants, House house) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceAnnual = priceAnnual;
        this.deposit = deposit;
        this.contactPerson = contactPerson;
        this.tenants = tenants;
        this.house = house;
    }

    //For Admin to create a Rental Agreement.
    public Rental(String startDate, String endDate, int priceAnnual, int deposit, String contactPerson, List<Tenant> tenants, House house) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceAnnual = priceAnnual;
        this.deposit = deposit;
        this.contactPerson = contactPerson;
        this.tenants = tenants;
        this.house = house;
    }

    //For DTO Conversion
    public Rental(RentalDTO rentalDTO){
        this.id = rentalDTO.getId();
        this.startDate = rentalDTO.getStartDate();
        this.endDate = rentalDTO.getEndDate();
        this.priceAnnual = rentalDTO.getPriceAnnual();
        this.deposit = rentalDTO.getDeposit();
        this.contactPerson = rentalDTO.getContactPerson();
        this.tenantNames = rentalDTO.getTenantNames();
        this.house = new House(rentalDTO.getHouse());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPriceAnnual() {
        return priceAnnual;
    }

    public void setPriceAnnual(int priceAnnual) {
        this.priceAnnual = priceAnnual;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public String toString() {
        return "Rental" +
                "\n{" +
                "\n id: " + id +
                "\n startDate: " + startDate + '\'' +
                "\n endDate: " + endDate + '\'' +
                "\n priceAnnual: " + priceAnnual +
                "\n deposit: " + deposit +
                "\n contactPerson: " + contactPerson + '\'' +
                "\n tenants: " + tenants +
                "\n house: " + house +
                "\n}";
    }
}
