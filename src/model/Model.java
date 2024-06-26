package model;

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

    private Client client = new Client();

    public Model() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    // Пример метода для выборки всех моделей велосипедов
    public List<BikeModel> getAllBikeModels() {
        List<BikeModel> bikeModels = new ArrayList<>();
        String query = "SELECT model_id, name FROM models";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bikeModels.add(new BikeModel(resultSet.getInt("model_id"), resultSet.getString("name")));
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
                stores.add(new Store(resultSet.getInt("store_id"), resultSet.getString("address"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stores;
    }

    public int getClientId(String passportNumber, String passportSeries, String password) {
        String query = "SELECT client_id FROM clients WHERE passport_number = ? AND passport_series = ? AND password = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            checkStatement.setString(1, passportNumber);
            checkStatement.setString(2, passportSeries);
            checkStatement.setString(3, password);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    this.client.setClientId(resultSet.getInt(1));
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean register(Client client) {
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

    public Client getClient() {
        return client;
    }

    private int getBikeCount(int modelId, int storeId) {
        String query = "SELECT COUNT(*) AS bike_count FROM bikes WHERE model_id = ? AND store_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, modelId);
            statement.setInt(2, storeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("bike_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getBookingCount(int modelId, int storeId, String bookingDate) {
        String query = "SELECT COUNT(*) AS booking_count FROM bookings WHERE model_id = ? AND store_id = ? AND booking_date BETWEEN DATE_SUB(?, INTERVAL 7 DAY) AND ?";
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
    private boolean isBikeAvailable(int modelId, String bookingDate, int storeId) {
        int bikeCount = getBikeCount(modelId, storeId);
        int bookingCount = getBookingCount(modelId, storeId, bookingDate);
        System.out.println("Байков: " + bikeCount +  " Бронирований: " + bookingCount);
        return bookingCount < bikeCount;
    }

    public boolean bookBike(int modelId, int storeId, String bookingDate) {
        if (!isBikeAvailable(modelId, bookingDate, storeId)) {
            return false;
        }
        String query = "INSERT INTO bookings (client_id, model_id, store_id, booking_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.client.getClientId());
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

}
