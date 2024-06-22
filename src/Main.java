

import model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.MainMenuController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));

        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        Model model = new Model();
        controller.initModel(model, primaryStage);
        model.getAllBikeModels();
        primaryStage.setTitle("Прокат велосипедов");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
