
package model;

public class Rental {
    private int rentalId;
    private String model;
    private String store;
    private String startDate;
    private String endDate;

    public Rental(int rentalId, String model, String store, String startDate, String endDate) {
        this.rentalId = rentalId;
        this.model = model;
        this.store = store;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getRentalId() {
        return rentalId;
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
}
