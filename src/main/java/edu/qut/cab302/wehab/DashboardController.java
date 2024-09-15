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
    private Button medicationButton;

    public void initialize() {
        medicationButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               try {
                   MainApplication.switchScene("medication/Medication-Search.fxml");
               } catch (IOException e) {
                   System.out.println("Failed to load medication page.\n" + e.getMessage());
                   throw new RuntimeException(e);
               }
           }
        });
    }
  
      @FXML
    private Label loggedInUserLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname + "!");
        } else
        {
            loggedInUserLabel.setText("Error");
        }
    }
  
}