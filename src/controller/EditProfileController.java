package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

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

    private Stage primaryStage;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        populateFields();
    }

    private void populateFields() {
        firstNameField.setText(Model.getInstance().getClient().getFirstName());
        lastNameField.setText(Model.getInstance().getClient().getLastName());
        middleNameField.setText(Model.getInstance().getClient().getMiddleName());
        passportNumberField.setText(Model.getInstance().getClient().getPassportNumber());
        passportSeriesField.setText(Model.getInstance().getClient().getPassportSeries());
        addressField.setText(Model.getInstance().getClient().getAddress());
        passwordField.setText(Model.getInstance().getClient().getPassword());
    }

    @FXML
    private void handleSaveButton() throws IOException {
        Model.getInstance().getClient().setFirstName(firstNameField.getText());
        Model.getInstance().getClient().setLastName(lastNameField.getText());
        Model.getInstance().getClient().setMiddleName(middleNameField.getText());
        Model.getInstance().getClient().setPassportNumber(passportNumberField.getText());
        Model.getInstance().getClient().setPassportSeries(passportSeriesField.getText());
        Model.getInstance().getClient().setAddress(addressField.getText());
        Model.getInstance().getClient().setPassword(passwordField.getText());

        Model.getInstance().updateClientInDatabase();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.init(primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();

    }
}


