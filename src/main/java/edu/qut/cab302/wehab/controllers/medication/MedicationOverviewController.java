package edu.qut.cab302.wehab.controllers.medication;

import edu.qut.cab302.wehab.main.MainApplication;
import edu.qut.cab302.wehab.controllers.dashboard.ButtonController;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;
import edu.qut.cab302.wehab.models.dao.MedicationDAO;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static edu.qut.cab302.wehab.models.dao.MedicationDAO.createMedicationTables;
import static edu.qut.cab302.wehab.util.EncryptionUtility.encrypt;

/**
 * Controller for the medication overview page in the application.
 * It manages the medication reminders, interactions with the database,
 * and handles UI events related to saved medications and reminders.
 */
public class MedicationOverviewController {

    @FXML
    private Label selectedMedicationLabel;

    @FXML
    private Button createReminderButton;

    @FXML
    private Button viewSummaryButton;

    @FXML
    private Button addMedicationButton;

    @FXML
    private Button dashboardButton;
    @FXML
    private Button workoutButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Label loggedInUserLabel;

    @FXML
    private ListView savedMedicationsListView;

    @FXML
    private TextField dosageTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox unitComboBox;

    @FXML
    private Spinner<LocalTime> timeSpinner;

    @FXML
    private TableView<MedicationReminder> remindersTable = new TableView<>();
    private ObservableList<MedicationReminder> medicationReminders = FXCollections.observableArrayList();

    private HashMap<String, String> userSavedMedications;
    private ArrayList<MedicationReminder> userSavedReminders;

    @FXML
    private Button completeReminderButton;
    @FXML
    private Button skipReminderButton;
    @FXML
    private Button editReminderButton;
    @FXML
    private Button deleteReminderButton;

    private SpinnerValueFactory<LocalTime> timeValueFactory;

    /**
     * Gets the SpinnerValueFactory for the time spinner.
     *
     * @return SpinnerValueFactory controlling time values in the time spinner.
     */
    public SpinnerValueFactory<LocalTime> getTimeValueFactory() { return timeValueFactory; }

    private MedicationOverviewController() {}
    private static MedicationOverviewController instance;

    /**
     * Singleton pattern to ensure a single instance of MedicationOverviewController.
     *
     * @return instance of MedicationOverviewController.
     */
    public static MedicationOverviewController getInstance() {
        if (instance == null) {
            instance = new MedicationOverviewController();
        }
        return instance;
    }

    /**
     * Resets the UI entry fields for creating reminders.
     * Clears text fields and resets values to default.
     */
    public void resetReminderFields() {
        dosageTextField.clear();
        datePicker.setValue(null);
        unitComboBox.setValue(null);
        timeSpinner.getValueFactory().setValue(LocalTime.of(12, 0));
    }

    /**
     * Reloads the user's saved medications and refreshes the ListView in which they are displayed.
     */
    private void refreshSavedMedicationsWindow() {
        savedMedicationsListView.getItems().clear();
        for(String medicationName : userSavedMedications.keySet()) {
            savedMedicationsListView.getItems().add(medicationName);
        }
    }

    /**
     * Refreshes the reminders window by fetching all medication reminders from the database
     * and repopulating the TableView.
     *
     * @throws SQLException if there is an error accessing the database.
     */
    private void refreshRemindersWindow() throws SQLException {
        remindersTable.getItems().clear();
        userSavedReminders = MedicationDAO.getAllReminders();
        System.out.println("Daily Reminders: " + userSavedReminders.size());
        medicationReminders = FXCollections.observableArrayList(userSavedReminders);
        remindersTable.setItems(medicationReminders);

        // Customises the TableView rows to indicate overdue reminders by colouring them red.
        remindersTable.setRowFactory(row -> new TableRow<MedicationReminder>() {

            @Override
            protected void updateItem(MedicationReminder reminder, boolean empty) {
                super.updateItem(reminder, empty);

                if(empty || reminder == null) {
                    setStyle("");
                } else {
                    LocalDateTime reminderDue = LocalDateTime.of(reminder.getDosageDate(), reminder.getDosageTime());

                    // Apply red background for overdue reminders
                    if(reminderDue.isBefore(LocalDateTime.now())) {
                        setStyle("-fx-background-color: rgba(255,0,0,0.68); ");
                    }
                }
            }
        });
    }

