/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 13/6/2023
 Client.java
 Main client
 Assumptions:
 1. User input for the variables needed are inputted correctly (no checks performed).
 2. Only one new customer will be added and if associate customer, 
 will not be added to paying customer.
 */

import java.io.File;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MagazineServiceGUI {

    private GridPane root;
    private TextArea infoPanelBox;
    private Stage primaryStage;
    private Button viewButton, createButton, editButton, submitButton, addSupplementButton, addCustomerButton,
            editSupplementButton, editCustomerButton, deleteSupplementButton, deleteCustomerButton,
            addMagazineButton, loadMagazineButton, saveMagazineButton;
    private ListView<Supplement> supplementsView, supplementChoice, oldSupplements;
    private ListView<Customer> customersView, customerChoice;
    private ComboBox<String> typeOfCustomerComboBox, cardType, magazineChoice;
    private ComboBox<PayingCustomer> payingCustomerChoice;
    private TextField magazineNameTextField, supplementNameTextField, supplementCostTextField, customersNameTextField,
            emailAddressTextField, streetNumberTextField, streetNameTextField, suburbTextField, postCodeTextField,
            accountNumberTextField, typeOfCustomerTextField;
    private Label payingCustomerLabel, accountNumberLabel, currentMagazine;
    private File selectedFile;

    public MagazineServiceGUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void homePage() {
        root = new GridPane();

        Label header = new Label("Magazine Service");

        // Create buttons for different modes
        viewButton = new Button("View");
        createButton = new Button("Create");
        editButton = new Button("Edit");

        viewButton.setFocusTraversable(false);
        createButton.setFocusTraversable(false);
        editButton.setFocusTraversable(false);

        // Set width and height of button
        viewButton.setPrefWidth(100);
        viewButton.setPrefHeight(30);
        createButton.setPrefWidth(100);
        createButton.setPrefHeight(30);
        editButton.setPrefWidth(100);
        editButton.setPrefHeight(30);

        // Create the layout
        root.setMinSize(700, 700);
        root.setPadding(new Insets(10));
        root.setVgap(20);
        root.setHgap(10);
        root.setAlignment(Pos.TOP_CENTER);

        // Styling nodes
        header.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-family: Georgia;");

        // Set column constraints to ensure equal distance
        ColumnConstraints column1 = new ColumnConstraints(200);
        column1.setHalignment(HPos.CENTER);
        ColumnConstraints column2 = new ColumnConstraints(200);
        column2.setHalignment(HPos.CENTER);
        ColumnConstraints column3 = new ColumnConstraints(200);
        column3.setHalignment(HPos.CENTER);
        root.getColumnConstraints().addAll(column1, column2, column3);

        // Arranging nodes in grid
        root.add(header, 0, 0, 4, 1);
        root.add(viewButton, 0, 1, 1, 1);
        root.add(createButton, 1, 1, 1, 1);
        root.add(editButton, 2, 1, 2, 1);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Magazine Service");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void viewMode() {
        homePage();

        // Create ListView for supplements
        supplementsView = new ListView<>();
        supplementsView.setStyle("-fx-pref-height: 200px");

        // Create ListView for customers
        customersView = new ListView<>();
        customersView.setStyle("-fx-pref-height: 200px");

        Label infoPanel = new Label("Information Panel:");
        Label supplementPanel = new Label("List of Supplements: ");
        Label customerPanel = new Label("List of Customers: ");
        currentMagazine = new Label("");

        // Styling nodes
        infoPanel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        supplementPanel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        customerPanel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        currentMagazine.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        // Create box for information panel
        infoPanelBox = new TextArea();
        infoPanelBox.setEditable(false);

        // Add nodes to the gridPane
        root.add(supplementPanel, 0, 3);
        root.add(supplementsView, 0, 4);
        root.add(customerPanel, 0, 5);
        root.add(customersView, 0, 6);
        root.add(infoPanel, 1, 3);
        root.add(infoPanelBox, 1, 4, 2, 3);
        root.add(currentMagazine, 0, 7, 3, 1);
    }

    public void createMode() {
        homePage();
        createButton.setDisable(true);

        Label createHeader = new Label("Choose an Option:");
        createHeader.setStyle("-fx-font-weight: bold;");
        addMagazineButton = new Button("Add Magazine");
        addMagazineButton.setFocusTraversable(false);
        loadMagazineButton = new Button("Load Magazine");
        loadMagazineButton.setFocusTraversable(false);
        saveMagazineButton = new Button("Save Magazine");
        saveMagazineButton.setFocusTraversable(false);

        // Set width and height of button
        addMagazineButton.setMinSize(200, 30);
        loadMagazineButton.setMinSize(200, 30);
        saveMagazineButton.setMinSize(200, 30);

        root.add(createHeader, 1, 5);
        root.add(addMagazineButton, 1, 6);
        root.add(loadMagazineButton, 1, 8);
        root.add(saveMagazineButton, 1, 10);
    }

    public void addMagazineMode() {
        homePage();

        Label magazineNameLabel = new Label("Magazine name:");
        magazineNameTextField = new TextField();

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(magazineNameLabel, 0, 5);
        root.add(magazineNameTextField, 1, 5);
        root.add(submitButton, 1, 6);
    }

    public void loadMagazineMode() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Load Magazine File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Files (*.ser)", "*.ser");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = new Stage();
        selectedFile = fileChooser.showOpenDialog(stage);
    }

    public void saveMagazineMode() {
        homePage();

        Label saveMagazineHeader = new Label("Select a magazine to save:");
        saveMagazineHeader.setStyle("-fx-font-weight: bold;");

        magazineChoice = new ComboBox<>();
        magazineChoice.setPromptText("Options");
        magazineChoice.setStyle("-fx-pref-width: 200px");

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(saveMagazineHeader, 1, 4);
        root.add(magazineChoice, 1, 5);
        root.add(submitButton, 1, 6);
    }

    public void editMode() {
        homePage();

        Label editHeader = new Label("Choose an Option:");
        editHeader.setStyle("-fx-font-weight: bold;");
        addSupplementButton = new Button("Add Supplement");
        addSupplementButton.setFocusTraversable(false);
        addCustomerButton = new Button("Add Customer");
        addCustomerButton.setFocusTraversable(false);
        editSupplementButton = new Button("Edit Supplement");
        editSupplementButton.setFocusTraversable(false);
        editCustomerButton = new Button("Edit Customer");
        editCustomerButton.setFocusTraversable(false);
        deleteSupplementButton = new Button("Delete Supplement");
        deleteSupplementButton.setFocusTraversable(false);
        deleteCustomerButton = new Button("Delete Customer");
        deleteCustomerButton.setFocusTraversable(false);
        currentMagazine = new Label("");
        currentMagazine.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        // Set width and height of button
        addSupplementButton.setMinSize(200, 30);
        addCustomerButton.setMinSize(200, 30);
        editSupplementButton.setMinSize(200, 30);
        editCustomerButton.setMinSize(200, 30);
        deleteSupplementButton.setMinSize(200, 30);
        deleteCustomerButton.setMinSize(200, 30);

        root.add(editHeader, 1, 5);
        root.add(addSupplementButton, 1, 6);
        root.add(addCustomerButton, 1, 8);
        root.add(editSupplementButton, 1, 10);
        root.add(editCustomerButton, 1, 12);
        root.add(deleteSupplementButton, 1, 14);
        root.add(deleteCustomerButton, 1, 16);
        root.add(currentMagazine, 0, 20, 3, 1);
    }

    public void addSupplementMode() {
        homePage();
        Label addSupplementHeader = new Label("Add Supplement:");
        addSupplementHeader.setStyle("-fx-font-weight: bold;");
        Label supplementNameLabel = new Label("Supplement name:");
        supplementNameTextField = new TextField();
        Label supplementCostLabel = new Label("Supplement cost:");
        supplementCostTextField = new TextField();

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        // Arranging nodes in grid
        root.add(addSupplementHeader, 1, 3);
        root.add(supplementNameLabel, 0, 5);
        root.add(supplementNameTextField, 1, 5);
        root.add(supplementCostLabel, 0, 6);
        root.add(supplementCostTextField, 1, 6);
        root.add(submitButton, 1, 7);
    }

    public void addCustomerMode() {
        homePage();

        // Customer header
        Label addCustomerHeader = new Label("Add Customer:");
        addCustomerHeader.setStyle("-fx-font-weight: bold;");

        // Customer type field
        Label customerTypeLabel = new Label("Select type of customer:");
        typeOfCustomerComboBox = new ComboBox<>();
        typeOfCustomerComboBox.setPromptText("Options");
        typeOfCustomerComboBox.setStyle("-fx-pref-width: 200px");

        // Customer name field
        Label customerNameLabel = new Label("Customer name:");
        customersNameTextField = new TextField();

        // Email field
        Label emailAddressLabel = new Label("Email Address:");
        emailAddressTextField = new TextField();

        // Street number field
        Label streetNumberLabel = new Label("Street Number:");
        streetNumberTextField = new TextField();

        // Street name field
        Label streetNameLabel = new Label("Street Name:");
        streetNameTextField = new TextField();

        // Suburb field
        Label suburbLabel = new Label("Suburb:");
        suburbTextField = new TextField();

        // Postcode field
        Label postcodeLabel = new Label("Postcode:");
        postCodeTextField = new TextField();

        // Supplement field
        Label supplementsLabel = new Label("Select Supplement(s):");
        supplementChoice = new ListView<>();
        supplementChoice.setStyle("-fx-pref-height: 75px");

        // Allow multiple selection for supplements
        supplementChoice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Paying customer field
        payingCustomerLabel = new Label("Select Paying Customer:");
        payingCustomerLabel.setVisible(false);
        payingCustomerChoice = new ComboBox<>();
        payingCustomerChoice.setStyle("-fx-pref-width: 200px");
        payingCustomerChoice.setVisible(false);

        // Account number field
        accountNumberLabel = new Label("Account Number:");
        accountNumberLabel.setVisible(false);
        accountNumberTextField = new TextField();
        accountNumberTextField.setVisible(false);

        // Card type field
        cardType = new ComboBox<>();
        cardType.getItems().addAll("Credit Card", "Debit Card");
        cardType.setPromptText("Select Card Type");
        cardType.setStyle("-fx-pref-width: 200px");
        cardType.setVisible(false);

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        // Arranging nodes in grid
        root.add(addCustomerHeader, 1, 3);

        // Customer type field
        root.add(customerTypeLabel, 0, 4);
        root.add(typeOfCustomerComboBox, 1, 4);

        // Customer name field
        root.add(customerNameLabel, 0, 5);
        root.add(customersNameTextField, 1, 5);

        // Email field
        root.add(emailAddressLabel, 0, 6);
        root.add(emailAddressTextField, 1, 6);

        // Street number field
        root.add(streetNumberLabel, 0, 7);
        root.add(streetNumberTextField, 1, 7);

        // Street name field
        root.add(streetNameLabel, 0, 8);
        root.add(streetNameTextField, 1, 8);

        // Suburb field
        root.add(suburbLabel, 0, 9);
        root.add(suburbTextField, 1, 9);

        // Postcode field
        root.add(postcodeLabel, 0, 10);
        root.add(postCodeTextField, 1, 10);

        // Supplement field
        root.add(supplementsLabel, 0, 11);
        root.add(supplementChoice, 1, 11);

        // Paying customer field
        root.add(payingCustomerLabel, 0, 12);
        root.add(payingCustomerChoice, 1, 12);

        // Account number field
        root.add(accountNumberLabel, 0, 12);
        root.add(accountNumberTextField, 1, 12);

        // Card type field
        root.add(cardType, 2, 12);

        // Submit button
        root.add(submitButton, 1, 13);
    }

    public void editSupplementMode() {
        homePage();
        // Selecting supplement
        Label supplementsLabel = new Label("Select supplement to edit:");
        supplementsLabel.setStyle("-fx-font-weight: bold;");
        supplementChoice = new ListView<>();
        supplementChoice.setStyle("-fx-pref-height: 450px");

        Label supplementNameLabel = new Label("Supplement name:");
        supplementNameTextField = new TextField();
        Label supplementCostLabel = new Label("Supplement cost:");
        supplementCostTextField = new TextField();
        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(supplementsLabel, 0, 3);
        root.add(supplementChoice, 0, 4, 1, 12);

        root.add(supplementNameLabel, 1, 12);
        root.add(supplementNameTextField, 2, 12);
        root.add(supplementCostLabel, 1, 13);
        root.add(supplementCostTextField, 2, 13);
        root.add(submitButton, 2, 14);
    }

    public void editCustomerMode() {
        /*
         ASSUMPTIONS: CANNOT EDIT TYPE OF CUSTOMER
         */
        homePage();
        // Selecting customer
        Label customersLabel = new Label("Select customer to edit:");
        customersLabel.setStyle("-fx-font-weight: bold;");
        customerChoice = new ListView<>();
        // Customer type field
        Label customerTypeLabel = new Label("Type of customer:");
        typeOfCustomerTextField = new TextField();
        typeOfCustomerTextField.setDisable(true);
        // Customer name field
        Label customerNameLabel = new Label("Customer name:");
        customersNameTextField = new TextField();
        // Email field
        Label emailAddressLabel = new Label("Email Address:");
        emailAddressTextField = new TextField();
        // Street number field
        Label streetNumberLabel = new Label("Street Number:");
        streetNumberTextField = new TextField();
        // Street name field
        Label streetNameLabel = new Label("Street Name:");
        streetNameTextField = new TextField();
        // Suburb field
        Label suburbLabel = new Label("Suburb:");
        suburbTextField = new TextField();
        // Postcode field
        Label postcodeLabel = new Label("Postcode:");
        postCodeTextField = new TextField();
        // Existing supplements
        Label oldSupplementsHeader = new Label("Your supplement(s):");
        oldSupplements = new ListView<>();
        // Disable selection and only allow scrolling
        oldSupplements.addEventFilter(MouseEvent.MOUSE_PRESSED, MouseEvent::consume);
        // Supplement field
        Label supplementsLabel = new Label("Select Supplement(s):");
        supplementChoice = new ListView<>();
        // Allow multiple selection for supplements
        supplementChoice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Paying customer field
        payingCustomerLabel = new Label("Select Paying Customer:");
        payingCustomerLabel.setVisible(false);
        payingCustomerChoice = new ComboBox<>();
        payingCustomerChoice.setPromptText("Select One");
        payingCustomerChoice.setStyle("-fx-pref-width: 200px");
        payingCustomerChoice.setVisible(false);
        // Account number field
        accountNumberLabel = new Label("Account Number:");
        accountNumberLabel.setVisible(false);
        accountNumberTextField = new TextField();
        accountNumberTextField.setVisible(false);
        // Card type field
        cardType = new ComboBox<>();
        cardType.setPromptText("Select Card Type");
        cardType.setStyle("-fx-pref-width: 200px");
        cardType.setVisible(false);
        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(customersLabel, 0, 3);
        root.add(customerChoice, 0, 4, 1, 10);
        // Customer type field
        root.add(customerTypeLabel, 1, 3);
        root.add(typeOfCustomerTextField, 2, 3);
        // Customer name field
        root.add(customerNameLabel, 1, 4);
        root.add(customersNameTextField, 2, 4);
        // Email field
        root.add(emailAddressLabel, 1, 5);
        root.add(emailAddressTextField, 2, 5);
        // Street number field
        root.add(streetNumberLabel, 1, 6);
        root.add(streetNumberTextField, 2, 6);
        // Street name field
        root.add(streetNameLabel, 1, 7);
        root.add(streetNameTextField, 2, 7);
        // Suburb field
        root.add(suburbLabel, 1, 8);
        root.add(suburbTextField, 2, 8);
        // Postcode field
        root.add(postcodeLabel, 1, 9);
        root.add(postCodeTextField, 2, 9);
        // Supplement header
        root.add(oldSupplementsHeader, 1, 10);
        root.add(oldSupplements, 1, 11);
        // Supplement field
        root.add(supplementsLabel, 2, 10);
        root.add(supplementChoice, 2, 11);
        // Paying customer field
        root.add(payingCustomerLabel, 1, 12);
        root.add(payingCustomerChoice, 2, 12);
        // Card type field
        root.add(cardType, 2, 12);
        // Account number field
        root.add(accountNumberLabel, 1, 13);
        root.add(accountNumberTextField, 2, 13);
        // Submit button
        root.add(submitButton, 2, 14);
    }

    public void deleteSupplementMode() {
        /*
         CANNOT DELETE A SUPPLEMENT THAT A CUSTOMER IS CURRENTLY SUBSCRIBED TO
         */
        homePage();
        Label supplementsLabel = new Label("Select supplement to delete:");
        supplementsLabel.setStyle("-fx-font-weight: bold;");
        supplementChoice = new ListView<>();
        supplementChoice.setStyle("-fx-pref-height: 400px");

        Label supplementsDetails = new Label("Supplement information:");
        supplementsDetails.setStyle("-fx-font-weight: bold;");

        infoPanelBox = new TextArea();
        infoPanelBox.setEditable(false);

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(supplementsLabel, 0, 3);
        root.add(supplementChoice, 0, 4);
        root.add(supplementsDetails, 1, 3);
        root.add(infoPanelBox, 1, 4, 2, 1);
        root.add(submitButton, 1, 6);
    }

    public void deleteCustomerMode() {
        /*
         ASSUMPTIONS: CANNOT DELETE A PAYING CUSTOMER THAT HAS ASSOCIATE CUSTOMER
         */
        homePage();
        Label customersLabel = new Label("Select customer to delete:");
        customersLabel.setStyle("-fx-font-weight: bold;");
        customerChoice = new ListView<>();
        customerChoice.setStyle("-fx-pref-height: 400px");

        Label customersDetails = new Label("Customer information:");
        customersDetails.setStyle("-fx-font-weight: bold;");

        infoPanelBox = new TextArea();
        infoPanelBox.setEditable(false);

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(customersLabel, 0, 3);
        root.add(customerChoice, 0, 4);
        root.add(customersDetails, 1, 3);
        root.add(infoPanelBox, 1, 4, 2, 1);
        root.add(submitButton, 1, 6);
    }

    public void magazineViewCheck() {
        homePage();
        viewButton.setDisable(true);

        Label viewCheckHeader = new Label("Select a magazine to view:");
        viewCheckHeader.setStyle("-fx-font-weight: bold;");

        magazineChoice = new ComboBox<>();
        magazineChoice.setPromptText("Options");
        magazineChoice.setStyle("-fx-pref-width: 200px");

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(viewCheckHeader, 1, 4);
        root.add(magazineChoice, 1, 5);
        root.add(submitButton, 1, 6);
    }

    public void magazineEditCheck() {
        homePage();
        editButton.setDisable(true);

        Label editCheckHeader = new Label("Select a magazine to edit:");
        editCheckHeader.setStyle("-fx-font-weight: bold;");

        magazineChoice = new ComboBox<>();
        magazineChoice.setPromptText("Options");
        magazineChoice.setStyle("-fx-pref-width: 200px");

        // Create a button for submitting the data
        submitButton = new Button("Submit");
        submitButton.setMinSize(200, 25);

        root.add(editCheckHeader, 1, 4);
        root.add(magazineChoice, 1, 5);
        root.add(submitButton, 1, 6);
    }

    public Button getViewButton() {
        return viewButton;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Button getAddMagazineButton() {
        return addMagazineButton;
    }

    public Button getLoadMagazineButton() {
        return loadMagazineButton;
    }

    public Button getSaveMagazineButton() {
        return saveMagazineButton;
    }

    public Button getAddSupplementButton() {
        return addSupplementButton;
    }

    public Button getAddCustomerButton() {
        return addCustomerButton;
    }

    public Button getEditSupplementButton() {
        return editSupplementButton;
    }

    public Button getEditCustomerButton() {
        return editCustomerButton;
    }

    public Button getDeleteSupplementButton() {
        return deleteSupplementButton;
    }

    public Button getDeleteCustomerButton() {
        return deleteCustomerButton;
    }

    public ListView<Supplement> getSupplementsView() {
        return supplementsView;
    }

    public ListView<Supplement> getSupplementChoice() {
        return supplementChoice;
    }

    public ListView<Supplement> getOldSupplements() {
        return oldSupplements;
    }

    public ListView<Customer> getCustomersView() {
        return customersView;
    }

    public ListView<Customer> getCustomerChoice() {
        return customerChoice;
    }

    public ComboBox<String> getMagazineChoice() {
        return magazineChoice;
    }

    public ComboBox<String> getTypeOfCustomerComboBox() {
        return typeOfCustomerComboBox;
    }

    public ComboBox<String> getCardType() {
        return cardType;
    }

    public ComboBox<PayingCustomer> getPayingCustomerChoice() {
        return payingCustomerChoice;
    }

    public TextArea getInfoPanelBox() {
        return infoPanelBox;
    }

    public TextField getMagazineNameTextField() {
        return magazineNameTextField;
    }

    public TextField getSupplementNameTextField() {
        return supplementNameTextField;
    }

    public TextField getSupplementCostTextField() {
        return supplementCostTextField;
    }

    public TextField getCustomersNameTextField() {
        return customersNameTextField;
    }

    public TextField getEmailAddressTextField() {
        return emailAddressTextField;
    }

    public TextField getStreetNumberTextField() {
        return streetNumberTextField;
    }

    public TextField getStreetNameTextField() {
        return streetNameTextField;
    }

    public TextField getSuburbTextField() {
        return suburbTextField;
    }

    public TextField getPostCodeTextField() {
        return postCodeTextField;
    }

    public TextField getAccountNumberTextField() {
        return accountNumberTextField;
    }

    public TextField getTypeOfCustomerTextField() {
        return typeOfCustomerTextField;
    }

    public Label getPayingCustomerLabel() {
        return payingCustomerLabel;
    }

    public Label getAccountNumberLabel() {
        return accountNumberLabel;
    }

    public Label getCurrentMagazine() {
        return currentMagazine;
    }

    public File getSelectedFile() {
        return selectedFile;
    }
}
