package edu.qut.cab302.wehab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
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
}
