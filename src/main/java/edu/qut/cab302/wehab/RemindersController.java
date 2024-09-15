package edu.qut.cab302.wehab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
/**
 * Controller for managing reminders, such as viewing, editing, and deleting them.
 */
public class RemindersController {

    @FXML
    private Button sundayButton;
    @FXML
    private Button mondayButton;
    @FXML
    private Button tuesdayButton;
    @FXML
    private Button wednesdayButton;
    @FXML
    private Button thursdayButton;
    @FXML
    private Button fridayButton;
    @FXML
    private Button saturdayButton;

    // This method can be used to set the days for a specific reminder
    public void setReminderDays(boolean[] daysActive) {
        // daysActive[0] represents Sunday, daysActive[1] represents Monday, and so on...

        setDayStyle(sundayButton, daysActive[0]);
        setDayStyle(mondayButton, daysActive[1]);
        setDayStyle(tuesdayButton, daysActive[2]);
        setDayStyle(wednesdayButton, daysActive[3]);
        setDayStyle(thursdayButton, daysActive[4]);
        setDayStyle(fridayButton, daysActive[5]);
        setDayStyle(saturdayButton, daysActive[6]);
    }

    // Helper function to set the background color of a day button
    private void setDayStyle(Button dayButton, boolean isActive) {
        if (isActive) {
            dayButton.setStyle("-fx-background-color: #808080; -fx-background-radius: 50%; -fx-text-fill: white;");
        } else {
            dayButton.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 50%; -fx-text-fill: black;");
        }
    }
}
