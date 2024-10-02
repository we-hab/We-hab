package edu.qut.cab302.wehab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

import java.io.IOException;
import java.util.Objects;

/**
 * Main entry point for the application.
 * Initializes the application and launches the user interface.
 */

public class MainApplication extends Application {

    private static Stage primaryStage;
    private static String activeStyleSheet = "MainStyleSheet.css";  // Default stylesheet

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        switchScene("Login.fxml"); // Start with the login page
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance(); // Connects to the database
        DatabaseConnection.createTable();
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        launch();
    }

    public static void switchScene(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);

        // Apply the currently active stylesheet
        scene.getStylesheets().add(MainApplication.class.getResource(activeStyleSheet).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String getActiveStyleSheet() {
        return activeStyleSheet;
    }

    // Method to change the active stylesheet globally
    public static void setActiveStyleSheet(String styleSheet) {
        activeStyleSheet = styleSheet;

        // Apply the new stylesheet to the current scene
        Scene currentScene = primaryStage.getScene();
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(MainApplication.class.getResource(activeStyleSheet).toExternalForm());
    }
}
