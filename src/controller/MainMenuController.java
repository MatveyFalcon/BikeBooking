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

    public void initModel(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
    }

    public void showMyOrdersView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyOrderView.fxml"));

        Parent root = loader.load();
        MyOrdersController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }
    public void showBookingBikeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BookingView.fxml"));

        Parent root = loader.load();
        BookingController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }

    public void exit() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));

        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }

}
