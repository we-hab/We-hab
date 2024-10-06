package edu.qut.cab302.wehab.workout;

import edu.qut.cab302.wehab.ButtonController;
import edu.qut.cab302.wehab.Session;
import edu.qut.cab302.wehab.UserAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;

public class WorkoutController {

    // FXML UI elements
    @FXML
    private Button dashboardButton;
    @FXML
    private Button medicationButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Label loggedInUserLabel;
    @FXML
    private ComboBox<String> workoutTypeComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> durationSpinner;
    @FXML
    private ComboBox<Integer> effortComboBox;
    @FXML
    private Button confirmButton;
    @FXML
    private GridPane monthOverviewGrid;
    @FXML
//    private ScatterChart<String, Number> minutesPerDayChart;
    private BarChart<String, Number> minutesPerDayChart;

    private String username;
    /**
     * Used to store data retrieved from the DB in state to parse to the charts and grids.
     * */
    private ObservableList<Workout> workoutList = FXCollections.observableArrayList();


    /**
     * This method initialises state for the workouts page by retrieving the logged-in user and populating the
     * workout information.
     */
    @FXML
    public void initialize() {
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullname = loggedInUser.getFirstName();
            username = loggedInUser.getUsername();
            loggedInUserLabel.setText(fullname);
            loadWorkouts(username);
        } else
        {
            loggedInUserLabel.setText("Error: No logged in user.");
        }

        ButtonController.initialiseButtons(dashboardButton, null, medicationButton, settingsButton, signOutButton);

        // Initialise the workout types in the ComboBox
        workoutTypeComboBox.getItems().addAll("Walk", "Jog", "Run", "Yoga", "Cycling", "Other");

        // Set up the Spinner for workout duration (1 : 300 minutes, default 30)
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 30));

        // Initialise the effort levels in the ComboBox (1 : 5)
        effortComboBox.getItems().addAll(1, 2, 3, 4, 5);

        // Set up the Confirm button event handler
        confirmButton.setOnAction(event -> handleAddWorkout());
    }

    /**
     * This method is used to add a workout to the database based on the users inputs.
     * If the values are populated correctly this creates a new workout object, adds
     * it to the database and refreshes the UI to display new results.
     * */
    private void handleAddWorkout() {
        // Retrieve the workout details
        String workoutType = workoutTypeComboBox.getValue();
        LocalDate date = datePicker.getValue();
        Integer effort = effortComboBox.getValue();
        int duration = durationSpinner.getValue();

        // Process the input
        if (workoutType == null || date == null || effort == null) {
            System.out.println("Please fill in all the workout details.");
        } else {
            Workout workout = new Workout(workoutType, date, duration, effort);
            WorkoutReturnModel.addWorkout(workout, username);
            loadWorkouts(username);
        }
    }

    /**
     * This method loads workouts from the database on initialisation to update the UI.
     * */
    private void loadWorkouts(String username) {
        workoutList.clear();
        List<Workout> savedWorkouts = WorkoutReturnModel.getWorkouts(username);
        workoutList.addAll(savedWorkouts);
        updateMonthOverview();
        updateMinutesPerDayChart();
    }


    /**
     * This method updates the Month Overview Heatmap.
     * */
    private void updateMonthOverview() {
        monthOverviewGrid.setGridLinesVisible(false);
        monthOverviewGrid.setGridLinesVisible(true);
        TreeMap<LocalDate, Integer> monthlyMinutes = WorkoutReturnModel.getMonthlyMinutes(username);

        for (Map.Entry<LocalDate, Integer> entry : monthlyMinutes.entrySet()) {
            LocalDate date = entry.getKey();
            int dayOfMonth = date.getDayOfMonth();
            int totalMinutes = entry.getValue();

            // Label for the total minutes / day
            Label workoutLabel = new Label(totalMinutes + " mins");
            workoutLabel.setMaxWidth(Double.MAX_VALUE);
            workoutLabel.setMaxHeight(Double.MAX_VALUE);
            String labelColour;
            switch (totalMinutes / 30){
                case 0:
                    labelColour = "rgb(150, 255, 150)";
                    break;
                case 1:
                    labelColour = "rgb(255, 230, 200)";
                    break;
                case 2:
                    labelColour = "rgb(255, 180, 150)";
                    break;
                case 3:
                    labelColour = "rgb(255, 130, 100)";
                    break;
                case 4:
                    labelColour = "rgb(255, 80, 50)";
                    break;
                default:
                    labelColour = "rgb(200, 0, 200)";
                    break;
            }
            workoutLabel.setStyle("-fx-alignment: center; -fx-background-color: " + labelColour + "; -fx-border-width: 0.5px;");

            // Add the label to the grid.
            int row = (dayOfMonth - 1) / 7;
            int column = (dayOfMonth - 1) % 7;
            monthOverviewGrid.add(workoutLabel, column, row);
        }
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                Label placeholder = new Label();
                placeholder.setStyle("-fx-alignment: center; -fx-border-width: 0.5px;");
                monthOverviewGrid.add(placeholder, col, row);
            }
        }
    }

    /**
     * Bar chart to be updated to change query to retrieve the minutes against the activity type.
     * The logic should also be updated to include different colours for each excercise.
     * Comments and suspicious code should also be removed.
     */


    // Method to update the Minutes per Day Bar Chart
    private void updateMinutesPerDayChart() {
        minutesPerDayChart.getData().clear();

        // Prepare data series for the scatter chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Workout Minutes");

        // Aggregate workout duration per day
        Map<LocalDate, Integer> dailyMinutes = new HashMap<>();
        for (Workout workout : workoutList) {
            dailyMinutes.put(workout.getDate(), dailyMinutes.getOrDefault(workout.getDate(), 0) + workout.getDuration());
        }

        // Add data points to the series
        for (Map.Entry<LocalDate, Integer> entry : dailyMinutes.entrySet()) {
            String formattedDate = entry.getKey().toString();
            series.getData().add(new XYChart.Data<>(formattedDate, entry.getValue()));
        }

        minutesPerDayChart.getData().add(series);
    }
}
