package edu.qut.cab302.wehab.controllers.medication;

import edu.qut.cab302.wehab.main.MainApplication;
import edu.qut.cab302.wehab.controllers.dashboard.ButtonController;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.medication.MedicationInfoPage;
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

public class MedicationOverviewController {

    @FXML
    private Label selectedMedicationLabel;

    private String selectedReminder;

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
    private ListView savedMedications;

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
    private Button reminderDoneButton;
    @FXML
    private Button reminderMissedButton;
    @FXML
    private Button editReminderButton;
    @FXML
    private Button deleteReminderButton;

    private SpinnerValueFactory<LocalTime> timeValueFactory;
    public SpinnerValueFactory<LocalTime> getTimeValueFactory() { return timeValueFactory; }

    private MedicationOverviewController() {}
    private static MedicationOverviewController instance;
    public static MedicationOverviewController getInstance() {
        if (instance == null) {
            instance = new MedicationOverviewController();
        }
        return instance;
    }


    public void resetReminderFields() {
        dosageTextField.clear();
        datePicker.setValue(null);
        unitComboBox.setValue(null);
        timeSpinner.getValueFactory().setValue(LocalTime.of(12, 0));
    }

    private void refreshResultsWindow() {
        savedMedications.getItems().clear();
        for(String medicationName : userSavedMedications.keySet()) {
            savedMedications.getItems().add(medicationName);
        }
    }

    private void refreshRemindersWindow() throws SQLException {
        remindersTable.getItems().clear();
        userSavedReminders = MedicationDAO.getAllReminders();
        System.out.println("Daily Reminders: " + userSavedReminders.size());
        medicationReminders = FXCollections.observableArrayList(userSavedReminders);
        remindersTable.setItems(medicationReminders);

        remindersTable.setRowFactory(row -> new TableRow<MedicationReminder>() {

            @Override
            protected void updateItem(MedicationReminder reminder, boolean empty) {
                super.updateItem(reminder, empty);

                if(empty || reminder == null) {
                    setStyle("");
                } else {
                    LocalDateTime reminderDue = LocalDateTime.of(reminder.getDosageDate(), reminder.getDosageTime());

                    if(reminderDue.isBefore(LocalDateTime.now())) {
                        setStyle("-fx-background-color: rgba(255,0,0,0.68); ");
                    }
                }
            }
        });
    }

    private boolean showConfirmationDialog(String prompt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(prompt);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public MedicationReminder getSelectedReminder() {
        return remindersTable.getSelectionModel().getSelectedItem();
    }

    private void setDisableAllReminderButtons(boolean disable) {
        reminderDoneButton.setDisable(disable);
        reminderMissedButton.setDisable(disable);
        editReminderButton.setDisable(disable);
        deleteReminderButton.setDisable(disable);
    }


    @FXML
    public void initialize() throws SQLException {

        remindersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        reminderDoneButton.setOnAction(new EventHandler<ActionEvent>() {
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

        reminderMissedButton.setOnAction(new EventHandler<ActionEvent>() {
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
                    try {
                        refreshRemindersWindow();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

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

                try {
                    refreshRemindersWindow();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        ButtonController.initialiseButtons(dashboardButton, workoutButton, null, settingsButton, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullName = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullName);
        } else
        {
            loggedInUserLabel.setText("Error");
        }

        try {
            System.out.print("Accessing medications table...");
            createMedicationTables();
            System.out.println("success!");
        } catch (SQLException e) {
            System.out.println("failed.\n" + e.getMessage());
        }

        refreshRemindersWindow();

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

        viewSummaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String medicationIdToSearch = userSavedMedications.get(selectedMedicationLabel.getText());

                System.out.println("Loading view summary page with medication ID: " + medicationIdToSearch);
                MedicationInfoPage medicationInfoPage = new MedicationInfoPage(medicationIdToSearch);
                Scene scene = medicationInfoPage.getMedicationInfoPage();

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
        createReminderButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Label statusMessage;

                String medicationId = null;
                String amount = null;
                String unit = null;
                String date = null;
                String time = null;

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

                if (!medicationId.isEmpty() && !amount.isEmpty() && !unit.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                    try {
                        MedicationDAO.addReminder(medicationId, amount, unit, date, time);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

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


        dosageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                dosageTextField.setText(oldValue);
            }
        });

        try {
            userSavedMedications = MedicationDAO.getUserSavedMedicationNames();
            System.out.println("Fetched Medications: " + userSavedMedications.keySet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        refreshResultsWindow();

        savedMedications.setCellFactory(lv -> new ListCell<String>() {
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Button deleteButton = new Button("x");
            private final Region spacer = new Region();

            {
                hbox.setSpacing(10);
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(label, spacer, deleteButton);
                deleteButton.setOnAction(event -> {
                    String medicationToRemove = getItem();
                    try {

                        if (medicationToRemove != null) {
                            System.out.print("Removing medication: " + medicationToRemove + "...");
                            MedicationDAO.deleteUserSavedMedication(encrypt(userSavedMedications.get(medicationToRemove)));
                            userSavedMedications.remove(medicationToRemove);
                            System.out.println("done.");

                            refreshResultsWindow();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidAlgorithmParameterException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (BadPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeyException e) {
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


        if (!savedMedications.getItems().isEmpty()) {
            savedMedications.getSelectionModel().select(0);
            selectedMedicationLabel.setText(savedMedications.getSelectionModel().getSelectedItem().toString());
        } else {
            selectedMedicationLabel.setText("No Saved Medications");
        }

        savedMedications.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedMedicationLabel.setText(newValue.toString());
            } else {
                selectedMedicationLabel.setText("No Saved Medications");
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



        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime initialValue = LocalTime.of(12, 0);
        LocalTime minTime = LocalTime.of(0, 0);
        LocalTime maxTime = LocalTime.of(23, 59);

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