package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.sound.midi.SysexMessage;
import java.util.List;

public class MainController {
    @FXML
    private Label welcomeText;


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
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to We-Hab! Or is it WeHab? Or We Hab?");
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

        if (!enteredPassword.equals(enteredPasswordConfirm))
        {
            System.out.println("Passwords do not match.");
            return;
        }

        UserAccountDAO userAccountDAO = new UserAccountDAO();
        List<String> usernames = userAccountDAO.getAllusernames();

        if (usernames.contains(enteredUsername))
        {
            System.out.println("Username taken, try another.");
        }
        else
        {
            UserAccount newAccount = new UserAccount(enteredUsername, enteredFirstName, enteredLastName, enteredEmail, enteredPassword);
            userAccountDAO.registerAccount(newAccount);
        }


    }

    @FXML
    protected void onLoginClick()
    {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        UserAccountDAO userAccountDAO = new UserAccountDAO();
        boolean loginSuccessful = userAccountDAO.LoginToAccount(enteredUsername, enteredPassword);

        if (loginSuccessful)
        {
            // Need to create a text box under password to show the suer it wasn't successful
            //welcomeText.setText("Login Successful!");
            System.out.println("Success login");
        }
        else {
            //welcomeText.setText("Login failed! Please try again.");
            System.out.println("Unsuccessful login attempt");
        }
    }
}

