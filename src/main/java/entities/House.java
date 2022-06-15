package entities;

import dtos.HouseDTO;

import javax.persistence.*;

@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Der Stod GenerationType.AUTO før. Men IDENTITY virker?
    private int id;

    private String address;
    private String city;
    private int numberOfRooms;

    public House() {}

    public House(int id, String address, String city, int numberOfRooms) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.numberOfRooms = numberOfRooms;
    }

    //For Admin to create a House.
    public House(String address, String city, int numberOfRooms) {
        this.address = address;
        this.city = city;
        this.numberOfRooms = numberOfRooms;
    }

    //DTO Conversion
    public House(HouseDTO houseDTO){
        this.id = houseDTO.getId();
        this.address = houseDTO.getAddress();
        this.city = houseDTO.getCity();
        this.numberOfRooms = houseDTO.getNumberOfRooms();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    @Override
    public String toString() {
        return "House" +
                "\n{" +
                "\n Id: " + id +
                "\n Address: " + address + '\'' +
                "\n City: " + city + '\'' +
                "\n NumberOfRooms: " + numberOfRooms +
                "\n}";
    }
}
