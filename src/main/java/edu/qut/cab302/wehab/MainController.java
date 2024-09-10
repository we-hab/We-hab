package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextArea usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to We-Hab! Or is it WeHab? Or We Hab?");
    }
    @FXML
    protected void testRegisterAccount(ActionEvent testRegAccount)
    {
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        userAccountDAO.testRegAccount();
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

