package edu.qut.cab302.wehab.controllers.medication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class CreateReminderController {

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent event) {
//               MedicationSearchController.mainMedicationModalScene;
           }
        });
    }
}