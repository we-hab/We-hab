package edu.qut.cab302.wehab.dashboard;

import edu.qut.cab302.wehab.MainApplication;
import edu.qut.cab302.wehab.database.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Responsible for initializing button actions in the application.
 * It assigns event handlers to various buttons for navigating between scenes.
 */
public class ButtonController {

    /**
     * Initializes the provided buttons by setting their event handlers for scene navigation.
     * Each button will switch to a different scene when clicked, or sign out the user if signOut is clicked.
     *
     * @param dashboard  Button to navigate to the dashboard scene.
     * @param workout    Button to navigate to the workout scene.
     * @param medication Button to navigate to the medication search scene.
     * @param settings   Button to navigate to the settings scene.
     * @param signOut    Button to log out the user and navigate to the login screen.
     */
    public static void initialiseButtons(Button dashboard, Button workout, Button medication, Button settings, Button signOut) {

        if (dashboard != null) {
            dashboard.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        MainApplication.switchScene("dashboard.fxml");
                    } catch (IOException e) {
                        System.out.println("Failed to load the dashboard.\n" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        if (workout != null) {
            workout.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        MainApplication.switchScene("Visual-Progress-Tracking.fxml");
                    } catch (IOException e) {
                        System.out.println("Failed to load medication page.\n" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        if (medication != null) {
            medication.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try {
                        MainApplication.switchScene("medication/medication-overview.fxml");
                    } catch (IOException e) {
                        System.out.println("Failed to load medication page.\n" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        if (settings != null) {
            settings.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        MainApplication.switchScene("settings-page.fxml");
                    } catch (IOException e) {
                        System.out.println("Failed to load settings page.\n" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Step 1: Clear session
                Session.getInstance().clearLoggedInUser();

                // Step 2: Navigate to the login screen
                try {
                    MainApplication.switchScene("/edu/qut/cab302/wehab/user_account/Login.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load login page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });


    }

}
