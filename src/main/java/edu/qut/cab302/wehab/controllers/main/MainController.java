package edu.qut.cab302.wehab.controllers.main;

import edu.qut.cab302.wehab.main.MainApplication;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import edu.qut.cab302.wehab.models.dao.UserAccountDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Main controller for managing application views and user interactions.
 */

public class MainController {

    // Login and Register Variables
    @FXML
    private Label ErrorText;
    @FXML
    private Label registerErrorText;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordFieldConfirm;
    @FXML
    private Button registerBackButton;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * Validates the provided email against the predefined regex pattern.
     *
     * @param email The email address to validate.
     * @return true if the email is valid; false otherwise.
     */
    private boolean isAValidEmail(String email)
    {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Initializes the controller and sets up key event handling for the password field.
     * Logs the user in when the ENTER key is pressed.
     */
    @FXML
    public void initialize()
    {


        passwordField.setOnKeyPressed(event ->
        {
            switch (event.getCode())
            {
                case ENTER:
                    onLoginClick();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Navigates to the registration screen when the user clicks the "Create account" button.
     */
    @FXML
    protected void onGoToRegisterScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/qut/cab302/wehab/fxml/user_account/create-account.fxml"));
            Parent root = loader.load();

            // Get the controller of the newly loaded FXML
            MainController controller = loader.getController();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));

            // Request focus on the back button after the scene is set
            if (controller.registerBackButton != null) {
                controller.registerBackButton.requestFocus();
            }

            stage.show();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * Navigates back to the login screen.
     */
    @FXML
    protected void backToLoginScreen()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/qut/cab302/wehab/fxml/user_account/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.show();
        } catch( Exception error) { error.printStackTrace(); }
    }

    /**
     * Handles user registration when the "Register" button is clicked.
     * Validates input fields and creates a new user account if validation passes.
     */
    @FXML
    protected void onRegisterClick()
    {
        String enteredFirstName = firstNameField.getText();
        String enteredLastName = lastNameField.getText();
        String enteredUsername = usernameField.getText();
        String enteredEmail = emailField.getText();
        String enteredPassword = passwordField.getText();
        String enteredPasswordConfirm = passwordFieldConfirm.getText();

        UserAccountDAO userAccountDAO = new UserAccountDAO();
        List<String> usernames = userAccountDAO.getAllusernames();
        List<String> emails = userAccountDAO.getAllemails();

        final int maxCharLimit = 255;

        registerErrorText.getStyleClass().remove("error-label");

        if (enteredFirstName.isEmpty())
        {
            registerErrorText.setText("Enter a first name.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredFirstName.length() > maxCharLimit)
        {
            registerErrorText.setText("First name cannot exceed " + maxCharLimit + " characters.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredLastName.isEmpty())
        {
            registerErrorText.setText("Enter a last name.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredLastName.length() > maxCharLimit)
        {
            registerErrorText.setText(" cannot exceed " + maxCharLimit + " characters.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredUsername.isEmpty())
        {
            registerErrorText.setText("Enter a username.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredUsername.length() > maxCharLimit)
        {
            registerErrorText.setText(" cannot exceed " + maxCharLimit + " characters.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredEmail.isEmpty())
        {
            registerErrorText.setText("Enter an email.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else if (enteredPassword.isEmpty() && enteredPasswordConfirm.isEmpty())
        {
            registerErrorText.setText("Enter a password.");
            registerErrorText.getStyleClass().add("error-label");  // Apply error styling
            return;
        }
        else
        {
            if (usernames.contains(enteredUsername))
            {
                registerErrorText.setText("Username taken, try another.");
                registerErrorText.getStyleClass().add("error-label");  // Apply error styling
                return;
            }
            else if (emails.contains(enteredEmail)) // Same email
            {
                registerErrorText.setText("Email taken, try another.");
                registerErrorText.getStyleClass().add("error-label");  // Apply error styling
                return;
            }
            else if (!isAValidEmail(enteredEmail))
            {
                registerErrorText.setText("Enter a valid email address.");
                registerErrorText.getStyleClass().add("error-label");  // Apply error styling
                return;
            }
            else if (!enteredPassword.equals(enteredPasswordConfirm))
            {
                registerErrorText.setText("Passwords do not match.");
                registerErrorText.getStyleClass().add("error-label");  // Apply error styling
                return;
            }
            else
            {
                UserAccount newAccount = new UserAccount(enteredUsername, enteredFirstName, enteredLastName, enteredEmail, enteredPassword);
                userAccountDAO.registerAccount(newAccount);
                Session.getInstance().setLoggedInUser(newAccount);

                // Clear error message and style if login succeeds
                ErrorText.setText("");
                registerErrorText.getStyleClass().remove("error-label");

                try
                {
                    // Use the switchScene method to apply the active stylesheet
                    MainApplication.switchScene("/edu/qut/cab302/wehab/fxml/dashboard/dashboard.fxml");
                } catch( Exception error) { error.printStackTrace(); }
            }
        }
    }

    /**
     * Handles user login when the "Login" button is clicked.
     * Validates input fields and authenticates the user.
     */
    @FXML
    protected void onLoginClick()
    {
        // Yoinks the inputted text from the user
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        UserAccountDAO userAccountDAO = new UserAccountDAO();

        // Clear any previous error styling before validating
        ErrorText.getStyleClass().remove("error-label");

        // Validate input fields
        if (enteredUsername.isEmpty())
        {
            ErrorText.setText("Please enter a username.");
        }
        else if (enteredPassword.isEmpty())
        {
            ErrorText.setText("Please enter a password");
        }
        else
        {
            // Checks to see whether the inputted username and password match an account in the system
            boolean loginSuccessful = userAccountDAO.LoginToAccount(enteredUsername, enteredPassword);


            if (loginSuccessful)
            {
                UserAccount loggedInUser = userAccountDAO.getByUsername(enteredUsername);
                Session.getInstance().setLoggedInUser(loggedInUser);

                // Clear error message and style if login succeeds
                ErrorText.setText("");
                ErrorText.getStyleClass().remove("error-label");

                try
                {
                    // Use the switchScene method to apply the active stylesheet
                    MainApplication.switchScene("/edu/qut/cab302/wehab/fxml/dashboard/dashboard.fxml");
                } catch( Exception error) { error.printStackTrace(); }
            }
            else {
                ErrorText.setText("Username or password not found. Try again.");
                ErrorText.getStyleClass().add("error-label");  // Apply error styling
                passwordField.clear();
            }
        }
        // Apply error styling if any validation fails
        ErrorText.getStyleClass().add("error-label");
    }
}

