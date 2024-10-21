package edu.qut.cab302.wehab.controllers.medication;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * Controller class for handling the "Create Reminder" screen in the application.
 * This controller manages the back button functionality.
 */
public class CreateReminderController {

    @FXML
    private Button backButton;  // Button to return to the previous screen

    /**
     * Initializes the controller. This method is called when the scene is loaded.
     * It sets the action for the back button to navigate back to the previous screen.
     */
    @FXML
    public void initialize() {
        // Set action for the back button to handle navigation
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Logic to navigate back to the main medication modal scene (currently commented out)
                // MedicationSearchController.mainMedicationModalScene;
            }
        });
    }
}
