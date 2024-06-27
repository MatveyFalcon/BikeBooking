package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField passportSeriesField;

    @FXML
    private TextField passportNumberField;

    @FXML
    private PasswordField passwordField;

    private Stage primaryStage;

    private Model model;

    @FXML
    public void initModel(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
    }
    @FXML
    private void handleLogin() throws IOException {
        // запускается при нажатии кнопки входа
        int id = model.getClientId(passportNumberField.getText(), passportSeriesField.getText(), passwordField.getText());
        // id выбирается из модели путем запроса к таблице клиентов и выборки среди них подходящего по входным данным
        if(id == -1){
            // если нет, то метод вернет -1, в случае если он сделал, выбрасываем окно с уведомлением о том что данные не верны
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Такого пользователя нет");
            alert.setContentText("Проверьте правильность введенных данных");
            alert.showAndWait();
        } else{
            // если такой пользователь есть, пароль верный, то переходим на глвый экран
            // айди пользователя уже сохранен в моделиы
            switchToMainMenu();
        }
    }

    private void switchToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    @FXML
    private void switchToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegistrationView.fxml"));

        Parent root = loader.load();
        RegistrationController controller = loader.getController();
        controller.initModel(model, primaryStage);
        primaryStage.setScene(new Scene(root, 1280, 720));

        primaryStage.show();


    }
}
