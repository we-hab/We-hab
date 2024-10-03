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

        // Dynamically select the text size toggle button based on the active text size stylesheet
        String currentTextSize = MainApplication.getActiveTextSizeSheet();
        switch (currentTextSize) {
            case "MainStyleSheet.css":
            case "accessible.css":
                defaultText.setSelected(true);
                break;
            case "MainStyleSheet_large.css":
            case "accessible_large.css":
                largeText.setSelected(true);
                break;
            case "MainStyleSheet_extraLarge.css":
            case "accessible_extraLarge.css":
                extraText.setSelected(true);
                break;
            default:
                defaultText.setSelected(true);  // Default to regular text size
                break;
        }

        ButtonController.initialiseButtons(dashboardButton, workoutButton, medicationButton, null, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname);
        } else {
            loggedInUserLabel.setText("Error");
        }

        // Handle the selection of style (theme)
        defaultStyle.setOnAction(event -> {
            MainApplication.setActiveStyleSheet("MainStyleSheet.css");
            applyTextSizeBasedOnSelection();
        });

        accessibleStyle.setOnAction(event -> {
            MainApplication.setActiveStyleSheet("accessible.css");
            applyTextSizeBasedOnSelection();
        });

        // Handle the selection of text size
        defaultText.setOnAction(event -> applyTextSizeBasedOnSelection());
        largeText.setOnAction(event -> applyTextSizeBasedOnSelection());
        extraText.setOnAction(event -> applyTextSizeBasedOnSelection());
    }

    // Helper method to apply text size based on the selected style and size
    private void applyTextSizeBasedOnSelection() {
        if (defaultStyle.isSelected()) {
            if (defaultText.isSelected()) {
                MainApplication.setActiveTextSize("MainStyleSheet.css");
            } else if (largeText.isSelected()) {
                MainApplication.setActiveTextSize("MainStyleSheet_large.css");
            } else if (extraText.isSelected()) {
                MainApplication.setActiveTextSize("MainStyleSheet_extraLarge.css");
            }
        } else if (accessibleStyle.isSelected()) {
            if (defaultText.isSelected()) {
                MainApplication.setActiveTextSize("accessible.css");
            } else if (largeText.isSelected()) {
                MainApplication.setActiveTextSize("accessible_large.css");
            } else if (extraText.isSelected()) {
                MainApplication.setActiveTextSize("accessible_extraLarge.css");
            }
        }
    }

}