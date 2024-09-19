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
    private Button dashboardButton;
    @FXML
    private Button workoutButton;
    @FXML
    private Button medicationButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Label loggedInUserLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ButtonController.initialiseButtons(dashboardButton, workoutButton, medicationButton, null, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String firstName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(firstName + "!");
        } else
        {
            loggedInUserLabel.setText("Error");
        }
    }
}
