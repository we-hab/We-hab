package edu.qut.cab302.wehab;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

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
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private Button updatePasswordButton;
    @FXML
    private Button deleteAccountButton;

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
        addEnterKeyHandler(defaultStyle);

        accessibleStyle.setOnAction(event -> {
            MainApplication.setActiveStyleSheet("accessible.css");
            applyTextSizeBasedOnSelection();
        });
        addEnterKeyHandler(accessibleStyle);

        // Handle the selection of text size
        defaultText.setOnAction(event -> applyTextSizeBasedOnSelection());
        addEnterKeyHandler(defaultText);

        largeText.setOnAction(event -> applyTextSizeBasedOnSelection());
        addEnterKeyHandler(largeText);

        extraText.setOnAction(event -> applyTextSizeBasedOnSelection());
        addEnterKeyHandler(extraText);

        // Set action for the password change button
        updatePasswordButton.setOnAction(event -> {
            try {
                changePassword();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Set action for delete account button
        deleteAccountButton.setOnAction(event -> {
            showDeleteAccountConfirmation();
        });
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

    private void changePassword() throws IOException {
        String currentPassword = oldPassword.getText();
        String newPass = newPassword.getText();

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            UserAccountDAO userAccountDAO = new UserAccountDAO();

            // Validate the old password
            if (userAccountDAO.validatePassword(loggedInUser.getUsername(), currentPassword)) {
                // Update the password
                boolean updateSuccess = userAccountDAO.updatePassword(loggedInUser.getUsername(), newPass);
                if (updateSuccess) {
                    showAlert("Password Change", "Password successfully updated!");
                } else {
                    showAlert("Password Change", "Failed to update password.");
                }
            } else {
                showAlert("Password Change", "Old password is incorrect.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDeleteAccountConfirmation() {
        // Create confirmation alert
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "This action cannot be undone.", deleteButtonType, cancelButtonType);
        confirmationAlert.setTitle("Delete Account");
        confirmationAlert.setHeaderText("Are you sure you want to delete your account?");

        // Wait for the user’s response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == deleteButtonType) {
                // User confirmed, proceed with account deletion
                try {
                    deleteAccount();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // User canceled, do nothing
                confirmationAlert.close();
            }
        });
    }

    private void deleteAccount() throws IOException {
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            UserAccountDAO userAccountDAO = new UserAccountDAO();
            boolean isDeleted = userAccountDAO.deleteAccount(loggedInUser.getUsername());

            if (isDeleted) {
                showAlert("Account Deletion", "Your account has been successfully deleted.");
                // log out the user and redirect to the login page
                Session.getInstance().clearLoggedInUser();
                MainApplication.switchScene("Login.fxml");
            } else {
                showAlert("Account Deletion", "Failed to delete your account. Please try again.");
            }
        }
    }

    private void addEnterKeyHandler(ToggleButton toggleButton) {
        toggleButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                toggleButton.fire(); // Simulates the action of clicking the button
            }
        });
    }

}