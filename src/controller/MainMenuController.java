package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    private Model model;
    private Stage primaryStage;

    @FXML
    private Button rentBikeButton;

    @FXML
    private Button returnBikeButton;

    @FXML
    private Button viewBikesButton;

    public void initModel(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;

        rentBikeButton.setOnAction(e -> {
            try {
                showRentBikeView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        returnBikeButton.setOnAction(e -> showReturnBikeView());
        viewBikesButton.setOnAction(e -> showViewBikesView());
    }

    private void showRentBikeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookingView.fxml"));

        Parent root = loader.load();
        BookingController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }

    private void showReturnBikeView() {
        // Логика для показа экрана возврата велосипеда
    }

    private void showViewBikesView() {
        // Логика для показа экрана просмотра велосипедов
    }
}
