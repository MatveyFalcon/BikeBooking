package model;

public class Store {
    private String name;
    private String address;

    public Store(String address, String name) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
// ctrl alt L