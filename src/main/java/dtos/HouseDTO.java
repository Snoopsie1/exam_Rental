package dtos;

import entities.House;

public class HouseDTO {

    private int id;

    private String address;
    private String city;
    private int numberOfRooms;

    public HouseDTO(House house){
        if (house.getId() != 0)
            this.id = house.getId();
        this.address = house.getAddress();
        this.city = house.getCity();
        this.numberOfRooms = house.getNumberOfRooms();
    }

    public HouseDTO(String address, String city, int numberOfRooms) {
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
