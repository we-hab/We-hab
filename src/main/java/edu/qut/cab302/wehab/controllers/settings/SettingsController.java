package edu.qut.cab302.wehab.controllers.settings;

import edu.qut.cab302.wehab.controllers.dashboard.ButtonController;
import edu.qut.cab302.wehab.main.MainApplication;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import edu.qut.cab302.wehab.dao.UserAccountDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    /**
     * Initializes the settings controller. This method sets up toggle buttons for themes and text sizes,
     * handles button actions, and displays the logged-in user's name.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupStyleToggleGroup();
        setupTextToggleGroup();
        initializeButtons();
        displayLoggedInUser();
        setupStyleButtonActions();
        setupTextButtonActions();
        setupAccountButtons();

    }

    /**
     * Sets up the toggle group for the style selection buttons (default and accessible styles).
     * This method initializes the toggle group and sets the selected button based on the
     * current active stylesheet.
     */
    private void setupStyleToggleGroup() {
        ToggleGroup styleToggleGroup = new ToggleGroup();
        defaultStyle.setToggleGroup(styleToggleGroup);
        accessibleStyle.setToggleGroup(styleToggleGroup);

        String currentStyle = MainApplication.getActiveStyleSheet();
        defaultStyle.setSelected(currentStyle.equals("/edu/qut/cab302/wehab/css/MainStyleSheet.css"));
        accessibleStyle.setSelected(currentStyle.equals("/edu/qut/cab302/wehab/css/accessible.css"));
    }

    /**
     * Sets up the toggle group for the text size selection buttons (default, large, and extra-large text sizes).
     * This method initializes the toggle group and updates the selection based on the current text size.
     */
    private void setupTextToggleGroup() {
        ToggleGroup textToggleGroup = new ToggleGroup();
        defaultText.setToggleGroup(textToggleGroup);
        largeText.setToggleGroup(textToggleGroup);
        extraText.setToggleGroup(textToggleGroup);

        updateTextSizeSelection(); // Call to update the active selection
    }

    /**
     * Updates the selection of text size toggle buttons based on the current active text size.
     * This method checks the active text size stylesheet and selects the corresponding toggle button.
     */
    private void updateTextSizeSelection() {
        String currentTextSize = MainApplication.getActiveTextSizeSheet();
        defaultText.setSelected(currentTextSize.startsWith("MainStyleSheet") || currentTextSize.startsWith("accessible"));
        largeText.setSelected(currentTextSize.endsWith("large.css"));
        extraText.setSelected(currentTextSize.endsWith("extraLarge.css"));
    }

    /**
     * Initializes the buttons in the settings interface by calling the
     * ButtonController's initialization method, which sets up the functionality
     * for the dashboard, workout, medication, and sign-out buttons.
     */
    private void initializeButtons() {
        ButtonController.initialiseButtons(dashboardButton, workoutButton, medicationButton, null, signOutButton);
    }

    /**
     * Displays the logged-in user's name in the user interface.
     * If no user is logged in, it displays "Error".
     */
    private void displayLoggedInUser() {
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();
        String fullname = (loggedInUser != null) ? loggedInUser.getFirstName() : "Error";
        loggedInUserLabel.setText(fullname);
    }

    /**
     * Sets up actions for the style selection buttons (default and accessible styles).
     * This method calls the setupButtonAction method to define the actions for
     * each button when selected.
     */
    private void setupStyleButtonActions() {
        setupButtonAction(defaultStyle, "/edu/qut/cab302/wehab/css/MainStyleSheet.css");
        setupButtonAction(accessibleStyle, "/edu/qut/cab302/wehab/css/accessible.css");
    }

    /**
     * Sets up actions for the text size selection buttons (default, large, and extra-large text sizes).
     * This method calls the setupButtonAction method for each button to define their actions.
     */
    private void setupTextButtonActions() {
        setupButtonAction(defaultText);
        setupButtonAction(largeText);
        setupButtonAction(extraText);
    }

    /**
     * Sets up an action for the specified toggle button that changes the active stylesheet
     * when the button is selected. This method also adds an event handler for the Enter key
     * to trigger the button action.
     *
     * @param button The toggle button to set up.
     * @param style  The stylesheet to apply when this button is selected.
     */
    private void setupButtonAction(ToggleButton button, String style) {
        button.setOnAction(event -> {
            MainApplication.setActiveStyleSheet(style);
            applyTextSizeBasedOnSelection();
        });
        addEnterKeyHandler(button);
    }

    /**
     * Sets up an action for the specified toggle button that applies the current text size
     * based on the selection. This method also adds an event handler for the Enter key
     * to trigger the button action.
     *
     * @param button The toggle button to set up.
     */
    private void setupButtonAction(ToggleButton button) {
        button.setOnAction(event -> applyTextSizeBasedOnSelection());
        addEnterKeyHandler(button);
    }

    /**
     * Sets up actions for account management buttons, including updating the password and
     * deleting the account. This method defines the behavior when the buttons are pressed.
     */
    private void setupAccountButtons() {
        updatePasswordButton.setOnAction(event -> {
            try {
                changePassword();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        deleteAccountButton.setOnAction(event -> showDeleteAccountConfirmation());
    }

    /**
     * Applies the selected text size and theme (default or accessible) to the application.
     * The method updates the stylesheet based on the current selection of the toggle buttons.
     */
    private void applyTextSizeBasedOnSelection() {
        if (defaultStyle.isSelected()) {
            if (defaultText.isSelected()) {
                MainApplication.setActiveTextSize("/edu/qut/cab302/wehab/css/MainStyleSheet.css");
            } else if (largeText.isSelected()) {
                MainApplication.setActiveTextSize("/edu/qut/cab302/wehab/css/MainStyleSheet_large.css");
            } else if (extraText.isSelected()) {
                MainApplication.setActiveTextSize("/edu/qut/cab302/wehab/css/MainStyleSheet_extraLarge.css");
            }
        } else if (accessibleStyle.isSelected()) {
            if (defaultText.isSelected()) {
                MainApplication.setActiveTextSize("/edu/qut/cab302/wehab/css/accessible.css");
            } else if (largeText.isSelected()) {
                MainApplication.setActiveTextSize("/edu/qut/cab302/wehab/css/accessible_large.css");
            } else if (extraText.isSelected()) {
                MainApplication.setActiveTextSize("/edu/qut/cab302/wehab/css/accessible_extraLarge.css");
            }
        }
    }
    /**
     * Changes the user's password by verifying the old password and updating it with the new one.
     * If successful, a confirmation dialog is shown. If the old password is incorrect or the update fails,
     * an error message is displayed.
     *
     * @throws IOException If an I/O error occurs during password change.
     */
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

    /**
     * Displays an alert dialog to the user with a given title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation dialog to the user before deleting the account. If confirmed,
     * the account deletion is processed. Otherwise, the action is canceled.
     */
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

    /**
     * Deletes the user's account and logs them out of the application if the deletion is successful.
     * After account deletion, the user is redirected to the login page.
     *
     * @throws IOException If an I/O error occurs during account deletion.
     */
    private void deleteAccount() throws IOException {
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            UserAccountDAO userAccountDAO = new UserAccountDAO();
            boolean isDeleted = userAccountDAO.deleteAccount(loggedInUser.getUsername());

            if (isDeleted) {
                showAlert("Account Deletion", "Your account has been successfully deleted.");
                // log out the user and redirect to the login page
                Session.getInstance().clearLoggedInUser();
                MainApplication.switchScene("/edu/qut/cab302/wehab/fxml/user_account/Login.fxml");
            } else {
                showAlert("Account Deletion", "Failed to delete your account. Please try again.");
            }
        }
    }

    /**
     * Adds an event handler to the given toggle button that fires the button's action when the Enter key is pressed.
     *
     * @param toggleButton The toggle button to add the event handler to.
     */
    private void addEnterKeyHandler(ToggleButton toggleButton) {
        toggleButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                toggleButton.fire(); // Simulates the action of clicking the button, enabling the user only needing to press enter once to change the focused setting
            }
        });
    }

}