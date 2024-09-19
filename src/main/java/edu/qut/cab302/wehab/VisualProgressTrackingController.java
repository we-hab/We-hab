package edu.qut.cab302.wehab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class VisualProgressTrackingController implements Initializable {

    @FXML
    private Label loggedInUserLabel;

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
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String firstName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(firstName + "!");
        }
        else
        {
            loggedInUserLabel.setText("Error");
        }
    }
}
