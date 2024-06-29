
package model.rental;

public class Rental {

    private int rentId;
    private String model;
    private String store;
    private String startDate;
    private String endDate;
    private int bikeId;
    private int bookingId;
    public Rental() {
    }

    public Rental(int rentalId, String model, String store, String startDate, String endDate, int bikeId) {
        this.rentId = rentalId;
        this.model = model;
        this.store = store;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bikeId = bikeId;
    }
    public Rental(int rentalId, String model, String store, String startDate, String endDate) {
        this.rentId = rentalId;
        this.model = model;
        this.store = store;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setRentId(int rentId) {
        this.rentId = rentId;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getRentId() {
        return rentId;
    }

    public String getModel() {
        return model;
    }

    public String getStore() {
        return store;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
