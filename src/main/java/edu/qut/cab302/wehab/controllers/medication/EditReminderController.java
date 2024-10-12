package edu.qut.cab302.wehab.controllers.medication;

import edu.qut.cab302.wehab.models.dao.MedicationDAO;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class EditReminderController {

    @FXML
    Button updateReminderButton;

    @FXML
    Button cancelButton;

    @FXML
    private Label selectedReminderLabel;

    @FXML
    private TextField dosageTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox unitComboBox;

    @FXML
    private Spinner<LocalTime> timeSpinner;

    private MedicationOverviewController controllerInstance = MedicationOverviewController.getInstance();

    Stage stage;

    @FXML
    public void initialize() {

        MedicationReminder selectedReminder = controllerInstance.getSelectedReminder();

        selectedReminderLabel.setText(selectedReminder.getDisplayName());

        dosageTextField.setText(String.valueOf(selectedReminder.getDosageAmount()));
        unitComboBox.setValue(selectedReminder.getDosageUnit());
        datePicker.setValue(selectedReminder.getDosageDate());

        SpinnerValueFactory<LocalTime> timeValueFactory = controllerInstance.getTimeValueFactory();
        timeSpinner.setValueFactory(timeValueFactory);
        timeSpinner.getValueFactory().setValue(selectedReminder.getDosageTime());

        updateReminderButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                Double amount = null;
                String unit = null;
                LocalDate date = null;
                LocalTime time = null;

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

                if (amount != null && unit != null && time != null) {

                    selectedReminder.setDosageAmount(amount);
                    selectedReminder.setDosageUnit(unit);
                    selectedReminder.setDosageDate(date);
                    selectedReminder.setDosageTime(time);

                    try {
                        MedicationDAO.updateReminder(selectedReminder);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setContentText("Reminder updated!");
                    alert.showAndWait();
                    stage = (Stage) updateReminderButton.getScene().getWindow();
                    stage.close();
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }
}
