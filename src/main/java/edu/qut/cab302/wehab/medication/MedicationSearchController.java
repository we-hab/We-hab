package edu.qut.cab302.wehab.medication;

import javafx.fxml.FXML;
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

import edu.qut.cab302.wehab.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * Handles medication search functionality within the application.
 */

public class MedicationSearchController {

    private Connection connection = DatabaseConnection.getInstance();

    private void createMedicationsTable() throws SQLException {

        Statement createMedicationsTable;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS medications (" +
                "medicationID VARCHAR(255) PRIMARY KEY" +
                ")";

        createMedicationsTable = connection.createStatement();

        createMedicationsTable.execute(createTableSQL);

    }

    private void createJunctionTable() throws SQLException {
        Statement createJunctionTable;

        String createJunctionTableSQL = "CREATE TABLE IF NOT EXISTS userMedications (" +
                "username VARCHAR(255) NOT NULL," +
                "medicationID VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (username, medicationID)" +
                ")";

        createJunctionTable = connection.createStatement();

        createJunctionTable.execute(createJunctionTableSQL);
    }

    private void deleteMedicationsTable() throws SQLException {

        Statement deleteMedicationsTable;
        String deleteMedicationsTableSQL = "DROP TABLE IF EXISTS medications";
        deleteMedicationsTable = connection.createStatement();
        deleteMedicationsTable.execute(deleteMedicationsTableSQL);
    }

    private void deleteJunctionTable() throws SQLException {
        Statement deleteJunctionTable;
        String deleteJunctionTableSQL = "DROP TABLE IF EXISTS userMedications";
        deleteJunctionTable = connection.createStatement();
        deleteJunctionTable.execute(deleteJunctionTableSQL);
    }

    private void createTables() throws SQLException {
        createMedicationsTable();
        createJunctionTable();
    }

    private void deleteTables() throws SQLException {
        deleteMedicationsTable();
        deleteJunctionTable();
    }

    private void resetTables() throws SQLException {
        deleteTables();
        createTables();
    }

    private void saveMedication(String medicationID) throws SQLException {
        PreparedStatement insertMedication;
        String insertMedicationSQL = "INSERT OR IGNORE INTO medications (medicationID) VALUES (?)";

        insertMedication = connection.prepareStatement(insertMedicationSQL);
        insertMedication.setString(1, medicationID);
    }

    @FXML
    private TextField searchField;

    @FXML
    private VBox resultsPane;

    @FXML
    private ScrollPane resultsScrollPane;

    private Label resultMessage;


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
        for(String ingredient : medication.getActiveIngredients()) {
            activeIngredients += ", " + ingredient;
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

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("PanadolBox.jpg")));
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

        Button logDoseButton = new Button("Log Dose");
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

        // Uncomment to reset tables
//        try {
//            resetTables();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        try {
            System.out.print("Accessing medications table...");
            createMedicationsTable();
            System.out.println("success!");
        } catch (SQLException e) {
            System.out.println("failed.\n" + e.getMessage());
        }

        resultsPane.setPadding(new Insets(20, 20, 20, 20));
    }

}
