package entities;

import javax.persistence.*;

@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Der Stod GenerationType.AUTO før. Men IDENTITY virker?
    private int id;

    private String address;
    private String city;
    private int numberOfRooms;

    public House() {
        super();
    }

    public House(int id, String address, String city, int numberOfRooms) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.numberOfRooms = numberOfRooms;
    }

    //For Admin to create a house.
    public House(String address, String city, int numberOfRooms) {
        this.address = address;
        this.city = city;
        this.numberOfRooms = numberOfRooms;
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
}
