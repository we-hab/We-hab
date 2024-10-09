package edu.qut.cab302.wehab.medication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class EditReminderController {

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
//                MedicationSearchController.returnModalToMainScreen();
            }
        });
    }
}
