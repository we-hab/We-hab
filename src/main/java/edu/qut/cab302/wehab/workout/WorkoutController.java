package edu.qut.cab302.wehab.workout;

import edu.qut.cab302.wehab.ButtonController;
import edu.qut.cab302.wehab.Session;
import edu.qut.cab302.wehab.UserAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.HashMap;
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
    private ScatterChart<String, Number> minutesPerDayChart;

    private String username;
    /**
     * Used to store data retrieved from the DB in state to parse to the charts and grids.
     * */
    private ObservableList<Workout> workoutList = FXCollections.observableArrayList();


    /**
     * This method initilaises the state for the workouts page by retrieving the logged-in user and populating the
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

        // Initialize the workout types in the ComboBox
        workoutTypeComboBox.getItems().addAll("Walk", "Jog", "Run", "Yoga", "Cycling", "Other");

        // Set up the Spinner for workout duration (1 to 300 minutes)
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 30)); // Default value 30

        // Initialise the effort levels in the ComboBox (1-5)
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


    // Method to update the Month Overview grid
    private void updateMonthOverview() {
        // Clear the existing content
        monthOverviewGrid.getChildren().clear();

        // Display workout data for the current month
        for (Workout workout : workoutList) {
            if (workout.getDate().getMonth() == LocalDate.now().getMonth()) {
                // Create a label to represent the workout and add it to the grid
                Label workoutLabel = new Label(workout.getWorkoutType() + " " + workout.getDuration() + " mins");

                // Set the width to avoid cut off and enable text wrapping
                workoutLabel.setMaxWidth(100); // Adjust width as needed
                workoutLabel.setWrapText(true); // Enable text wrapping

                // Add a tooltip for detailed view
                Tooltip tooltip = new Tooltip(workout.getWorkoutType() + ": " + workout.getDuration() + " mins, Effort: " + workout.getEffort());
                Tooltip.install(workoutLabel, tooltip);

                int row = workout.getDate().getDayOfMonth() / 7; // Example row calculation
                int column = workout.getDate().getDayOfMonth() % 7; // Example column calculation
                monthOverviewGrid.add(workoutLabel, column, row);
            }
        }
    }

    // Method to update the Minutes per Day scatter chart
    private void updateMinutesPerDayChart() {
        // Clear previous data
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
            String formattedDate = entry.getKey().toString(); // You can format the date if needed
            series.getData().add(new XYChart.Data<>(formattedDate, entry.getValue()));
        }

        // Rotate X-Axis labels
        minutesPerDayChart.getXAxis().setTickLabelRotation(45); // Rotate labels for better readability

        // Add series to the scatter chart
        minutesPerDayChart.getData().add(series);
    }
}
