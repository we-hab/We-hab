package edu.qut.cab302.wehab;

import edu.qut.cab302.wehab.medication.Medication;
import edu.qut.cab302.wehab.medication.MedicationSearchModel;
import edu.qut.cab302.wehab.medication.PrescribedMedicationDose;
import edu.qut.cab302.wehab.workout.Workout;
import edu.qut.cab302.wehab.workout.WorkoutReturnModel;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import edu.qut.cab302.wehab.MainApplication;


public class DashboardController implements Initializable {

    @FXML
    private Button workoutButton;

    @FXML
    private Button medicationButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutButton;

    @FXML
    private Label loggedInUserLabel;

    @FXML
    private Button moodRatingSubmission;

    @FXML
    private RadioButton moodButton1, moodButton2, moodButton3, moodButton4, moodButton5,
                        moodButton6, moodButton7, moodButton8, moodButton9, moodButton10;

    private ToggleGroup moodToggleGroup;

    @FXML
    LineChart<String, Number> moodChart;
    @FXML
    private CategoryAxis xHorizontal;
    @FXML
    private NumberAxis yVertical;

    @FXML
    private Button generatePdfBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        moodToggleGroup = new ToggleGroup();
        moodButton1.setToggleGroup(moodToggleGroup);
        moodButton2.setToggleGroup(moodToggleGroup);
        moodButton3.setToggleGroup(moodToggleGroup);
        moodButton4.setToggleGroup(moodToggleGroup);
        moodButton5.setToggleGroup(moodToggleGroup);
        moodButton6.setToggleGroup(moodToggleGroup);
        moodButton7.setToggleGroup(moodToggleGroup);
        moodButton8.setToggleGroup(moodToggleGroup);
        moodButton9.setToggleGroup(moodToggleGroup);
        moodButton10.setToggleGroup(moodToggleGroup);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String firstName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(firstName);
            loadMoodData(loggedInUser.getUsername());

            moodRatingSubmission.setOnAction(event ->
            {
                int selectedRating = getSelectedRating();
                if (selectedRating != -1)
                {
                    moodRating.insertMoodRating(selectedRating, loggedInUser.getUsername());
                    disableMoodButtons();
                    loadMoodData(loggedInUser.getUsername());
                }
            });

            if (moodRating.hasRatedToday(loggedInUser.getUsername()))
            {
                disableMoodButtons();
            }
        } else
        {
            loggedInUserLabel.setText("Error");
        }

        ButtonController.initialiseButtons(null, workoutButton, medicationButton, settingsButton, signOutButton);
        moodChart.setLegendVisible(false);

        generatePdfBtn.setOnAction(event ->
        {
            if (loggedInUser != null)
            {
                try {
                    List<moodRating> moodRatings = moodRating.getLast7Days(loggedInUser.getUsername());
                    List<Workout> workouts = WorkoutReturnModel.getWorkouts(loggedInUser.getUsername());
                    List<PrescribedMedicationDose> medications = MedicationSearchModel.getCurrentDayMedications();
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String formattedDate = today.format(formatter);
                    String filePath = "Report for " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + " " + formattedDate + ".pdf";
                    PDFReportGenerator.generateReport(moodRatings, workouts, medications, filePath);
                } catch (SQLException error) { System.err.println(error); }
            }
        });
    }

    private void loadMoodData(String username)
    {
        moodChart.getData().clear();


        List<moodRating> ratings = moodRating.getLast7Days(username);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Collections.reverse(ratings);
        for (moodRating rating : ratings)
        {
            series.getData().add(new XYChart.Data<>(rating.getRatingDate().toString(), rating.getMoodRating()));
        }

        moodChart.getData().add(series);
    }

    private int getSelectedRating()
    {
        RadioButton pressedButton = (RadioButton) moodToggleGroup.getSelectedToggle();
        if (pressedButton != null)
        {
            if (pressedButton == moodButton1) { return 1; }
            else if (pressedButton == moodButton2) { return 2; }
            else if (pressedButton == moodButton3) { return 3; }
            else if (pressedButton == moodButton4) { return 4; }
            else if (pressedButton == moodButton5) { return 5; }
            else if (pressedButton == moodButton6) { return 6; }
            else if (pressedButton == moodButton7) { return 7; }
            else if (pressedButton == moodButton8) { return 8; }
            else if (pressedButton == moodButton9) { return 9; }
            else if (pressedButton == moodButton10) { return 10; }
            //return Integer.parseInt(pressedButton.getText());
        }
        return -1;
    }

    private void disableMoodButtons()
    {
        moodButton1.setDisable(true);
        moodButton2.setDisable(true);
        moodButton3.setDisable(true);
        moodButton4.setDisable(true);
        moodButton5.setDisable(true);
        moodButton6.setDisable(true);
        moodButton7.setDisable(true);
        moodButton8.setDisable(true);
        moodButton9.setDisable(true);
        moodButton10.setDisable(true);
        moodRatingSubmission.setDisable(true);
    }

    private void enableMoodButtons()
    {
        moodButton1.setDisable(false);
        moodButton2.setDisable(false);
        moodButton3.setDisable(false);
        moodButton4.setDisable(false);
        moodButton5.setDisable(false);
        moodButton6.setDisable(false);
        moodButton7.setDisable(false);
        moodButton8.setDisable(false);
        moodButton9.setDisable(false);
        moodButton10.setDisable(false);
        moodRatingSubmission.setDisable(false);
    }

}

