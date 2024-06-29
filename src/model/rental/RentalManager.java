package model.rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalManager {
    private static RentalManager instance;
    private Connection connection;

    private RentalManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static RentalManager getInstance() {
        if (instance == null) {
            instance = new RentalManager();
        }
        return instance;
    }
    public void confirmReturn(Rental rental, String currentDate) {
        String query = "UPDATE rentals SET return_date = ? WHERE rent_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, currentDate);
            statement.setInt(2, rental.getRentId());
            System.out.println(rental.getRentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Rental> getRentalsByUserId(int userId) {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT r.* FROM rentals r " +
                "JOIN bookings b ON r.booking_id = b.booking_id " +
                "WHERE b.client_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Rental rental = new Rental();
                    rental.setBikeId(resultSet.getInt("bike_id"));
                    rental.setStartDate(resultSet.getString("rental_start"));
                    rental.setEndDate(resultSet.getString("return_date"));
                    rental.setRentId(resultSet.getInt("rent_id"));
                    rental.setBookingId(resultSet.getInt("booking_id"));
                    rentals.add(rental);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }
    public List<Rental> getRentalsForUser(int userId) {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT r.rent_id, b.booking_id, m.name as model, s.name as store, r.rental_start, r.return_date " +
                "FROM rentals r " +
                "JOIN bookings b ON r.booking_id = b.booking_id " +
                "JOIN models m ON b.model_id = m.model_id " +
                "JOIN stores s ON b.store_id = s.store_id " +
                "WHERE b.client_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rentals.add(new Rental(
                            resultSet.getInt("rent_id"),
                            resultSet.getString("model"),
                            resultSet.getString("store"),
                            resultSet.getString("rental_start"),
                            resultSet.getString("return_date")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }


}
