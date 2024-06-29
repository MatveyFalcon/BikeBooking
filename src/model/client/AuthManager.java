package model.client;

import java.sql.*;

public class AuthManager {
    private static AuthManager instance;
    private Connection connection;

    private AuthManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public Client login(int id) {
        String query = "SELECT * FROM clients WHERE client_id = ?";
        Client client = null;

        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            checkStatement.setInt(1, id);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    client = new Client(
                            resultSet.getInt("client_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("middle_name"),
                            resultSet.getString("passport_number"),
                            resultSet.getString("passport_series"),
                            resultSet.getString("address"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("is_admin")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }
    public int getClientId(String passportNumber, String passportSeries, String password) {
        String query = "SELECT client_id FROM clients WHERE passport_number = ? AND passport_series = ? AND password = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            checkStatement.setString(1, passportNumber);
            checkStatement.setString(2, passportSeries);
            checkStatement.setString(3, password);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }



}
