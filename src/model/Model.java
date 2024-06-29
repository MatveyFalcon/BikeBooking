package model;

import model.client.AuthManager;
import model.bike_model.BikeModelManager;
import model.bikes.Bike;
import model.bikes.BikeManager;
import model.booking.Booking;
import model.booking.BookingManager;
import model.client.Client;
import model.client.ClientManager;
import model.rental.Rental;
import model.rental.RentalManager;
import model.store.Store;
import model.store.StoreManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Model {
    private static Model model;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bikebooking";
    private static final String DB_USER = "bikebooking";
    private static final String DB_PASSWORD = "bikebookingmatvey";

    private Connection connection;

    private model.client.Client client = new model.client.Client();

    private Model() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }
    public static Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    // Пример метода для выборки всех моделей велосипедов
    public List<Bike> getAllBikeModels() {
        return BikeModelManager.getInstance().getAllBikeModels();
    }

    public void confirmReturn(model.rental.Rental rental, String currentDate) {
        RentalManager.getInstance().confirmReturn(rental, currentDate);
    }

    public List<Store> getAllStores() {
        return StoreManager.getInstance().getAllStores();
    }

    public model.client.Client login(int id) {
        return AuthManager.getInstance().login(id);
    }

    public int getClientId(String passportNumber, String passportSeries, String password) {
        int tempId = AuthManager.getInstance().getClientId(passportNumber, passportSeries, password);
        this.client = login(tempId);
        return tempId;
    }

    public boolean register(model.client.Client client) {
        String checkQuery = "SELECT * FROM clients WHERE passport_number = ? AND passport_series = ?";
        String insertQuery = "INSERT INTO clients (first_name, last_name, middle_name, passport_number, passport_series, address, password, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, false)";

        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, client.getPassportNumber());
            checkStatement.setString(2, client.getPassportSeries());

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    return false;
                }
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, client.getFirstName());
                insertStatement.setString(2, client.getLastName());
                insertStatement.setString(3, client.getMiddleName());
                insertStatement.setString(4, client.getPassportNumber());
                insertStatement.setString(5, client.getPassportSeries());
                insertStatement.setString(6, client.getAddress());
                insertStatement.setString(7, client.getPassword());


                int rowsAffected = insertStatement.executeUpdate();

                int clientId = getClientId(client.getPassportNumber(), client.getPassportSeries(), client.getPassword());

                client.setClientId(clientId);

                this.client = client;
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Client getClient() {
        return client;
    }

    private int getBikeCount(int modelId, int storeId) {
       return BikeManager.getInstance().getBikeCount(modelId, storeId);
    }

    private int getBookingCount(int modelId, int storeId, String bookingDate) {
        return BookingManager.getInstance().getBookingCount(modelId, storeId, bookingDate);
    }



    public List<String> getAllUsersForComboBox() {
        return ClientManager.getInstance().getAllUsersForComboBox();
    }

    public int getUserIdByName(String firstName, String lastName) {
        return ClientManager.getInstance().getUserIdByName(firstName, lastName);
    }

    public List<model.rental.Rental> getRentalsByUserId(int userId) {
        return RentalManager.getInstance().getRentalsByUserId(userId);
    }


    private boolean isBikeAvailable(int modelId, String bookingDate, int storeId) {
        int bikeCount = getBikeCount(modelId, storeId);
        int bookingCount = getBookingCount(modelId, storeId, bookingDate);
        return bookingCount < bikeCount;
    }

    public boolean bookBike(int modelId, int storeId, String bookingDate) {
        if (!isBikeAvailable(modelId, bookingDate, storeId)) {
            return false;
        }
        return BookingManager.getInstance().bookBike(modelId, storeId, bookingDate, this.client.getClientId());
    }

    public List<Booking> getBookingsForUser(int userId) {
        return BookingManager.getInstance().getBookingsForUser(userId);
    }

    public boolean isBikeAvailableRentals(int modelId, String bookingDate, int storeId) {
        int bikeCount = getBikeCount(modelId, storeId);
        int bookingCount = getBookingCount(modelId, storeId, bookingDate);
        return bookingCount <= bikeCount;
    }
    public List<Rental> getRentalsForUser(int userId) {
        return RentalManager.getInstance().getRentalsForUser(userId);
    }
    public boolean confirmPickup(int bookingId) {
        return BookingManager.getInstance().confirmPickup(bookingId);
    }

    public void updateClientInDatabase() {
        String sql = "UPDATE clients SET first_name = ?, last_name = ?, middle_name = ?, passport_number = ?, passport_series = ?, address = ?, password = ? WHERE client_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, this.client.getFirstName());
            pstmt.setString(2, this.client.getLastName());
            pstmt.setString(3, this.client.getMiddleName());
            pstmt.setString(4, this.client.getPassportNumber());
            pstmt.setString(5, this.client.getPassportSeries());
            pstmt.setString(6, this.client.getAddress());
            pstmt.setString(7, this.client.getPassword());
            pstmt.setInt(8, this.client.getClientId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
