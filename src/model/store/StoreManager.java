package model.store;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreManager {
    private static StoreManager instance;
    private Connection connection;

    private StoreManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
        }
        return instance;
    }

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
}