    /**
     * Displays a confirmation dialog with the given prompt and returns whether
     * the user confirmed the action.
     *
     * @param prompt Message to display in the confirmation dialog.
     * @return true if user confirmed the action, false otherwise.
     */
    private boolean showConfirmationDialog(String prompt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(prompt);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Retrieves the currently selected reminder from the TableView.
     *
     * @return the selected MedicationReminder object.
     */
    public MedicationReminder getSelectedReminder() {
        return remindersTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Disables or enables all reminder-related buttons (done, missed, edit, delete).
     *
     * @param disable true to disable buttons, false to enable them.
     */
    private void setDisableAllReminderButtons(boolean disable) {
        completeReminderButton.setDisable(disable);
        skipReminderButton.setDisable(disable);
        editReminderButton.setDisable(disable);
        deleteReminderButton.setDisable(disable);
    }


    /**
     * Initialises the controller, sets up event handlers, populates UI components,
     * and handles initial loading of medication reminders.
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @FXML
    public void initialize() throws SQLException {

        // Sets the column resize policy for the reminders table to constrained resize policy
        remindersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Handles marking a medication reminder as done
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

                    // Refresh the table to reflect changes
                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        // Handles marking a medication reminder as missed
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

                    // Refresh the table to reflect changes
                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        // Handles deleting a medication reminder
        deleteReminderButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                boolean proceed = showConfirmationDialog("Delete reminder?");

                if(proceed) {

                    MedicationReminder reminder = remindersTable.getSelectionModel().getSelectedItem();
                    String reminderIdToSearch;
                    try {
                        reminderIdToSearch = reminder.getReminderID();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        System.out.println("Deleting reminder " + reminderIdToSearch);
                        MedicationDAO.deleteMedicationReminder(reminderIdToSearch);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    // Refresh the table to reflect changes
                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        // Loads a new FXML scene in a modal window to edit a reminder
        editReminderButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Reminder");
                stage.setResizable(false);

                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/edu/qut/cab302/wehab/fxml/medication/edit-reminder.fxml"));
                Parent root;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();

                // Refresh the table to reflect changes
                try {
                    refreshRemindersWindow();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        // Initialise button controls for dashboard navigation
        ButtonController.initialiseButtons(dashboardButton, workoutButton, null, settingsButton, signOutButton);

        // Retrieve and display the logged-in user details
        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullName);
        } else
        {
            loggedInUserLabel.setText("Error");
        }

        // Attempt to access the medication table in the database
        try {
            System.out.print("Accessing medications table...");
            createMedicationTables();
            System.out.println("success!");
        } catch (SQLException e) {
            System.out.println("failed.\n" + e.getMessage());
        }

        // Refresh the reminders window
        refreshRemindersWindow();

        // Set action for adding medication, which switches to another scene
        addMedicationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("/edu/qut/cab302/wehab/fxml/medication/Medication-Search.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load medication search page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        viewSummaryButton.setMinWidth(112);

        // Set action for viewing medication summary
        viewSummaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String medicationIdToSearch = userSavedMedications.get(selectedMedicationLabel.getText());

                System.out.println("Loading view summary page with medication ID: " + medicationIdToSearch);
                MedicationInfoPageController medicationInfoPageController = new MedicationInfoPageController(medicationIdToSearch);
                Scene scene = medicationInfoPageController.getMedicationInfoPage();

                Stage medicationOverviewModal = new Stage();
                medicationOverviewModal.initModality(Modality.APPLICATION_MODAL);
                medicationOverviewModal.setResizable(false);
                medicationOverviewModal.setScene(scene);
                medicationOverviewModal.showAndWait();
            }
        });

        Stage notificationPopupModal = new Stage();
        notificationPopupModal.initModality(Modality.APPLICATION_MODAL);
        notificationPopupModal.setResizable(false);

        // Set action for creating a new reminder
        createReminderButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Label statusMessage;

                String medicationId = null;
                String amount = null;
                String unit = null;
                String date = null;
                String time = null;

                // Check if all fields are filled before creating the reminder
                try {
                    medicationId = userSavedMedications.get(selectedMedicationLabel.getText());
                    amount = dosageTextField.getText();
                    unit = unitComboBox.getValue().toString();
                    date = datePicker.getValue().toString();
                    time = timeSpinner.getValue().toString();
                } catch (NullPointerException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setContentText("All fields must be filled.");
                    alert.showAndWait();
                }

                // If all fields are valid, save the reminder to the database
                if (!medicationId.isEmpty() && !amount.isEmpty() && !unit.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                    try {
                        MedicationDAO.addReminder(medicationId, amount, unit, date, time);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    // Refresh the reminders table
                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    resetReminderFields();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setContentText("Reminder created!");
                    alert.showAndWait();
                }
            }
        });


        // Add input validation for the dosageTextField (accepts only numeric values up to two decimal places)
        dosageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                dosageTextField.setText(oldValue);
            }
        });

        // Load saved medications for the logged-in user
        try {
            userSavedMedications = MedicationDAO.getUserSavedMedicationNames();
            System.out.println("Fetched Medications: " + userSavedMedications.keySet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Refresh the medication results window
        refreshSavedMedicationsWindow();

        // Configure saved medications ListView
        savedMedicationsListView.setCellFactory(lv -> new ListCell<String>() {
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Button deleteButton = new Button("x");
            private final Region spacer = new Region();

            {
                label.setWrapText(true);
                hbox.setSpacing(10);
                label.setMaxWidth(savedMedicationsListView.getWidth() * 0.85);
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(label, spacer, deleteButton);

                deleteButton.setOnAction(event -> {
                    String medicationToRemove = getItem();
                    try {

                        // Removes medication from the database and updates the UI
                        if (medicationToRemove != null) {
                            System.out.print("Removing medication: " + medicationToRemove + "...");
                            MedicationDAO.deleteUserSavedMedication(encrypt(userSavedMedications.get(medicationToRemove)));
                            userSavedMedications.remove(medicationToRemove);
                            System.out.println("done.");

                            refreshSavedMedicationsWindow();
                            savedMedicationsListView.getSelectionModel().select(0);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    }
                });
            }


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(hbox);
                }
            }
        });

        // Set the initial selected medication label
        if (!savedMedicationsListView.getItems().isEmpty()) {
            savedMedicationsListView.getSelectionModel().select(0);
            selectedMedicationLabel.setText(savedMedicationsListView.getSelectionModel().getSelectedItem().toString());
        } else {
            selectedMedicationLabel.setText("No Saved Medications");
        }

        // Update selected medication label when a new medication is selected from the ListView
        savedMedicationsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedMedicationLabel.setText(newValue.toString());
            } else {
                selectedMedicationLabel.setText("No Saved Medications");
            }
        });

        // Disable reminder buttons when no reminder is selected
        remindersTable.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && remindersTable.getSelectionModel().getSelectedItem() == null) {
                setDisableAllReminderButtons(true);
            } else {
                setDisableAllReminderButtons(false);
            }
        });

        // Update button states when a new reminder is selected
        remindersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDisableAllReminderButtons(false);
            } else {
                setDisableAllReminderButtons(true);
            }
        });

        // Disable all reminder buttons if no reminders are present
        remindersTable.getItems().addListener((ListChangeListener<MedicationReminder>) change -> {
            if (remindersTable.getItems().isEmpty()) {
                setDisableAllReminderButtons(true);
            }
        });

        // Set up the time spinner with 15-minute step increments
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime initialValue = LocalTime.of(12, 0);
        LocalTime minTime = LocalTime.of(0, 0);
        LocalTime maxTime = LocalTime.of(23, 59);

        // SpinnerValueFactory for managing time input
        timeValueFactory = new SpinnerValueFactory<>() {
            {
                setValue(initialValue);
            }

            @Override
            public void decrement(int steps) {
                LocalTime currentTime = getValue();
                setValue(currentTime.minusMinutes(steps * 15));
            }

            @Override
            public void increment(int steps) {
                LocalTime currentTime = getValue();
                setValue(currentTime.plusMinutes(steps * 15));
            }
        };

        timeSpinner.setValueFactory(timeValueFactory);

        // Allow the time spinner to accept custom time input
        timeSpinner.setEditable(true);
        timeSpinner.getEditor().setTextFormatter(new TextFormatter<>(c -> {
            try {
                LocalTime.parse(c.getControlNewText(), timeFormatter);
                return c;
            } catch (Exception e) {
                return null;
            }
        }));
    }
}