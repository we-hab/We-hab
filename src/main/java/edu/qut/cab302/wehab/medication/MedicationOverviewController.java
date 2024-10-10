package edu.qut.cab302.wehab.medication;

import edu.qut.cab302.wehab.MainApplication;
import edu.qut.cab302.wehab.dashboard.ButtonController;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.user_account.UserAccount;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static edu.qut.cab302.wehab.medication.MedicationSearchModel.createMedicationTables;

public class MedicationOverviewController {

    private static Stage medicationOverviewModal = new Stage();
    private static Stage notificationPopupModal = new Stage();

    @FXML
    private Label selectedMedicationLabel;

    @FXML
    private Button createReminderButton;

    @FXML
    private Button editReminderButton;

    @FXML
    private Button viewSummaryButton;

    @FXML
    private Button addMedicationButton;

    @FXML
    private Button dashboardButton;
    @FXML
    private Button workoutButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Label loggedInUserLabel;

    @FXML
    private ListView resultsWindow;

    @FXML
    private TextField dosageTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox unitComboBox;

    @FXML
    private Spinner<LocalTime> timeSpinner;

    private HashMap<String, String> userSavedMedications;

    private void refreshResultsWindow() {
        resultsWindow.getItems().clear();
        for(String medicationName : userSavedMedications.keySet()) {
            resultsWindow.getItems().add(medicationName);
        }
    }

    @FXML
    public void initialize() {

        ButtonController.initialiseButtons(dashboardButton, workoutButton, null, settingsButton, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullName);
        } else
        {
            loggedInUserLabel.setText("Error");
        }

        try {
            System.out.print("Accessing medications table...");
            createMedicationTables();
            System.out.println("success!");
        } catch (SQLException e) {
            System.out.println("failed.\n" + e.getMessage());
        }

        addMedicationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("/edu/qut/cab302/wehab/medication/Medication-Search.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load medication search page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        viewSummaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String medicationIdToSearch = userSavedMedications.get(selectedMedicationLabel.getText());

                System.out.println("Loading view summary page with medication ID: " + medicationIdToSearch);
                MedicationInfoPage medicationInfoPage = new MedicationInfoPage(medicationIdToSearch);
                Scene scene = medicationInfoPage.getMedicationInfoPage();

                medicationOverviewModal.initModality(Modality.APPLICATION_MODAL);
                medicationOverviewModal.setResizable(false);
                medicationOverviewModal.setScene(scene);
                medicationOverviewModal.showAndWait();
            }
        });

        notificationPopupModal.initModality(Modality.APPLICATION_MODAL);
        notificationPopupModal.setResizable(false);
        createReminderButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Label statusMessage;

                String medicationId = null;
                String amount = null;
                String unit = null;
                String date = null;
                String time = null;

                try {
                    medicationId = userSavedMedications.get(selectedMedicationLabel.getText());
                    amount = dosageTextField.getText();
                    unit = unitComboBox.getValue().toString();
                    date = datePicker.getValue().toString();
                    time = timeSpinner.getValue().toString();
                } catch (NullPointerException e) {
                    statusMessage = new Label("Unable to create reminder. Ensure all fields are specified.");
                    Button okButton = new Button("OK");
                    okButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            notificationPopupModal.close();
                        }
                    });
                    VBox vbox = new VBox(statusMessage, okButton);
                    vbox.setPadding(new Insets(10, 10, 10, 10));
                    vbox.setSpacing(10);
                    vbox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(vbox);
                    notificationPopupModal.setScene(scene);
                    notificationPopupModal.showAndWait();
                }

                if (!medicationId.isEmpty() && !amount.isEmpty() && !unit.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                    try {
                        MedicationSearchModel.addReminder(medicationId, amount, unit, date, time);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    statusMessage = new Label("Reminder added!");
                    Button okButton = new Button("OK");
                    okButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            notificationPopupModal.close();
                        }
                    });
                    VBox vbox = new VBox(statusMessage, okButton);
                    vbox.setPadding(new Insets(10, 10, 10, 10));
                    vbox.setSpacing(10);
                    vbox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(vbox);
                    notificationPopupModal.setScene(scene);
                    notificationPopupModal.showAndWait();
                }
            }
        });
//
//        editReminderButton.setOnAction(new EventHandler<ActionEvent>() {
//
//            public void handle(ActionEvent event) {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("medication-reminder-edit.fxml"));
//                String sceneTitle = " - Edit Reminder";
//
//                Scene scene;
//                try {
//                    scene = new Scene(fxmlLoader.load(), 640, 400);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
////                MedicationSearchController.changeMedicationOverviewModalScene(scene,sceneTitle);
//            }
//        });

        dosageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                dosageTextField.setText(oldValue);
            }
        });

        try {
            userSavedMedications = MedicationSearchModel.getUserSavedMedicationNames();
            System.out.println("Fetched Medications: " + userSavedMedications.keySet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        refreshResultsWindow();

        resultsWindow.setCellFactory(lv -> new ListCell<String>() {
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Button deleteButton = new Button("x");
            private final Region spacer = new Region();

            {
                hbox.setSpacing(10);
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(label, spacer, deleteButton);
                deleteButton.setOnAction(event -> {
                    String medicationToRemove = getItem();
                    try {

                        if (medicationToRemove != null) {
                            System.out.print("Removing medication: " + medicationToRemove + "...");
                            MedicationSearchModel.deleteUserSavedMedication(userSavedMedications.get(medicationToRemove));
                            userSavedMedications.remove(medicationToRemove);
                            System.out.println("done.");

                            refreshResultsWindow();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(hbox);
                }
            }
        });


        if (!resultsWindow.getItems().isEmpty()) {
            resultsWindow.getSelectionModel().select(0);
            selectedMedicationLabel.setText(resultsWindow.getSelectionModel().getSelectedItem().toString());
        } else {
            selectedMedicationLabel.setText("No Saved Medications");
        }

        resultsWindow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedMedicationLabel.setText(newValue.toString());
            } else {
                selectedMedicationLabel.setText("No Saved Medications");
            }
        });

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime initialValue = LocalTime.of(12, 0); // 12:00 PM
        LocalTime minTime = LocalTime.of(0, 0);       // 00:00 AM
        LocalTime maxTime = LocalTime.of(23, 59);     // 23:59 PM

        SpinnerValueFactory<LocalTime> timeValueFactory = new SpinnerValueFactory<>() {
            {
                setValue(initialValue);
            }

            @Override
            public void decrement(int steps) {
                LocalTime currentTime = getValue();
                setValue(currentTime.minusMinutes(steps * 15));
            }

            @Override
            public void increment(int steps) {
                LocalTime currentTime = getValue();
                setValue(currentTime.plusMinutes(steps * 15));
            }
        };

        timeSpinner.setValueFactory(timeValueFactory);

        timeSpinner.setEditable(true);
        timeSpinner.getEditor().setTextFormatter(new TextFormatter<>(c -> {
            try {
                LocalTime.parse(c.getControlNewText(), timeFormatter);
                return c;
            } catch (Exception e) {
                return null;
            }
        }));
    }
}