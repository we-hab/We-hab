package edu.qut.cab302.wehab.medication;

import edu.qut.cab302.wehab.MainApplication;
import edu.qut.cab302.wehab.dashboard.ButtonController;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.user_account.UserAccount;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static edu.qut.cab302.wehab.medication.MedicationSearchModel.createMedicationTables;

public class MedicationOverviewController {

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
    private Spinner<LocalTime> timeSpinner;
    @FXML
    private Spinner<Integer> minutesSpinner;

    private ArrayList<String> userSavedMedications;

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

//        createReminderButton.setOnAction(new EventHandler<ActionEvent>() {
//
//            public void handle(ActionEvent event) {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("medication-reminder-create.fxml"));
//                String sceneTitle = " - Create Reminder";
//
//                Scene scene;
//                try {
//                    scene = new Scene(fxmlLoader.load(), 640, 400);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
////                MedicationSearchController.changeMedicationOverviewModalScene(scene, sceneTitle);
//
//            }
//
//        });
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
            System.out.println("Fetched Medications: " + userSavedMedications);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(String medicationName : userSavedMedications) {
            resultsWindow.getItems().add(medicationName);
        }

        if (!resultsWindow.getItems().isEmpty()) {
            resultsWindow.getSelectionModel().select(0);
            selectedMedicationLabel.setText(resultsWindow.getSelectionModel().getSelectedItem().toString());
        } else {
            selectedMedicationLabel.setText("No Saved Medications");
        }

        resultsWindow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMedicationLabel.setText(newValue.toString());
        });

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Define the initial value, minimum, and maximum times
        LocalTime initialValue = LocalTime.of(12, 0); // 12:00 PM
        LocalTime minTime = LocalTime.of(0, 0);       // 00:00 AM
        LocalTime maxTime = LocalTime.of(23, 59);     // 23:59 PM

        // Create a SpinnerValueFactory that increments and decrements the time by 15 minutes
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

        // Display the time in a readable format
        timeSpinner.setEditable(true);
        timeSpinner.getEditor().setTextFormatter(new javafx.scene.control.TextFormatter<>(c -> {
            try {
                LocalTime.parse(c.getControlNewText(), timeFormatter);
                return c;
            } catch (Exception e) {
                return null; // Invalid input will not be committed
            }
        }));


    }
}