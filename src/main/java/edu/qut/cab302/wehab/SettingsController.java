package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private Button workoutButton;
    @FXML
    private Button medicationButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Label loggedInUserLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Workout button action
        workoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("Visual-Progress-Tracking.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load workout page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        // Medication button action
        medicationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("medication/Medication-Search.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load medication page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        // Dashboard button action
        dashboardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("dashboard.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load dashboard page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname);
        } else
        {
            loggedInUserLabel.setText("Error");
        }
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        // Step 1: Clear session
        Session.getInstance().clearLoggedInUser();

        // Step 2: Navigate to the login screen
        try {
            MainApplication.switchScene("Login.fxml");  // Adjust the path based on your structure
        } catch (IOException e) {
            System.out.println("Failed to load login page.\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
