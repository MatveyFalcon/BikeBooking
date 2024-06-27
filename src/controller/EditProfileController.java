package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import model.Client;
import model.Model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditProfileController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private TextField passportNumberField;
    @FXML
    private TextField passportSeriesField;
    @FXML
    private TextField addressField;
    @FXML
    private PasswordField passwordField;

    private Model model;
    private Stage primaryStage;

    public void initModel(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
        populateFields();
    }

    private void populateFields() {
        firstNameField.setText(model.getClient().getFirstName());
        lastNameField.setText(model.getClient().getLastName());
        middleNameField.setText(model.getClient().getMiddleName());
        passportNumberField.setText(model.getClient().getPassportNumber());
        passportSeriesField.setText(model.getClient().getPassportSeries());
        addressField.setText(model.getClient().getAddress());
        passwordField.setText(model.getClient().getPassword());
    }

    @FXML
    private void handleSaveButton() throws IOException {
        model.getClient().setFirstName(firstNameField.getText());
        model.getClient().setLastName(lastNameField.getText());
        model.getClient().setMiddleName(middleNameField.getText());
        model.getClient().setPassportNumber(passportNumberField.getText());
        model.getClient().setPassportSeries(passportSeriesField.getText());
        model.getClient().setAddress(addressField.getText());
        model.getClient().setPassword(passwordField.getText());

        model.updateClientInDatabase();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();

    }
}


