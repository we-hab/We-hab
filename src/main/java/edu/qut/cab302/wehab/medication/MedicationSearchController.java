package edu.qut.cab302.wehab.medication;

import edu.qut.cab302.wehab.MainApplication;
import edu.qut.cab302.wehab.Session;
import edu.qut.cab302.wehab.UserAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static edu.qut.cab302.wehab.medication.MedicationSearchModel.*;

/**
 * Handles medication search functionality within the application.
 */

public class MedicationSearchController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private VBox resultsPane;

    @FXML
    private ScrollPane resultsScrollPane;

    private Label resultMessage;

    @FXML
    private Button dashboardButton;
    @FXML
    private Button workoutButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Label loggedInUserLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Medication button action
        dashboardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("dashboard.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load the dashboard.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        // Settings button action
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("settings-page.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load settings page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        // Dashboard button action
        workoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainApplication.switchScene("Visual-Progress-Tracking.fxml");
                } catch (IOException e) {
                    System.out.println("Failed to load the workout page.\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname);
        } else
        {
            loggedInUserLabel.setText("Error");
        }
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        // Step 1: Clear session
        Session.getInstance().clearLoggedInUser();

        // Step 2: Navigate to the login screen
        try {
            MainApplication.switchScene("Login.fxml");  // Adjust the path based on your structure
        } catch (IOException e) {
            System.out.println("Failed to load login page.\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void search() {

        if (!searchField.getText().isEmpty()) {

            resultsScrollPane.setVvalue(0.0);
            resultsPane.getChildren().clear();

            Medication[] results = OpenFDAClient.searchForMedications(searchField.getText());

            resultMessage = new Label(OpenFDAClient.getResultsMessage());
            resultsPane.getChildren().add(resultMessage);

            if (results != null) {
                for (Medication result : results) {
                    resultsPane.getChildren().add(createMedicationListing(result));
                }
            }
        }
    }


    private HBox createMedicationListing(Medication medication) {

        HBox medicationListing = new HBox();
        medicationListing.setAlignment(javafx.geometry.Pos.CENTER);
        medicationListing.setPrefSize(1000, 200);
        medicationListing.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(1000, 200);
        anchorPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_COMPUTED_SIZE);
        anchorPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #F5F5DC; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 10px;");

        Label lastUpdated = new Label("Last Updated: " + medication.getLastUpdated().toString());

        Label brandName = new Label((medication.hasBrandName()) ? medication.getBrandName() : medication.getGenericName());

        Label genericName = new Label("Generic Name: " + ((medication.hasBrandName()) ? medication.getGenericName() : null));

        String activeIngredients = "Active Ingredients: ";

        if (medication.getActiveIngredients() != null) {
            for(String ingredient : medication.getActiveIngredients()) {
                activeIngredients += ", " + ingredient;
            }
        } else {
            activeIngredients += "Unspecified";
        }
        Label activeIngredientsLabel = new Label(activeIngredients);

        Label administrationRoute = new Label("Administration Route: " + medication.getAdministrationRoute());

        String establishedPharmClasses;
        if(medication.getMedicationTypes() == null) {
            establishedPharmClasses = "Medication Type: Unspecified";
        } else if(medication.getMedicationTypes().length == 1) {
            establishedPharmClasses = "Medication Type: " + medication.getMedicationTypes()[0];
        } else {
            establishedPharmClasses = "Medication Types: " + medication.getMedicationTypes()[0];
            for (int i = 1; i < medication.getMedicationTypes().length; i++) {
                    establishedPharmClasses += ", " + medication.getMedicationTypes()[i];
            }
        }
        Label medicationTypes = new Label(establishedPharmClasses);

        Label descriptionLabel = new Label("Description:");
        TextArea description = new TextArea(medication.getDescription());
        description.setLayoutX(14);
        description.setLayoutY(40);
        description.setPrefSize(614, 65);
        description.setEditable(false);
        description.setWrapText(true);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/edu/qut/cab302/wehab/images/PanadolBox.jpg")));
        icon.setFitWidth(158);
        icon.setFitHeight(85);
        icon.setLayoutX(658);
        icon.setLayoutY(27);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);

        Button viewButton = new Button("View");
        viewButton.setLayoutX(844);
        viewButton.setLayoutY(76);
        viewButton.setPrefSize(81, 25);
        viewButton.setStyle("-fx-background-color: #00FF7F; -fx-border-color: #000000;");

        Button logDoseButton = new Button("Add to List");
        logDoseButton.setLayoutX(844);
        logDoseButton.setLayoutY(40);
        logDoseButton.setPrefSize(81, 25);
        logDoseButton.setStyle("-fx-background-color: #00FF7F; -fx-border-color: #000000;");
        logDoseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    saveMedication(medication.getID());
                    System.out.println("Medication " + medication.getBrandName() + " (ID: " + medication.getID() + ") added to database.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // Will require a PreparedStatement to insert the username/medicationID combination into the junction table.
                // Add this once logic for username retrieval has been created.

            }
        });

        VBox medicationInfoBox = new VBox();
        medicationInfoBox.setPadding(new Insets((10)));
        medicationInfoBox.setSpacing(5);
        medicationInfoBox.setLayoutX(20);
        medicationInfoBox.setLayoutY(10);

        VBox buttonsBox = new VBox();
        buttonsBox.setPadding(new Insets((10)));
        buttonsBox.setSpacing(5);
        buttonsBox.setLayoutX(20);
        buttonsBox.setLayoutY(10);

        // Creating containers for controls
        medicationInfoBox.getChildren().addAll(brandName, genericName, lastUpdated, medicationTypes, descriptionLabel, description);
        buttonsBox.getChildren().addAll(viewButton, logDoseButton);
        buttonsBox.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(medicationInfoBox, icon, buttonsBox);
        hbox.setSpacing(40);
        hbox.setAlignment(Pos.CENTER_LEFT);

        anchorPane.getChildren().add(hbox);

        medicationListing.getChildren().add(anchorPane);

        return medicationListing;
    }

    public void initialize() {

//         Uncomment to reset tables
//        try {
//            resetTables();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        try {
            System.out.print("Accessing medications table...");
            createMedicationsTable();
            createJunctionTable();
            System.out.println("success!");
        } catch (SQLException e) {
            System.out.println("failed.\n" + e.getMessage());
        }

        resultsPane.setPadding(new Insets(20, 20, 20, 20));
    }

}
