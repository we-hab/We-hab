package edu.qut.cab302.wehab.controllers.medication;

import edu.qut.cab302.wehab.dao.MedicationDAO;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller class for handling the editing of a medication reminder.
 * This class allows users to modify and update the details of a selected medication reminder.
 */
public class EditReminderController {

    @FXML
    private Button updateReminderButton;  // Button to update the reminder

    @FXML
    private Button cancelButton;  // Button to cancel the edit and close the window

    @FXML
    private Label selectedReminderLabel;  // Label displaying the selected reminder's name

    @FXML
    private TextField dosageTextField;  // Text field for entering the dosage amount

    @FXML
    private DatePicker datePicker;  // Date picker to select the reminder date

    @FXML
    private ComboBox unitComboBox;  // Combo box to select the unit for dosage

    @FXML
    private Spinner<LocalTime> timeSpinner;  // Spinner to select the reminder time

    // Instance of MedicationOverviewController to access selected reminder details
    private MedicationOverviewController controllerInstance = MedicationOverviewController.getInstance();

    private Stage stage;  // Current stage reference for closing the window

    /**
     * Initializes the controller when the scene is loaded.
     * Sets up the fields with the selected reminder data and handles button actions.
     */
    @FXML
    public void initialize() {
        MedicationReminder selectedReminder = controllerInstance.getSelectedReminder();

        // Populate UI elements with the selected reminder's data
        selectedReminderLabel.setText(selectedReminder.getDisplayName());
        dosageTextField.setText(String.valueOf(selectedReminder.getDosageAmount()));
        unitComboBox.setValue(selectedReminder.getDosageUnit());
        datePicker.setValue(selectedReminder.getDosageDate());

        // Set up the spinner for the time input
        SpinnerValueFactory<LocalTime> timeValueFactory = controllerInstance.getTimeValueFactory();
        timeSpinner.setValueFactory(timeValueFactory);
        timeSpinner.getValueFactory().setValue(selectedReminder.getDosageTime());

        // Handle the update button action
        updateReminderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Double amount = null;
                String unit = null;
                LocalDate date = null;
                LocalTime time = null;

                // Try to parse user input and ensure all fields are filled
                try {
                    amount = Double.parseDouble(dosageTextField.getText());
                    unit = unitComboBox.getValue().toString();
                    date = datePicker.getValue();
                    time = timeSpinner.getValue();
                } catch (NullPointerException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setContentText("All fields must be filled.");
                    alert.showAndWait();
                }

                // Update reminder if all fields are valid
                if (amount != null && unit != null && time != null) {
                    selectedReminder.setDosageAmount(amount);
                    selectedReminder.setDosageUnit(unit);
                    selectedReminder.setDosageDate(date);
                    selectedReminder.setDosageTime(time);

                    try {
                        MedicationDAO.updateReminder(selectedReminder);  // Update the reminder in the database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    // Show confirmation alert and close the window
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setContentText("Reminder updated!");
                    alert.showAndWait();
                    stage = (Stage) updateReminderButton.getScene().getWindow();
                    stage.close();
                }
            }
        });

        // Handle the cancel button action
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();  // Close the window without saving changes
            }
        });
    }
}
