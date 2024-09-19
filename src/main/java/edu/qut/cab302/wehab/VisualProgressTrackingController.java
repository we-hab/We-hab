package edu.qut.cab302.wehab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class VisualProgressTrackingController implements Initializable {

    @FXML
    private Button dashboardButton;

    @FXML
    private Button medicationButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ButtonController.initialiseButtons(dashboardButton, null, medicationButton, settingsButton, signOutButton);

    }
}
