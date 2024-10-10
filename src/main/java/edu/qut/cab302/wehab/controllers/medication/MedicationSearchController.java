package edu.qut.cab302.wehab.controllers.medication;

import edu.qut.cab302.wehab.main.MainApplication;
import edu.qut.cab302.wehab.controllers.dashboard.ButtonController;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.medication.Medication;
import edu.qut.cab302.wehab.models.medication.MedicationInfoPage;
import edu.qut.cab302.wehab.models.medication.MedicationSearchModel;
import edu.qut.cab302.wehab.models.medication.OpenFDAClient;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static edu.qut.cab302.wehab.models.medication.MedicationSearchModel.*;

/**
 * The MedicationSearchController class is responsible for linking the UI view with the
 * logic for handling the search functionality contained in the {@link MedicationSearchModel}.
 * It initialises UI components, passes user-entered search criteria to an {@link OpenFDAClient}
 * to search for medications, and displays search results in the view. This class also interacts
 * with the database to store medication-related data.
 */
public class MedicationSearchController {

    private static Stage medicationOverviewModal = new Stage();

    /**
     * The TextField control where the user enters the medication search query.
     */
    @FXML
    private TextField searchField;

    /**
     * The VBox container that holds the search results displayed on the screen.
     */
    @FXML
    private VBox resultsPane;

    /**
     * The ScrollPane containing the resultsPane.
     */
    @FXML
    private ScrollPane resultsScrollPane;

    /**
     * The label that displays the result message after performing the search.
     */
    private Label resultMessage;

    @FXML
    private Button dashboardButton;
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

    OpenFDAClient apiClient;

    /**
     * Initialises the controller class and its associated UI components. Sets up buttons, retrieves the
     * logged-in user's information, initialises the medicationvhjq                                                                                                                                                                                                                                 q tables in the database, and instantiates
     * the API client.
     */
    public void initialize() {

        ButtonController.initialiseButtons(dashboardButton, workoutButton, medicationButton, settingsButton, signOutButton);

        UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null)
        {
            String fullname = loggedInUser.getFirstName();
            loggedInUserLabel.setText(fullname);
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

        apiClient = new OpenFDAClient();

        resultsPane.setAlignment(Pos.TOP_LEFT); // Align items to the top left
        resultsPane.setSpacing(50);
        resultsPane.setPadding(new Insets(20, 20, 20, 20));

        // Set a preferred width for the resultsPane
        resultsPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        resultsPane.setMaxWidth(Double.MAX_VALUE); // Allow it to stretch

        resultsScrollPane.setFitToWidth(true); // Make the ScrollPane fit its content width
        resultsScrollPane.setContent(resultsPane); // Ensure the content is set correctly

        medicationOverviewModal.initModality(Modality.APPLICATION_MODAL);
        medicationOverviewModal.setResizable(false);
    }

    /**
     * Handles the search functionality when the user enters a query in the searchField. The method
     * clears any previous results from the resultsPane, performs a search for medications using the API,
     * and displays the results in the resultsPane.
     */
    @FXML
    protected void search() {

        if (!searchField.getText().isEmpty()) {

            resultsScrollPane.setVvalue(0.0);
            resultsPane.getChildren().clear();

            Medication[] results = apiClient.searchForMedications(searchField.getText());

            resultMessage = new Label(apiClient.getResultsMessageForView());
            resultsPane.getChildren().add(resultMessage);

            if (results != null) {
                for (Medication result : results) {
                    resultsPane.getChildren().add(createMedicationListing(result));
                }
            }
        }
    }

    /**
     * Creates an HBox element representing a single medication result. This includes the medication's
     * brand name and manufacturer, generic name, last updated date, active ingredients, and a description, along with
     * buttons to view more details or add the medication to the user's saved medications.
     *
     * @param medication The Medication object containing details to display.
     * @return An HBox containing the medication listing.
     */
    private HBox createMedicationListing(Medication medication) {

        HBox medicationListing = new HBox();
        medicationListing.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        medicationListing.setPrefSize(1000, 200);
        medicationListing.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(1000, 200);
        anchorPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_COMPUTED_SIZE);
        anchorPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #F5F5DC; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 10px;");

        Label lastUpdated = new Label("Last Updated: " + medication.getLastUpdated().toString());

        Label brandName = new Label(medication.getDisplayName());

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
        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                MedicationInfoPage medicationInfoPage = new MedicationInfoPage(medication.getID());
                Scene scene = medicationInfoPage.getMedicationInfoPage();

                medicationOverviewModal.setScene(scene);
                medicationOverviewModal.showAndWait();
            }
        });


        Button logDoseButton = new Button("Add to List");
        logDoseButton.setLayoutX(844);
        logDoseButton.setLayoutY(40);
        logDoseButton.setPrefSize(81, 25);
        logDoseButton.setStyle("-fx-background-color: #00FF7F; -fx-border-color: #000000;");

        logDoseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MedicationSearchModel.addMedicationToUserList(medication);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
                try {
                    MainApplication.switchScene("/edu/qut/cab302/wehab/fxml/medication/medication-overview.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

}
