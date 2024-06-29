package model.bike_model;

import model.bikes.Bike;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BikeModelManager {
    private static BikeModelManager instance;
    private Connection connection;

    private BikeModelManager() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikebooking", "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удается подключиться к базе данных", e);
        }
    }

    public static BikeModelManager getInstance() {
        if (instance == null) {
            instance = new BikeModelManager();
        }
        return instance;
    }
    public List<Bike> getAllBikeModels() {
        List<Bike> bikeModels = new ArrayList<>();
        String query = "SELECT model_id, name FROM models";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bikeModels.add(new Bike(resultSet.getInt("model_id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bikeModels;
    }

}
