package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.sound.midi.SysexMessage;
import java.util.List;

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

    // If the user clicks the "Create account" button on the Login screen, change to the register screen.
    @FXML
    protected void onGoToRegisterScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-account.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @FXML
    protected void backToLoginScreen()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.show();
        } catch( Exception error) { error.printStackTrace(); }
    }

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

        if (enteredFirstName.isEmpty())
        {
            registerErrorText.setText("Enter a first name.");
            return;
        }
        else if (enteredLastName.isEmpty())
        {
            registerErrorText.setText("Enter a last name.");
            return;
        }
        else if (enteredUsername.isEmpty())
        {
            registerErrorText.setText("Enter a username.");
            return;
        }
        else if (enteredEmail.isEmpty())
        {
            registerErrorText.setText("Enter an email.");
            return;
        }
        else if (enteredPassword.isEmpty() && enteredPasswordConfirm.isEmpty())
        {
            registerErrorText.setText("Enter a password.");
            return;
        }
        else
        {
            if (usernames.contains(enteredUsername))
            {
                registerErrorText.setText("Username taken, try another.");
                System.out.println("Username taken, try another.");
            }
            else if (!enteredPassword.equals(enteredPasswordConfirm))
            {
                registerErrorText.setText("Passwords do not match.");
                System.out.println("Passwords do not match.");
            }
            else
            {
                UserAccount newAccount = new UserAccount(enteredUsername, enteredFirstName, enteredLastName, enteredEmail, enteredPassword);
                userAccountDAO.registerAccount(newAccount);

                try
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));

                    stage.show();
                } catch( Exception error) { error.printStackTrace(); }
            }
        }
    }

    // Login screen //
    // When a user clicks the 'login' button, it'll run this code:
    @FXML
    protected void onLoginClick()
    {
        // Yoinks the inputted text from the user
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        UserAccountDAO userAccountDAO = new UserAccountDAO();

        if (enteredUsername.isEmpty())
        {
            ErrorText.setText("Please enter a username.");
            return;
        }
        else if (enteredPassword.isEmpty())
        {
            ErrorText.setText("Please enter a password");
            return;
        }
        else
        {
            // Checks to see whether the inputted username and password match an account in the system
            boolean loginSuccessful = userAccountDAO.LoginToAccount(enteredUsername, enteredPassword);

            if (loginSuccessful)
            {
                try
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));

                    stage.show();
                } catch( Exception error) { error.printStackTrace(); }
            }
            else {
                ErrorText.setText("Username or password not found. Try again.");
                passwordField.clear();
            }
        }
    }
}

