package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import model.client.Client;
import model.Model;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private TextField addressField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField passportSeriesField;

    @FXML
    private TextField passportNumberField;

    @FXML
    private PasswordField passwordField;

    private Stage primaryStage;


    @FXML
    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;

    }


    @FXML
    private void handleRegister() throws IOException {
        String address = addressField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String middleName = middleNameField.getText();
        String passportSeries = passportSeriesField.getText();
        String passportNumber = passportNumberField.getText();
        String password = passwordField.getText();
        Client client = new Client(
                0,
                firstName,
                lastName,
                middleName,
                passportNumber,
                passportSeries,
                address,
                password,
                false);

        if(!Model.getInstance().register(client)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Пользователь уже существует");
            alert.showAndWait();
        }
        else{
            switchToMainMenu();
        }
    }

    private void switchToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.init(primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    @FXML
    private void switchToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));

        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.init(primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();
    }
}
