package edu.qut.cab302.wehab;

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
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WorkoutController {

    // UI elements

    @FXML
    private Button dashboardButton;

    @FXML
    private Button medicationButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button signOutButton;

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

    // List to store workout data
    private ObservableList<Workout> workoutList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        ButtonController.initialiseButtons(dashboardButton, null, medicationButton, settingsButton, signOutButton);

        // Initialize the workout types in the ComboBox
        workoutTypeComboBox.getItems().addAll("Walk", "Jog", "Run", "Yoga", "Cycling", "Other");

        // Set up the Spinner for workout duration (1 to 300 minutes)
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 30)); // Default value 30

        // Initialize the effort levels in the ComboBox (1-5)
        effortComboBox.getItems().addAll(1, 2, 3, 4, 5);

        // Set up the Confirm button event handler
        confirmButton.setOnAction(event -> handleAddWorkout());
    }

    // Method to add a workout and update visualizations
    private void handleAddWorkout() {
        // Get the selected workout type
        String workoutType = workoutTypeComboBox.getValue();

        // Get the selected date
        LocalDate date = datePicker.getValue();

        // Get the effort level from the ComboBox (use Integer wrapper class)
        Integer effort = effortComboBox.getValue();

        // Get the duration from the Spinner (default to 0 if it's null)
        int duration = durationSpinner.getValue();

        // Check for null values to prevent NullPointerException
        if (workoutType == null || date == null || effort == null) {
            System.out.println("Please fill in all the workout details.");
            return;
        }

        // Create a new Workout object
        Workout workout = new Workout(workoutType, date, duration, effort);

        // Add the workout to the list
        workoutList.add(workout);

        // Update visual elements
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
