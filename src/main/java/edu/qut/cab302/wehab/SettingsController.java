package edu.qut.cab302.wehab;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

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
    @FXML
    private ToggleButton defaultStyle;
    @FXML
    private ToggleButton accessibleStyle;
    @FXML
    private ToggleButton defaultText;
    @FXML
    private ToggleButton largeText;
    @FXML
    private ToggleButton extraText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Create a ToggleGroup for the buttons to ensure only one can be selected at a time
        ToggleGroup styleToggleGroup = new ToggleGroup();
        defaultStyle.setToggleGroup(styleToggleGroup);
        accessibleStyle.setToggleGroup(styleToggleGroup);

        // Dynamically select the toggle button based on the currently applied stylesheet
        String currentStyle = MainApplication.getActiveStyleSheet();
        if (currentStyle.equals("MainStyleSheet.css")) {
            defaultStyle.setSelected(true);
        } else if (currentStyle.equals("accessible.css")) {
            accessibleStyle.setSelected(true);
        }

        ToggleGroup textToggleGroup = new ToggleGroup();
        defaultText.setToggleGroup(textToggleGroup);
        largeText.setToggleGroup(textToggleGroup);
        extraText.setToggleGroup(textToggleGroup);

        ButtonController.initialiseButtons(dashboardButton, workoutButton, medicationButton, null, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname);
        } else {
            loggedInUserLabel.setText("Error");
        }

        URL defaultStyleURL = getClass().getResource("/edu/qut/cab302/wehab/MainStyleSheet.css");
        URL accessibleStyleURL = getClass().getResource("/edu/qut/cab302/wehab/accessible.css");


        // Set up action for Default Style button
        defaultStyle.setOnAction(event -> {
            if (defaultStyleURL != null) {
                MainApplication.setActiveStyleSheet("MainStyleSheet.css"); // Apply default style globally
            }
        });

        // Set up action for Accessible Style button
        accessibleStyle.setOnAction(event -> {
            if (accessibleStyleURL != null) {
                MainApplication.setActiveStyleSheet("accessible.css"); // Apply accessible style globally
            }
        });
    }


}