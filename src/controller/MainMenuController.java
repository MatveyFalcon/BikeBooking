package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;

import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController {
    private Model model;
    private Stage primaryStage;
    @FXML
    private Button activeOrder;
    public void initModel(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
        activeOrder.setVisible(model.getClient().isAdmin());
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

    public void showEditProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProfileView.fxml"));

        Parent root = loader.load();
        EditProfileController controller = loader.getController();
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

    public void activeOrder() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ActiveOrderView.fxml"));

        Parent root = loader.load();
        ActiveOrderController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }
}
