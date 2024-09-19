package edu.qut.cab302.wehab;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import edu.qut.cab302.wehab.MainApplication;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.sql.*;



public class DashboardController implements Initializable
{
    @FXML
    private Label loggedInUserLabel;

    @FXML
    private LineChart<String, Number> moodChart;
    @FXML
    private ToggleGroup moodToggleGroup;

    @FXML
    private RadioButton dailyMoodRatingButton1, dailyMoodRatingButton2, dailyMoodRatingButton3, dailyMoodRatingButton4, dailyMoodRatingButton5;
    @FXML
    private RadioButton dailyMoodRatingButton6, dailyMoodRatingButton7, dailyMoodRatingButton8, dailyMoodRatingButton9, dailyMoodRatingButton10;

    @FXML
    private Button workoutButton;

    @FXML
    private Button medicationButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        ButtonController.initialiseButtons(null, workoutButton, medicationButton, settingsButton, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String firstName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(firstName + "!");

            if (hasRatedToday(loggedInUser.getUsername()))
            {
                disableMoodRadioButtons();
            }
        }
        else
        {
            loggedInUserLabel.setText("Error");
        }
    }

    public boolean hasRatedToday(String username)
    {
        try (Connection connection = DatabaseConnection.getInstance())
        {
            String sqlStatement = "SELECT COUNT(*) FROM moodRatings WHERE username = ? AND ratingDate = ?";
            PreparedStatement statement = connection.prepareStatement(sqlStatement);
            statement.setString(1, username);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet result = statement.executeQuery();
            if (result.next())
            {
                return result.getInt(1) > 0;
            }
        } catch (SQLException error) { System.err.println(error); }
        return false;
    }

    private void disableMoodRadioButtons()
    {
        dailyMoodRatingButton1.setDisable(true);
        dailyMoodRatingButton2.setDisable(true);
        dailyMoodRatingButton3.setDisable(true);
        dailyMoodRatingButton4.setDisable(true);
        dailyMoodRatingButton5.setDisable(true);
        dailyMoodRatingButton6.setDisable(true);
        dailyMoodRatingButton7.setDisable(true);
        dailyMoodRatingButton8.setDisable(true);
        dailyMoodRatingButton9.setDisable(true);
        dailyMoodRatingButton10.setDisable(true);
    }

    private void setupMoodButtonActions(String username)
    {
        RadioButton[] moodButtons =
                {
                        dailyMoodRatingButton1, dailyMoodRatingButton2, dailyMoodRatingButton3, dailyMoodRatingButton4, dailyMoodRatingButton5,
                        dailyMoodRatingButton6, dailyMoodRatingButton7, dailyMoodRatingButton8, dailyMoodRatingButton9, dailyMoodRatingButton10
                };

        for (RadioButton button : moodButtons)
        {
            button.setOnAction(actionEvent -> {
                int moodRating = Integer.parseInt(button.getText());
                handleMoodRatingSubmission(username, moodRating);
            });
        }
    }

    private void handleMoodRatingSubmission(String username, int moodRating)
    {
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        userAccountDAO.insertMoodRating(username, LocalDate.now(), moodRating);

        disableMoodRadioButtons();

        updateMoodChart(username);
    }

    @FXML
    protected void onDailyMoodRatingClick()
    {
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();
        String username = loggedInUser.getUsername();

        UserAccountDAO userAccountDAO = new UserAccountDAO();

        if (hasRatedToday(username)) { disableMoodRadioButtons(); return; }

        RadioButton selectedMoodRadioButton = (RadioButton) moodToggleGroup.getSelectedToggle();
        int moodRating = Integer.parseInt(selectedMoodRadioButton.getText());
        userAccountDAO.insertMoodRating(username, LocalDate.now(), moodRating);

        disableMoodRadioButtons();

        updateMoodChart(username);
    }

    private void updateMoodChart(String username)
    {
        UserAccountDAO userAccountDAO = new UserAccountDAO();
        List<MoodRating> moodRatings = userAccountDAO.getLast7DaysOfRatings(username);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (MoodRating rating : moodRatings)
        {
            series.getData().add(new XYChart.Data<>(rating.getDate().toString(), rating.getRating()));
        }

        moodChart.getData().clear();
        moodChart.getData().add(series);
    }

}