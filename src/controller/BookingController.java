package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Model;
import model.Store;

import java.io.IOException;

public class BookingController {

    @FXML
    private DatePicker bookingDate;

    @FXML
    private ComboBox<String> bikeModel;

    @FXML
    private ComboBox<Store> store;

    private Stage primaryStage;

    private Model model;

    @FXML
    public void initModel(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
        // Инициализация моделей велосипеда
        bikeModel.getItems().addAll(model.getAllBikeModels());

        // Инициализация магазинов
        store.getItems().addAll(model.getAllStores());
    }

    @FXML
    private void handleBooking() {
        String date = bookingDate.getValue() != null ? bookingDate.getValue().toString() : "не выбрано";
        String model = bikeModel.getValue() != null ? bikeModel.getValue() : "не выбрано";
        String storeName = store.getValue().getName() != null ? store.getValue().getName() : "не выбрано";
        String address = store.getValue().getAddress() != null ? store.getValue().getAddress() : "не выбрано";

        // Здесь можно добавить логику для обработки бронирования, например, сохранение данных в БД

        // Показ уведомления пользователю
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Бронирование");
        alert.setHeaderText("Информация о бронировании");
        alert.setContentText("Дата: " + date + "\nМодель велосипеда: " + model + "\nМагазин: " + storeName + "\nАдрес: " + address);
        alert.showAndWait();
    }

    @FXML
    private void exitToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }
}
