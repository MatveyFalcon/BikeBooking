package model;

import models.Store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bikebooking";
    private static final String DB_USER = "bikebooking";
    private static final String DB_PASSWORD = "bikebookingmatvey";

    private Connection connection;

    public Model() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    // Пример метода для выборки всех моделей велосипедов
    public List<String> getAllBikeModels() {
        List<String> bikeModels = new ArrayList<>();
        String query = "SELECT * FROM models";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bikeModels.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bikeModels;
    }

    // Пример метода для извлечения всех хранилищ
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        String query = "SELECT * FROM stores";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                stores.add(new Store(resultSet.getString("address"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stores;
    }

    // Примерный способ бронирования велосипеда
    public boolean bookBike(int clientId, int modelId, int storeId, String bookingDate) {
        String query = "INSERT INTO Бронирования (client_id, model_id, store_id, Дата_бронирования) VALUES (?, ?, ?, ?)";

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

    // Закрытие подключения к базе данных
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Добавляйте другие CRUD-операции по мере необходимости

    // Например, выборка всех бронирований
    public List<String> getAllBookings() {
        List<String> bookings = new ArrayList<>();
        String query = "SELECT * FROM Бронирования";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bookings.add("Booking ID: " + resultSet.getInt("booking_id") +
                        ", Client ID: " + resultSet.getInt("client_id") +
                        ", Model ID: " + resultSet.getInt("model_id") +
                        ", Store ID: " + resultSet.getInt("store_id") +
                        ", Date: " + resultSet.getString("Дата_бронирования"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
