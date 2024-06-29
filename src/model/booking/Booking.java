package model.booking;

public class Booking {
    private int bookingId;
    private String model;
    private String store;
    private String bookingDate;

    public Booking(int bookingId, String model, String store, String bookingDate) {
        this.bookingId = bookingId;
        this.model = model;
        this.store = store;
        this.bookingDate = bookingDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getModel() {
        return model;
    }

    public String getStore() {
        return store;
    }

    public String getBookingDate() {
        return bookingDate;
    }
}
