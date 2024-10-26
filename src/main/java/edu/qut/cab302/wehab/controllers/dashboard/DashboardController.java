package edu.qut.cab302.wehab.controllers.dashboard;

import edu.qut.cab302.wehab.controllers.workout.WorkoutController;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.dao.MedicationDAO;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;
import edu.qut.cab302.wehab.models.mood_ratings.moodRating;
import edu.qut.cab302.wehab.models.pdf_report.PDFReportGenerator;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import edu.qut.cab302.wehab.models.workout.Workout;
import edu.qut.cab302.wehab.models.workout.WorkoutReturnModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DashboardController implements Initializable {

    @FXML
    private Button workoutButton, medicationButton, settingsButton, signOutButton, moodRatingSubmission;

    @FXML
    private Label loggedInUserLabel;
    @FXML
    private RadioButton moodButton1, moodButton2, moodButton3, moodButton4, moodButton5,
            moodButton6, moodButton7, moodButton8, moodButton9, moodButton10;

    private ToggleGroup moodToggleGroup; // Toggle group for mood rating buttons (So they can be greyed out all together)

    @FXML
    LineChart<String, Number> moodChart; // Line chart to show mood ratings
    @FXML
    private CategoryAxis xHorizontal; // Horizontal axis for the mood chart (left to right)
    @FXML
    private NumberAxis yVertical; // Vertical axis for the mood chart (up and down)

    @FXML
    private Button generatePdfBtn; // Button that generates the PDF report

    @FXML
    private TableView<MedicationReminder> remindersTable = new TableView<>();
    private ObservableList<MedicationReminder> medicationReminders = FXCollections.observableArrayList();

    private HashMap<String, String> userSavedMedications;
    private ArrayList<MedicationReminder> userSavedReminders;
    @FXML
    private BarChart<Number, String> minutesPerDayChart;
    @FXML
    private CategoryAxis workoutTypeAxis;
    private ObservableList<Workout> workoutList = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> targetComboBox;
    private WorkoutController workoutController;
    @FXML
    private Button completeReminderButton;
    @FXML
    private Button skipReminderButton;

    private boolean showConfirmationDialog(String prompt)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(prompt);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


    private void setDisableAllReminderButtons(boolean disable)
    {
        completeReminderButton.setDisable(disable);
        skipReminderButton.setDisable(disable);
    }

    /**
     * Initializes the controller. This method is called when the scene is loaded.
     * It sets up the UI elements, loads the mood data, and handles button actions.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Set up the toggle group for the mood buttons
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

        try {
            MedicationDAO.createMedicationRemindersTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set the default value for the ComboBox
        ObservableList<String> targetOptions = FXCollections.observableArrayList("30", "60", "90", "120", "120+"); // Populate ComboBox
        targetComboBox.setItems(targetOptions); // Set ComboBox options
        targetComboBox.setValue("30"); // Set ComboBox default

        // Retrieve the logged-in user for the session and load their mood data from the table moodRatings
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String firstName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(firstName); // Displays the user's first name in the top left of the UI.
            loadMoodData(loggedInUser.getUsername()); // Load the mood data for the user
            loadWorkouts(loggedInUser.getUsername()); // Load workouts for the user

            try {
                refreshRemindersWindow(); // Load the medication data for the user
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //** Handles the mood rating submission button action **\\
            moodRatingSubmission.setOnAction(event ->
            {
                int selectedRating = getSelectedRating(); // Get the selected mood rating that the user has selected
                if (selectedRating != -1) // If the user has selected a radio button, then continue
                {
                    // Insert the mood rating data into the database
                    moodRating.insertMoodRating(selectedRating, loggedInUser.getUsername());
                    disableMoodButtons(); // Disables the button after submission
                    loadMoodData(loggedInUser.getUsername()); // Reload the mood data in the UI to show the new rating
                }
            });

            // Disable the radio buttons if the user has already rated their mood today
            if (moodRating.hasRatedToday(loggedInUser.getUsername()))
            {
                disableMoodButtons();
            }
        } else
        {
            loggedInUserLabel.setText("Error"); // Error message if the user is not logged in.
        }

        //*** Initialize button actions for the navigation tabs ***\\
        ButtonController.initialiseButtons(null, workoutButton, medicationButton, settingsButton, signOutButton);
        moodChart.setLegendVisible(false); // Hide chart legend for mood ratings

        //*** Handle the PDF generation button action ***\\
        generatePdfBtn.setOnAction(event ->
        {
            if (loggedInUser != null)
            {
                try {
                    // Retrieve the data for the PDF Report
                    List<moodRating> moodRatings = moodRating.getLast7Days(loggedInUser.getUsername());
                    List<Workout> workouts = WorkoutReturnModel.getWorkouts(loggedInUser.getUsername());
                    List<MedicationReminder> medications = MedicationDAO.getReminderLog(LocalDate.now(), LocalDate.now());
                    TreeMap<LocalDate, Integer> monthlyMinutes = WorkoutReturnModel.getMonthlyMinutes(loggedInUser.getUsername());

                    // Format today's date for the file name in Australian date format
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String formattedDate = today.format(formatter);
                    String filePath = "Report for " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + " " + formattedDate + ".pdf";

                    // Generate the PDF report
                    PDFReportGenerator.generateReport(moodRatings, workouts, medications, monthlyMinutes ,filePath);
                } catch (SQLException error) { System.err.println(error); }
            }
        });

        remindersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        completeReminderButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                boolean proceed = showConfirmationDialog("Mark medication as taken?");

                if(proceed) {

                    MedicationReminder reminder = remindersTable.getSelectionModel().getSelectedItem();
                    String reminderIdToSearch = reminder.getReminderID();

                    try {
                        System.out.println("Marking medication: " + reminderIdToSearch + " as taken.");
                        MedicationDAO.markMedicationAsTaken(reminderIdToSearch);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        skipReminderButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                boolean proceed = showConfirmationDialog("Mark medication as missed?");

                if(proceed) {

                    MedicationReminder reminder = remindersTable.getSelectionModel().getSelectedItem();
                    String reminderIdToSearch = reminder.getReminderID();

                    try {
                        System.out.println("Marking medication: " + reminderIdToSearch + " as missed.");
                        MedicationDAO.markMedicationAsMissed(reminderIdToSearch);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        remindersTable.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && remindersTable.getSelectionModel().getSelectedItem() == null) {
                setDisableAllReminderButtons(true);
            } else {
                setDisableAllReminderButtons(false);
            }
        });

        remindersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDisableAllReminderButtons(false);
            } else {
                setDisableAllReminderButtons(true);
            }
        });

        remindersTable.getItems().addListener((ListChangeListener<MedicationReminder>) change -> {
            if (remindersTable.getItems().isEmpty()) {
                setDisableAllReminderButtons(true);
            }
        });

        targetComboBox.setOnAction(event -> {
            updateMinutesPerDayChart(); // If the ComboBox changes, update the chart.
        });

    }

    /**
     * Loads the mood data for the given username and updates the mood chart in the UI.
     * @param username The username of the logged-in user
     */
    private void loadMoodData(String username)
    {
        moodChart.getData().clear(); // Clear any existing chart data to start fresh

        // Retrieve the last 7 days of mood ratings
        List<moodRating> ratings = moodRating.getLast7Days(username);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Collections.reverse(ratings); // Reverse the order to show the latest date to the from right to left. (Latest date far right)

        // Add dots into the chart for each mood rating
        for (moodRating rating : ratings)
        {
            series.getData().add(new XYChart.Data<>(rating.getRatingDate().toString(), rating.getMoodRating()));
        }
        moodChart.getData().add(series); // Update the chart with the new data
    }

    /**
     * Retrieve the selected mood rating from the radio buttons.
     * @return The selected mood rating (1-10) or -1 if nothing is selected (for error handling).
     */
    private int getSelectedRating()
    {
        RadioButton pressedButton = (RadioButton) moodToggleGroup.getSelectedToggle();
        if (pressedButton != null)
        {
            // Return the rating based on the selected button
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
        }
        return -1; // Return -1 if nothing is selected.
    }

    /**
     * Disables the mood rating buttons to prevent multiple selections.
     */
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

    /**
     * Enables the mood rating buttons so the user can submit a rating.
     */
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

    /**
     * This method loads workouts from the database on initialisation to update the UI.
     * */
    private void loadWorkouts(String username) {
        workoutList.clear();
        List<Workout> savedWorkouts = WorkoutReturnModel.getWorkouts(username);
        workoutList.addAll(savedWorkouts);

        // Checking async retrieval
        if (workoutList.isEmpty()) {
            System.out.println("No workouts found for user.");
        } else {
            updateMinutesPerDayChart();
        }
    }

    private void refreshRemindersWindow() throws SQLException {
        remindersTable.getItems().clear();
        userSavedReminders = MedicationDAO.getAllReminders();
        System.out.println("Daily Reminders: " + userSavedReminders.size());
        medicationReminders = FXCollections.observableArrayList(userSavedReminders);
        remindersTable.setItems(medicationReminders);
    }

    public MedicationReminder getSelectedReminder() {
        return remindersTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Method to update the Minutes per Day BarChart
     * Bar chart to be updated to change query to retrieve the minutes against the activity type.
     */
    private void updateMinutesPerDayChart() {
        // Set up
        minutesPerDayChart.getData().clear();
        ObservableList<String> workoutTypes = FXCollections.observableArrayList("Walk", "Jog", "Run", "Yoga", "Cycling", "Other");
        workoutTypeAxis.setCategories(workoutTypes);

        String selectedTarget = targetComboBox.getValue(); // Retrieve ComboBox target value
        int targetValue;

        if(selectedTarget.equals("120+")){
            targetValue = 120;
        } else {
            targetValue = Integer.parseInt(selectedTarget);
        }

        // Add minutes by workout type
        Map<String, Integer> workoutMinutesByType = new HashMap<>();
        for (Workout workout : workoutList) {
            String workoutType = workout.getWorkoutType();
            workoutMinutesByType.put(workoutType, workoutMinutesByType.getOrDefault(workoutType, 0) + workout.getDuration());
        }

        // Collect data - total minutes per workout type
        XYChart.Series<Number, String> workoutTypeData = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : workoutMinutesByType.entrySet()) {
            String workoutType = entry.getKey();
            Integer totalMinutes = entry.getValue();

            if (totalMinutes > 0) {
                // Create a datapoint for each node and test against the users target.
                XYChart.Data<Number, String> dataPoint = new XYChart.Data<>(totalMinutes, workoutType);
                dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {

                    if (newNode != null) {
                        if (totalMinutes >= targetValue) {
                            newNode.setStyle("-fx-bar-fill: green;");
                        } else {
                            newNode.setStyle("-fx-bar-fill: orange;");
                        }
                    }
                });
                workoutTypeData.getData().add(dataPoint);
            }
        }
        minutesPerDayChart.getData().add(workoutTypeData);
    }

}