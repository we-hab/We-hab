package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

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
}

