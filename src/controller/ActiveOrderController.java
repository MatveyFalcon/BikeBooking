package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Model;
import model.rental.Rental;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActiveOrderController {

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private TableView<Rental> rentalsTable;

    @FXML
    private TableColumn<Rental, Integer> bikeIdColumn;

    @FXML
    private TableColumn<Rental, String> startDateColumn;

    @FXML
    private TableColumn<Rental, String> endDateColumn;

    @FXML
    private TableColumn<Rental, Void> actionColumn;

    @FXML
    private TableColumn<Rental, Integer> rentId;

    private Stage primaryStage;

    @FXML
    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        ObservableList<String> users = FXCollections.observableArrayList(
                Model.getInstance().getAllUsersForComboBox()
        );
        userComboBox.setItems(users);
        bikeIdColumn.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        rentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));

        addButtonToTable();

    }

    private void addButtonToTable() {
        Callback<TableColumn<Rental, Void>, TableCell<Rental, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Rental, Void> call(final TableColumn<Rental, Void> param) {
                final TableCell<Rental, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Подтвердить");

                    {
                        btn.setOnAction(event -> {
                            Rental rental = getTableView().getItems().get(getIndex());
                            confirmReturn(rental);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void confirmReturn(Rental rental) {
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Model.getInstance().confirmReturn(rental, currentDate);
        rental.setEndDate(currentDate);
        rentalsTable.refresh();
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

    @FXML
    private void searchUserRentals() {
        String selectedUser = userComboBox.getValue();
        if (selectedUser != null) {
            String[] nameParts = selectedUser.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts[1];

            int userId = Model.getInstance().getUserIdByName(firstName, lastName);

            ObservableList<Rental> rentals = FXCollections.observableArrayList(Model.getInstance().getRentalsByUserId(userId));
            rentalsTable.setItems(rentals);
        }
    }
}
