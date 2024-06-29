package model.client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    private static ClientManager instance;
    private Connection connection;

    private ClientManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    public List<String> getAllUsersForComboBox() {
        List<String> users = new ArrayList<>();
        String query = "SELECT first_name, last_name FROM clients";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String user = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public int getUserIdByName(String firstName, String lastName) {
        String query = "SELECT client_id FROM clients WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("client_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Возвращаем -1, если пользователь не найден
    }

}
