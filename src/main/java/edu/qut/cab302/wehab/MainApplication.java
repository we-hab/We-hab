package edu.qut.cab302.wehab;

import edu.qut.cab302.wehab.workout.WorkoutReturnModel;
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
    private static String activeTextSizeSheet = "MainStyleSheet.css";  // Default text size

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        switchScene("Login.fxml"); // Start with the login page
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance(); // Connects to the database
        DatabaseConnection.createTable();
        moodRating.createMoodTable();
        WorkoutReturnModel.createWorkoutTable();
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        launch();
    }

    public static void switchScene(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);

        // Apply both the active base stylesheet and the text size stylesheet
        scene.getStylesheets().add(MainApplication.class.getResource(activeStyleSheet).toExternalForm());
        scene.getStylesheets().add(MainApplication.class.getResource(activeTextSizeSheet).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String getActiveStyleSheet() {
        return activeStyleSheet;
    }

    public static String getActiveTextSizeSheet() {
        return activeTextSizeSheet;
    }

    // Method to change the base theme globally
    public static void setActiveStyleSheet(String styleSheet) {
        activeStyleSheet = styleSheet;
        updateCurrentSceneStyles();
    }

    // Method to change the active text size globally
    public static void setActiveTextSize(String textSizeSheet) {
        activeTextSizeSheet = textSizeSheet;
        updateCurrentSceneStyles();
    }

    // Utility method to reapply stylesheets to the current scene
    private static void updateCurrentSceneStyles() {
        Scene currentScene = primaryStage.getScene();
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(MainApplication.class.getResource(activeStyleSheet).toExternalForm());
        currentScene.getStylesheets().add(MainApplication.class.getResource(activeTextSizeSheet).toExternalForm());
    }
}
