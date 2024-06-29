package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.booking.Booking;
import model.Model;
import model.rental.Rental;

import java.io.IOException;

public class MyOrdersController {

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableColumn<Booking, String> bookingModelColumn;

    @FXML
    private TableColumn<Booking, String> bookingStoreColumn;

    @FXML
    private TableColumn<Booking, String> bookingDateColumn;

    @FXML
    private TableColumn<Booking, Void> confirmPickupColumn;

    @FXML
    private TableView<Rental> rentalTable;

    @FXML
    private TableColumn<Rental, String> rentalModelColumn;

    @FXML
    private TableColumn<Rental, String> rentalStoreColumn;

    @FXML
    private TableColumn<Rental, String> rentalStartDateColumn;

    @FXML
    private TableColumn<Rental, String> rentalEndDateColumn;


    private Stage primaryStage;

    @FXML
    public void initialize() {
        bookingModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        bookingStoreColumn.setCellValueFactory(new PropertyValueFactory<>("store"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        confirmPickupColumn.setCellFactory(getConfirmPickupCellFactory());

        rentalModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        rentalStoreColumn.setCellValueFactory(new PropertyValueFactory<>("store"));
        rentalStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        rentalEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadBookings();
        loadRentals();
    }

    private void loadBookings() {
        ObservableList<Booking> bookingList = FXCollections.observableArrayList(Model.getInstance().getBookingsForUser(Model.getInstance().getClient().getClientId()));
        bookingTable.setItems(bookingList);
    }

    private void loadRentals() {
        ObservableList<Rental> rentalList = FXCollections.observableArrayList(Model.getInstance().getRentalsForUser(Model.getInstance().getClient().getClientId()));
        rentalTable.setItems(rentalList);
    }

    private Callback<TableColumn<Booking, Void>, TableCell<Booking, Void>> getConfirmPickupCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Booking, Void> call(final TableColumn<Booking, Void> param) {
                final TableCell<Booking, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Подтвердить");

                    {
                        btn.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());
                            handleConfirmPickup(booking);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
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
    }

    private void handleConfirmPickup(Booking booking) {
        if (booking != null) {
            boolean success = Model.getInstance().confirmPickup(booking.getBookingId());
            if (success) {
                showAlert("Подтверждено");
                loadBookings(); // Refresh the booking list
                loadRentals(); // Refresh the rental list
            } else {
                showAlert("Ошибка в подтверждении");
            }
        } else {
            showAlert("Выберите бронирование для подтверждения");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
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
