package model.booking;

import model.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private static BookingManager instance;
    private Connection connection;

    private BookingManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    public int getBookingCount(int modelId, int storeId, String bookingDate) {
        String query = "SELECT COUNT(*) AS booking_count " +
                "FROM bookings b " +
                "WHERE b.model_id = ? AND b.store_id = ? " +
                "AND b.booking_date BETWEEN DATE_SUB(?, INTERVAL 7 DAY) AND ? " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM rentals r " +
                "    WHERE r.booking_id = b.booking_id " +
                "    AND r.return_date IS NOT NULL" +
                ")";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, modelId);
            statement.setInt(2, storeId);
            statement.setString(3, bookingDate);
            statement.setString(4, bookingDate);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("booking_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean bookBike(int modelId, int storeId, String bookingDate, int clientId) {

        String query = "INSERT INTO bookings (client_id, model_id, store_id, booking_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, clientId);
            statement.setInt(2, modelId);
            statement.setInt(3, storeId);
            statement.setString(4, bookingDate);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Booking> getBookingsForUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, m.name as model, s.name as store, b.booking_date " +
                "FROM bookings b " +
                "JOIN models m ON b.model_id = m.model_id " +
                "JOIN stores s ON b.store_id = s.store_id " +
                "WHERE b.client_id = ? " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM rentals r " +
                "    WHERE r.booking_id = b.booking_id " +
                "    AND r.return_date IS NOT NULL" +
                ")";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(new Booking(
                            resultSet.getInt("booking_id"),
                            resultSet.getString("model"),
                            resultSet.getString("store"),
                            resultSet.getString("booking_date")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean confirmPickup(int bookingId) {
        // Получаем информацию о бронировании
        String bookingInfoQuery = "SELECT model_id, booking_date, store_id FROM bookings WHERE booking_id = ?";
        try (PreparedStatement bookingInfoStmt = connection.prepareStatement(bookingInfoQuery)) {
            bookingInfoStmt.setInt(1, bookingId);
            try (ResultSet resultSet = bookingInfoStmt.executeQuery()) {
                if (resultSet.next()) {
                    int modelId = resultSet.getInt("model_id");
                    String bookingDate = resultSet.getString("booking_date");
                    int storeId = resultSet.getInt("store_id");

                    // Проверяем доступность велосипеда
                    if (Model.getInstance().isBikeAvailableRentals(modelId, bookingDate, storeId)) {
                        // Выполняем вставку записи в таблицу аренд
                        String insertQuery = "INSERT INTO rentals (booking_id, bike_id, rental_start) " +
                                "SELECT b.booking_id, bi.bike_id, NOW() " +
                                "FROM bookings b " +
                                "JOIN bikes bi ON b.model_id = bi.model_id AND b.store_id = bi.store_id " +
                                "WHERE b.booking_id = ? " +
                                "AND NOT EXISTS (SELECT 1 FROM rentals r WHERE r.bike_id = bi.bike_id AND (r.return_date IS NULL OR r.return_date >= NOW())) " +
                                "LIMIT 1";

                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, bookingId);
                            int rowsAffected = insertStmt.executeUpdate();
                            if (rowsAffected > 0) {
                                return true;
                            }
                        }
                    } else {
                        System.out.println("Нет доступных велосипедов для этого бронирования.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
