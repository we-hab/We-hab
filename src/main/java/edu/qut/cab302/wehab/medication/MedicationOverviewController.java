package edu.qut.cab302.wehab.medication;

import edu.qut.cab302.wehab.MainApplication;
import edu.qut.cab302.wehab.MainController;
import edu.qut.cab302.wehab.dashboard.ButtonController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static edu.qut.cab302.wehab.MainApplication.switchScene;

public class MedicationOverviewController {

    @FXML
    private Button dashboardButton, workoutButton,settingsButton, signOutButton;

    @FXML
    private Button addMedicationButton;

    @FXML
    private Button createReminderButton;

    @FXML
    private Button editReminderButton;

    @FXML
    public void initialize() {

        initializeButtons();

        // Set event handler to switch to the medication search screen
        addMedicationButton.setOnAction(event -> {
            try {
                MainApplication.switchScene("/edu/qut/cab302/wehab/medication/Medication-Search.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createReminderButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("medication-reminder-create.fxml"));
                String sceneTitle = " - Create Reminder";

                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 640, 400);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                MedicationSearchController.changeMedicationOverviewModalScene(scene, sceneTitle);

            }

        });


        editReminderButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("medication-reminder-edit.fxml"));
                String sceneTitle = " - Edit Reminder";

                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 640, 400);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                MedicationSearchController.changeMedicationOverviewModalScene(scene,sceneTitle);

            }

        });
    }

    /**
     * Initializes the buttons in the settings interface by calling the
     * ButtonController's initialization method, which sets up the functionality
     * for the dashboard, workout, medication, and sign-out buttons.
     */
    private void initializeButtons() {
        ButtonController.initialiseButtons(dashboardButton, workoutButton, null, settingsButton, signOutButton);
    }

}
