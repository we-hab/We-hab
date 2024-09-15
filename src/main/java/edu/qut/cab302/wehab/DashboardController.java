package edu.qut.cab302.wehab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import edu.qut.cab302.wehab.MainApplication;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button medicationButton;

    public void initialize() {
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
    }
}
