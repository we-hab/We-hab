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

    @Override
    public void start(Stage stage) throws IOException {
        // Change this line to sandbox your build
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        scene.getStylesheets().add(MainApplication.class.getResource("MainStyleSheet.css").toExternalForm());
        stage.setTitle("We-Hab");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        Connection connection = DatabaseConnection.getInstance(); // Connects to the database
        DatabaseConnection.createTable();
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        launch();
    }

    public static void switchScene(String fxmlFile) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        scene.getStylesheets().add(MainApplication.class.getResource("MainStyleSheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
