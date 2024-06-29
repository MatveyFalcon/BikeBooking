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
import model.bikes.Bike;
import model.Model;
import model.store.Store;

import java.io.IOException;

public class BookingController {

    @FXML
    private DatePicker bookingDate;

    @FXML
    private ComboBox<Bike> bikeModel;

    @FXML
    private ComboBox<Store> store;

    private Stage primaryStage;


    @FXML
    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Инициализация моделей велосипеда
        bikeModel.getItems().addAll(Model.getInstance().getAllBikeModels());

        // Инициализация магазинов
        store.getItems().addAll(Model.getInstance().getAllStores());
    }

    @FXML
    private void handleBooking() {
        String _date = bookingDate.getValue() != null ? bookingDate.getValue().toString() : "не выбрано";
        Bike selectedBikeModel = bikeModel.getValue();
        String _bikeModel = selectedBikeModel != null ? selectedBikeModel.getName() : "не выбрано";
        int modelId = selectedBikeModel != null ? selectedBikeModel.getId() : -1;
        Store selectedStore = store.getValue();
        String _storeName = selectedStore != null ? selectedStore.getName() : "не выбрано";
        String _address = selectedStore != null ? selectedStore.getAddress() : "не выбрано";

        if (modelId != -1 && selectedStore != null) {
            if(Model.getInstance().bookBike(modelId, selectedStore.getId(), _date)){
                // Показ уведомления пользователю
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Бронирование");
                alert.setHeaderText("Информация о бронировании");
                alert.setContentText("Дата: " + _date + "\nМодель велосипеда: " + _bikeModel + "\nМагазин: " + _storeName + "\nАдрес: " + _address);
                alert.showAndWait();
            } else{
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Бронирование");
                alert.setHeaderText("Информация о бронировании");
                alert.setContentText("На данную дату нет доступных велосипедов");
                alert.showAndWait();
            }


            }

    }

    @FXML
    private void exitToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.init(primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }
}
