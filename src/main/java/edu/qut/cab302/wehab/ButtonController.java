package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.io.IOException;

public class ButtonController {

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
                        MainApplication.switchScene("medication/Medication-Search.fxml");
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
                    MainApplication.switchScene("Login.fxml");  // Adjust the path based on your structure
                } catch (IOException e) {
                    System.out.println("Failed to load login page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });


    }

}
