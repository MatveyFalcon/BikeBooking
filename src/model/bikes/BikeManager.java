package model.bikes;

import java.sql.*;

public class BikeManager {
    private static BikeManager instance;
    private Connection connection;

    private BikeManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static BikeManager getInstance() {
        if (instance == null) {
            instance = new BikeManager();
        }
        return instance;
    }

    public int getBikeCount(int modelId, int storeId) {
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

}
