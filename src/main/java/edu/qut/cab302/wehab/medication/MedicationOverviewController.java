package edu.qut.cab302.wehab.medication;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import java.io.IOException;

public class MedicationOverviewController {

    @FXML
    private Button createReminderButton;

    @FXML
    private Button editReminderButton;

    @FXML
    public void initialize() {

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
}
