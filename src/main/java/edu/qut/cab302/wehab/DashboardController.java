package edu.qut.cab302.wehab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import edu.qut.cab302.wehab.MainApplication;


public class DashboardController implements Initializable {

    @FXML
    private Button workoutButton;

    @FXML
    private Button medicationButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutButton;

    @FXML
    private Label loggedInUserLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname);
        } else
        {
            loggedInUserLabel.setText("Error");
        }

        ButtonController.initialiseButtons(null, workoutButton, medicationButton, settingsButton, signOutButton);

    }
}