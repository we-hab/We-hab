package edu.qut.cab302.wehab;

import edu.qut.cab302.wehab.workout.WorkoutReturnModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

import java.io.IOException;

/**
 * Main entry point for the application.
 * Initializes the application and launches the user interface.
 */

public class MainApplication extends Application {

    private static Stage primaryStage;
    private static String activeStyleSheet = "MainStyleSheet.css";  // Default stylesheet
    private static String activeTextSizeSheet = "MainStyleSheet.css";  // Default text size

    /**
     * Starts the application by setting the primary stage and switching to the login scene.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If there is an error loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException  {
        primaryStage = stage;
        primaryStage.setTitle("We-Hab"); // Set the title here

        // Set the minimum width and height for the application window
        primaryStage.setMinWidth(1280); // Set minimum width
        primaryStage.setMinHeight(800);  // Set minimum height

        UserAccountDAO userAccountDAO = new UserAccountDAO();

//        Change this to your username to skip the login screen when testing
//        UserAccount loggedInUser = userAccountDAO.getByUsername("test");
//        Session.getInstance().setLoggedInUser(loggedInUser);
        switchScene("Login.fxml");
    }

    /**
     * Main method for launching the application.
     * Establishes the database connection and creates necessary tables.
     */
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance(); // Connects to the database
        DatabaseConnection.createTable();
        moodRating.createMoodTable();
        WorkoutReturnModel.createWorkoutTable();
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        launch();
    }

    /**
     * Switches the current scene to a new FXML layout.
     * Also applies the active stylesheets to the new scene.
     *
     * @param fxmlFile The name of the FXML file to load.
     * @throws IOException If there is an error loading the FXML file.
     */
    public static void switchScene(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        // Apply both the active base stylesheet and the text size stylesheet
        scene.getStylesheets().add(MainApplication.class.getResource(activeStyleSheet).toExternalForm());
        scene.getStylesheets().add(MainApplication.class.getResource(activeTextSizeSheet).toExternalForm());

        Stage stage = primaryStage; // Assuming primaryStage is accessible here

        // Preserve the current size of the stage
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        stage.setScene(scene);

        // Set the stage size to the previous size
        stage.setWidth(currentWidth);
        stage.setHeight(currentHeight);

        stage.show();
    }

    /**
     * @return The file name of the active base stylesheet.
     */
    public static String getActiveStyleSheet() {
        return activeStyleSheet;
    }

    /**
     *
     * @return The file name of the active text size stylesheet.
     */
    public static String getActiveTextSizeSheet() {
        return activeTextSizeSheet;
    }

    /**
     * Sets the active base stylesheet and updates the current scene's styles.
     *
     * @param styleSheet The file name of the new base stylesheet.
     */
    public static void setActiveStyleSheet(String styleSheet) {
        activeStyleSheet = styleSheet;
        updateCurrentSceneStyles();
    }

    /**
     * Sets the active text size stylesheet and updates the current scene's styles.
     *
     * @param textSizeSheet The file name of the new text size stylesheet.
     */
    public static void setActiveTextSize(String textSizeSheet) {
        activeTextSizeSheet = textSizeSheet;
        updateCurrentSceneStyles();
    }

    /**
     * Utility method to reapply the active stylesheets to the current scene.
     * Clears existing styles and applies the active base and text size stylesheets.
     */
    private static void updateCurrentSceneStyles() {
        Scene currentScene = primaryStage.getScene();
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(MainApplication.class.getResource(activeStyleSheet).toExternalForm());
        currentScene.getStylesheets().add(MainApplication.class.getResource(activeTextSizeSheet).toExternalForm());
    }
}
