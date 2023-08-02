/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 MagazineServiceApp.java
 MagazineServiceApp class, main application to control the business model and 
 navigation of GUI while providing CRUD of data
 Assumptions: 
 1. Cannot edit type of customer in edit mode
 2. Cannot delete a supplement that is currently subscribed
 3. Cannot delete a paying customer that has one or more associate customer
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MagazineServiceApp extends Application {

    private MagazineServiceGUI gui;
    private AlertHandler alert = new AlertHandler();
    private MagazineServiceHandler magazineHandler = new MagazineServiceHandler();
    private String magazineName;
    private MagazineService magazineService = new MagazineService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gui = new MagazineServiceGUI(primaryStage);
        // Program starts at create mode
        gui.createMode();
        switchToCreateMode();
        monitorMainButtons();
    }

    private void switchToViewMode(String magazineName) {
        gui.viewMode();

        magazineService = magazineHandler.getMagazineService(magazineName);

        // Add all supplements and output to list view
        gui.getSupplementsView().getItems().addAll(magazineService.getSupplements());
        // Monitor selection
        gui.getSupplementsView().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue != null) {
                gui.getCustomersView().getSelectionModel().clearSelection();
                showSupplementInfo(newValue);
            }
        });

        // Add all customers and output to list view
        gui.getCustomersView().getItems().addAll(magazineService.getCustomers());
        // Monitor selection
        gui.getCustomersView().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue != null) {
                gui.getSupplementsView().getSelectionModel().clearSelection();
                showCustomerInfo(newValue);
            }
        });

        gui.getCurrentMagazine().setText("Currently viewing: " + magazineName);

        monitorMainButtons();
    }

    private void switchToCreateMode() {
        gui.createMode();

        gui.getAddMagazineButton().setOnAction(e -> addMagazineMode());
        gui.getLoadMagazineButton().setOnAction(e -> loadMagazineMode());
        gui.getSaveMagazineButton().setOnAction(e -> saveMagazineMode());

        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
    }

    private void addMagazineMode() {
        gui.addMagazineMode();
        monitorMainButtons();

        gui.getSubmitButton().setOnAction(e -> {
            magazineName = gui.getMagazineNameTextField().getText();
            // To check if magazine field is empty
            if (!magazineName.trim().isEmpty()) {
                // To check length of magazine name does not exceed 50 letters
                if (magazineName.length() > 50) {
                    alert.showAlert("Magazine name cannot be more than 50 characters");
                } else {
                    // Check for same magazine name
                    if (magazineHandler.compareMagazineName(magazineName)) {
                        alert.showAlert("'" + magazineName + "' already exist");
                    } else {
                        magazineHandler.addMagazineService(magazineName);
                        switchToCreateMode();
                    }
                }
            } else {
                gui.getMagazineNameTextField().clear();
                alert.showAlert("Magazine name cannot be empty or contain only whitespaces");
            }
        });

        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
    }

    private void loadMagazineMode() {
        gui.loadMagazineMode();

        // Check if file is selected
        if (gui.getSelectedFile() != null) {
            for (File file : gui.getSelectedFile()) {
                magazineName = file.getName().replace(".ser", "");
                magazineHandler.loadMagazineFromFile(magazineName);
            }
        } else {
            alert.showAlert("No file selected");
        }
    }

    private void saveMagazineMode() {
        gui.saveMagazineMode();
        monitorMainButtons();

        gui.getMagazineChoice().getItems().addAll(magazineHandler.getAllMagazineNames());

        gui.getSubmitButton().setOnAction(e -> {
            // Retrieve selected magazine
            magazineName = gui.getMagazineChoice().getSelectionModel().getSelectedItem();
            // Check if magazine is selected
            if (magazineName != null) {
                magazineHandler.saveMagazineToFile(magazineName);
                switchToCreateMode();
            } else {
                gui.getMagazineChoice().setValue(null);
                alert.showAlert("No magazine selected");
            }
        });
    }

    private void switchToEditMode(String magazineName) {
        gui.editMode();

        gui.getAddSupplementButton().setOnAction(e -> addSupplementMode(magazineName));
        gui.getAddCustomerButton().setOnAction(e -> addCustomerMode(magazineName));
        gui.getEditSupplementButton().setOnAction(e -> editSupplementMode(magazineName));
        gui.getEditCustomerButton().setOnAction(e -> editCustomerMode(magazineName));
        gui.getDeleteSupplementButton().setOnAction(e -> deleteSupplementMode(magazineName));
        gui.getDeleteCustomerButton().setOnAction(e -> deleteCustomerMode(magazineName));

        gui.getCurrentMagazine().setText("Currently editing: " + magazineName);

        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getCreateButton().setOnAction(e -> switchToCreateMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
    }

    private void addSupplementMode(String magazineName) {
        gui.addSupplementMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            String supplementName = gui.getSupplementNameTextField().getText();
            // If no supplement name provided
            if (!supplementName.trim().isEmpty()) {
                try {
                    double supplementCost = Double.parseDouble(gui.getSupplementCostTextField().getText());
                    Supplement supplement = new Supplement(supplementName, supplementCost);
                    magazineService.addSupplement(supplement);
                    switchToEditMode(magazineName);
                } catch (Exception ex) {
                    gui.getSupplementCostTextField().clear();
                    alert.showAlert("Please input numbers only for cost of supplement");
                }
            } else {
                gui.getSupplementNameTextField().clear();
                alert.showAlert("Supplement name cannot be empty or contain only whitespaces");
            }
        });
    }

    private void addCustomerMode(String magazineName) {
        gui.addCustomerMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        addCustomerFillData(magazineService);

        ArrayList<Supplement>[] supplements = new ArrayList[]{new ArrayList<>()};
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            supplements[0] = new ArrayList<>(gui.getSupplementChoice().getSelectionModel().getSelectedItems());
        });

        // Set event handler for type of customer
        gui.getTypeOfCustomerComboBox().setOnAction(e -> {
            switch (gui.getTypeOfCustomerComboBox().getValue()) {
                case "Paying Customer":
                    setPayingCustomerVisible();
                    break;
                case "Associate Customer":
                    setAssociateCustomerVisible();
                    break;
            }
        });

        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            ArrayList<Boolean> validateList = new ArrayList<>();
            validateList = addCustomerValidation(validateList);

            // Check validation array
            Boolean isValidated = checkValidateArray(validateList);

            // All fields validated
            if (isValidated) {
                switch (gui.getTypeOfCustomerComboBox().getValue()) {
                    // If paying customer, add details to paying customer object
                    case "Paying Customer":
                        PayingCustomer payingCustomer = new PayingCustomer();
                        setCustomerSpecificData(payingCustomer, supplements);
                        payingCustomer.setPaymentMethod(new PaymentMethod(
                                gui.getCardType().getValue(),
                                Integer.parseInt(gui.getAccountNumberTextField().getText())));
                        // Update magazine service
                        magazineService.addCustomer(payingCustomer);
                        break;
                    // If associate customer, add details to associate customer object
                    case "Associate Customer":
                        AssociateCustomer associateCustomer = new AssociateCustomer();
                        setCustomerSpecificData(associateCustomer, supplements);
                        // Selection of paying customer
                        PayingCustomer selectedPayingCustomer = gui.getPayingCustomerChoice().getValue();
                        // Adding associate customer to selected paying customer
                        selectedPayingCustomer.addAssociateCustomer(associateCustomer);
                        // Update magazine service
                        magazineService.addCustomer(associateCustomer);
                        break;
                }
                switchToEditMode(magazineName);
            }
        });
    }

    private void editSupplementMode(String magazineName) {
        gui.editSupplementMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        // Adding existing supplements
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());

        // Monitor selection
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            gui.getSupplementNameTextField().setText(newValue.getName());
            gui.getSupplementCostTextField().setText(String.valueOf(newValue.getCost()));
        });

        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            Supplement supplement = gui.getSupplementChoice().getSelectionModel().getSelectedItem();
            // If no selection
            if (supplement != null) {
                String supplementName = gui.getSupplementNameTextField().getText();
                // If no supplement name
                if (!supplementName.trim().isEmpty()) {
                    try {
                        supplement.setName(supplementName);
                        supplement.setCost(Double.parseDouble(gui.getSupplementCostTextField().getText()));
                        switchToEditMode(magazineName);
                    } catch (Exception ex) {
                        gui.getSupplementCostTextField().clear();
                        alert.showAlert("Please input numbers only for the cost of the supplement");
                    }
                } else {
                    gui.getSupplementNameTextField().clear();
                    alert.showAlert("Supplement name cannot be empty or contain only whitespaces");
                }
            } else {
                alert.showAlert("Please select a supplement to edit");
            }
        });
    }

    private void editCustomerMode(String magazineName) {
        gui.editCustomerMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        editCustomerFillData();

        ArrayList<Supplement>[] supplements = new ArrayList[]{new ArrayList<>()};
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            supplements[0] = new ArrayList<>(gui.getSupplementChoice().getSelectionModel().getSelectedItems());
        });

        // Set text based on selected customer
        gui.getCustomerChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            editCustomerSetField(newValue, magazineService);
        });

        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            ArrayList<Boolean> validateList = new ArrayList<>();
            validateList = editCustomerValidation(validateList);
            // Check validation array
            Boolean isValidated = checkValidateArray(validateList);
            // All fields validated
            if (isValidated) {
                editCustomerSetData(supplements, magazineService);
                switchToEditMode(magazineName);
            }
        });
    }

    private void deleteSupplementMode(String magazineName) {
        gui.deleteSupplementMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        // Add existing supplements to list view
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());

        // Monitor selection
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            showSupplementInfo(newValue);
        });

        gui.getSubmitButton().setOnAction(e -> {
            Supplement supplement = gui.getSupplementChoice().getSelectionModel().getSelectedItem();
            // Check if supplement is selected
            if (supplement != null) {
                Boolean isSubscribed = false;
                for (Customer customer : magazineService.getCustomers()) {
                    // Check if supplement is currently subscribed
                    for (Supplement tempSupplement : customer.getSupplement()) {
                        if (tempSupplement.equals(supplement)) {
                            isSubscribed = true;
                            break;
                        }
                    }
                    if (isSubscribed) {
                        break;
                    }
                }
                if (isSubscribed) {
                    alert.showAlert("You are not able to delete a supplement that has subscriptions");
                } else {
                    magazineService.removeSupplement(supplement);
                    switchToEditMode(magazineName);
                }
            } else {
                alert.showAlert("Please select a supplement to delete");
            }
        });
    }

    private void deleteCustomerMode(String magazineName) {
        gui.deleteCustomerMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        // Add existing customers to list view
        gui.getCustomerChoice().getItems().addAll(magazineService.getCustomers());

        // Monitor selection
        gui.getCustomerChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            showCustomerInfo(newValue);
        });

        gui.getSubmitButton().setOnAction(e -> {
            Customer customer = gui.getCustomerChoice().getSelectionModel().getSelectedItem();
            // Check if customer is selected
            if (customer != null) {
                if (customer instanceof PayingCustomer) {
                    PayingCustomer selectedPayingCustomer = (PayingCustomer) customer;
                    // Check if paying customer has one or more associate customer
                    if (selectedPayingCustomer.containsAssociateCustomer()) {
                        alert.showAlert("You are not able to delete a paying customer that has associate customer(s)");
                    } else {
                        magazineService.removeCustomer(customer);
                        switchToEditMode(magazineName);
                    }
                } else if (customer instanceof AssociateCustomer) {
                    // For loop to remove associate customer from paying customer
                    deleteAssociateCustomerFromPayingCustomer(customer, magazineService);
                    magazineService.removeCustomer(customer);
                    switchToEditMode(magazineName);
                }
            } else {
                alert.showAlert("Please select a customer to delete");
            }
        });
    }

    // To perform check for magazine choice before viewing
    private void checkMagazineViewMode() {
        gui.magazineViewCheck();

        gui.getMagazineChoice().getItems().addAll(magazineHandler.getAllMagazineNames());

        gui.getSubmitButton().setOnAction(e -> {
            magazineName = gui.getMagazineChoice().getSelectionModel().getSelectedItem();
            if (magazineName != null) {
                switchToViewMode(magazineName);
                gui.getMagazineChoice().setValue(null);
            } else {
                gui.getMagazineChoice().setValue(null);
                alert.showAlert("No magazine selected");
            }
        });

        gui.getCreateButton().setOnAction(e -> switchToCreateMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
    }

    // To perform check for magazine choice before editing
    private void checkMagazineEditMode() {
        gui.magazineEditCheck();

        gui.getMagazineChoice().getItems().addAll(magazineHandler.getAllMagazineNames());

        gui.getSubmitButton().setOnAction(e -> {
            magazineName = gui.getMagazineChoice().getSelectionModel().getSelectedItem();
            if (magazineName != null) {
                switchToEditMode(magazineName);
                gui.getMagazineChoice().setValue(null);
            } else {
                gui.getMagazineChoice().setValue(null);
                alert.showAlert("No magazine selected");
            }
        });

        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getCreateButton().setOnAction(e -> switchToCreateMode());
    }

    private void showSupplementInfo(Supplement supplement) {
        String text = "Name: " + supplement.getName()
                + "\nWeekly Cost: $" + String.format(Locale.US, "%.2f", supplement.getCost());

        gui.getInfoPanelBox().setText(text);
    }

    private void showCustomerInfo(Customer customer) {
        customer.getSupplement();
        String text = "Name: " + customer.getName()
                + "\n\nAddress: " + customer.getAddress() + "\n\nEmail: "
                + customer.getEmail() + "\n\nSupplements Subscribed to:";
        for (int i = 0; i < customer.getSupplement().size(); i++) {
            int count = i + 1;
            text += ("\n" + count + ". " + customer.getSupplement().get(i).getName());
        }
        if (customer instanceof PayingCustomer) {
            PayingCustomer payingCustomer = (PayingCustomer) customer;
            text += "\n\nYou are a Paying Customer.\n\nPayment Method:\n"
                    + payingCustomer.getPaymentMethod() + "\n\nAssociate Customer:";
            for (Customer associateCustomer : payingCustomer.getAssociateCustomers()) {
                text += "\n" + associateCustomer.getName();
            }
            // Monthly billing history
            double totalSupplementsCost = payingCustomer.calculateTotalSupplementsCost();
            text += "\n\nMonthly cost: $" + String.format(Locale.US, "%.2f", totalSupplementsCost);
        } else if (customer instanceof AssociateCustomer) {
            text += "\n\nYou are an Associate Customer.";
        }

        gui.getInfoPanelBox().setText(text);
    }

    private static Boolean isValidEmailAddress(String emailAddress) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }

    private void addCustomerFillData(MagazineService magazineService) {
        gui.getTypeOfCustomerComboBox().getItems().addAll("Paying Customer", "Associate Customer");

        // Add supplements to list view
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());

        // Create customer arraylist and add all customers
        ArrayList<Customer> customerList = new ArrayList<>();
        ArrayList<PayingCustomer> payingCustomerList = new ArrayList<>();
        customerList.addAll(magazineService.getCustomers());

        // Check if paying customer
        for (Customer customer : customerList) {
            if (customer instanceof PayingCustomer) {
                payingCustomerList.add((PayingCustomer) customer);
            }
        }
        gui.getPayingCustomerChoice().getItems().addAll(payingCustomerList);
        gui.getPayingCustomerChoice().setPromptText("Select One");
    }

    private ArrayList<Boolean> addCustomerValidation(ArrayList<Boolean> validateList) {
        // Validation for customer type field
        if (gui.getTypeOfCustomerComboBox().getSelectionModel().isEmpty()) {
            alert.showAlert("Please select type of customer");
            validateList.add(false);
        } else {
            validateList.add(true);
        }

        // Validation for customer name
        validateIfEmpty(gui.getCustomersNameTextField(), "Customer name cannot be empty or contain only whitespaces", validateList);

        // Validate email address
        if (isValidEmailAddress(gui.getEmailAddressTextField().getText())) {
            validateList.add(true);
        } else {
            alert.showAlert("Invalid email address");
            validateList.add(false);
        }

        // Validation for relevant fields
        validateIfEmpty(gui.getStreetNumberTextField(), "Street number cannot be empty or contain only whitespaces", validateList);
        validateIfEmpty(gui.getStreetNameTextField(), "Street name cannot be empty or contain only whitespaces", validateList);
        validateIfEmpty(gui.getSuburbTextField(), "Suburb cannot be empty or contain only whitespaces", validateList);
        validateIfEmpty(gui.getPostCodeTextField(), "Postcode cannot be empty or contain only whitespaces", validateList);

        // Validation for supplement field
        if (gui.getSupplementChoice().getSelectionModel().isEmpty()) {
            alert.showAlert("Please select at least one supplement");
            validateList.add(false);
        } else {
            validateList.add(true);
        }

        String customer = gui.getTypeOfCustomerComboBox().getValue();
        // Check if paying customer
        if (customer != null && customer.equals("Paying Customer")) {
            try {
                Integer.parseInt(gui.getAccountNumberTextField().getText());
                validateList.add(true);
            } catch (Exception ex) {
                gui.getAccountNumberTextField().clear();
                alert.showAlert("Please input numbers only for account number");
                validateList.add(false);
            }
            if (gui.getCardType().getSelectionModel().isEmpty()) {
                alert.showAlert("Please select card type");
                validateList.add(false);
            } else {
                validateList.add(true);
            }
        }
        // Check if associate customer
        if (customer != null && customer.equals("Associate Customer")) {
            if (gui.getPayingCustomerChoice().getSelectionModel().isEmpty()) {
                alert.showAlert("Please select a paying customer");
                validateList.add(false);
            } else {
                validateList.add(true);
            }
        }
        return validateList;
    }

    private void editCustomerFillData() {
        // Adding customers to list view
        gui.getCustomerChoice().getItems().addAll(magazineService.getCustomers());

        // Adding supplements subscribed by respective customer
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());

        // Adding all customers to list view
        ArrayList<Customer> customerList = new ArrayList<>();
        ArrayList<PayingCustomer> payingCustomerList = new ArrayList<>();
        customerList.addAll(magazineService.getCustomers());

        // Check for paying customer
        for (Customer customer : customerList) {
            if (customer instanceof PayingCustomer) {
                payingCustomerList.add((PayingCustomer) customer);
            }
        }

        gui.getPayingCustomerChoice().getItems().addAll(payingCustomerList);
        // Card type field
        gui.getCardType().getItems().addAll("Credit Card", "Debit Card");
    }

    private void editCustomerSetField(Customer newValue, MagazineService magazineService) {
        if (newValue instanceof PayingCustomer) {
            setPayingCustomerVisible();
            gui.getTypeOfCustomerTextField().setText("Paying Customer");
            PayingCustomer payingCustomer = (PayingCustomer) newValue;
            gui.getAccountNumberTextField().setText(String.valueOf(payingCustomer.getPaymentMethod().getAccountNo()));
            gui.getCardType().setValue(payingCustomer.getPaymentMethod().getCardType());
        } else if (newValue instanceof AssociateCustomer) {
            setAssociateCustomerVisible();
            gui.getTypeOfCustomerTextField().setText("Associate Customer");
            editCustomerSetPayingCustomer(newValue, magazineService);
        }
        gui.getCustomersNameTextField().setText(newValue.getName());
        gui.getEmailAddressTextField().setText(newValue.getEmail());
        gui.getStreetNumberTextField().setText(newValue.getAddress().getStreetNumber());
        gui.getStreetNameTextField().setText(newValue.getAddress().getStreetName());
        gui.getSuburbTextField().setText(newValue.getAddress().getSuburb());
        gui.getPostCodeTextField().setText(newValue.getAddress().getPostcode());
        gui.getOldSupplements().getItems().setAll(newValue.getSupplement());
    }

    private void editCustomerSetPayingCustomer(Customer newValue, MagazineService magazineService) {
        for (Customer customer : magazineService.getCustomers()) {
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customer;
                // Check for paying customer that has the associate customer
                if (payingCustomer.compareAssociateCustomer(newValue.getName())) {
                    // Set associated paying customer
                    gui.getPayingCustomerChoice().setValue(payingCustomer);
                }
            }
        }
    }

    private ArrayList<Boolean> editCustomerValidation(ArrayList<Boolean> validateList) {
        // If no customer selected
        if (gui.getCustomerChoice().getSelectionModel().getSelectedItem() != null) {

            // Validation for customer name
            validateIfEmpty(gui.getCustomersNameTextField(), "Customer name cannot be empty or contain only whitespaces", validateList);

            // Validate email address
            if (isValidEmailAddress(gui.getEmailAddressTextField().getText())) {
                validateList.add(true);
            } else {
                alert.showAlert("Invalid email address");
                validateList.add(false);
            }

            // Validation for relevant fields
            validateIfEmpty(gui.getStreetNumberTextField(), "Street number cannot be empty or contain only whitespaces", validateList);
            validateIfEmpty(gui.getStreetNameTextField(), "Street name cannot be empty or contain only whitespaces", validateList);
            validateIfEmpty(gui.getSuburbTextField(), "Suburb cannot be empty or contain only whitespaces", validateList);
            validateIfEmpty(gui.getPostCodeTextField(), "Postcode cannot be empty or contain only whitespaces", validateList);

            // Validation for supplement field
            if (gui.getSupplementChoice().getSelectionModel().isEmpty()) {
                alert.showAlert("Please select at least one supplement");
                validateList.add(false);
            } else {
                validateList.add(true);
            }

            // Check for paying customer type
            if (gui.getTypeOfCustomerTextField().getText().equals("Paying Customer")) {
                try {
                    Integer.parseInt(gui.getAccountNumberTextField().getText());
                    validateList.add(true);
                } catch (Exception ex) {
                    gui.getAccountNumberTextField().clear();
                    alert.showAlert("Please input numbers only for account number");
                    validateList.add(false);
                }
            }
        } else {
            alert.showAlert("Please select a customer to edit");
            validateList.add(false);
        }
        return validateList;
    }

    private void editCustomerSetData(ArrayList<Supplement>[] supplements, MagazineService magazineService) {
        Customer customer = gui.getCustomerChoice().getSelectionModel().getSelectedItem();
        // If paying customer selected, update all fields
        if (gui.getTypeOfCustomerTextField().getText().equals("Paying Customer")) {
            PayingCustomer selectedPayingCustomer = (PayingCustomer) customer;
            setCustomerSpecificData(selectedPayingCustomer, supplements);
            selectedPayingCustomer.setPaymentMethod(new PaymentMethod(
                    gui.getCardType().getValue(),
                    Integer.parseInt(gui.getAccountNumberTextField().getText())));
        } // If associate customer selected, update all fields
        else if (gui.getTypeOfCustomerTextField().getText().equals("Associate Customer")) {
            AssociateCustomer selectedAssociateCustomer = (AssociateCustomer) customer;
            setCustomerSpecificData(selectedAssociateCustomer, supplements);
            PayingCustomer selectedPayingCustomer = gui.getPayingCustomerChoice().getValue();
            // To remove associate customer from paying customer and add to new
            if (!selectedPayingCustomer.compareAssociateCustomer(selectedAssociateCustomer.getName())) {
                deleteAssociateCustomerFromPayingCustomer(customer, magazineService);
                selectedPayingCustomer.addAssociateCustomer(selectedAssociateCustomer);
            }
        }
    }

    private void setCustomerSpecificData(Customer customer, ArrayList<Supplement>[] supplements) {
        customer.setName(gui.getCustomersNameTextField().getText());
        customer.setEmail(gui.getEmailAddressTextField().getText());
        customer.setAddress(new Address(
                gui.getStreetNumberTextField().getText(),
                gui.getStreetNameTextField().getText(),
                gui.getSuburbTextField().getText(),
                gui.getPostCodeTextField().getText()
        ));
        customer.setSupplement(supplements[0]);
    }

    private void deleteAssociateCustomerFromPayingCustomer(Customer selectedCustomer, MagazineService magazineService) {
        for (Customer customer : magazineService.getCustomers()) {
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customer;
                if (payingCustomer.compareAssociateCustomer(selectedCustomer.getName())) {
                    // Delete associate customer from paying customer
                    payingCustomer.removeAssociateCustomer(selectedCustomer);
                }
            }
        }
    }

    private void validateIfEmpty(TextField textField, String errorMessage, ArrayList<Boolean> validateList) {
        if (textField.getText().trim().isEmpty()) {
            textField.clear();
            alert.showAlert(errorMessage);
            validateList.add(false);
        } else {
            validateList.add(true);
        }
    }

    private Boolean checkValidateArray(ArrayList<Boolean> validateList) {
        Boolean isValidated = true;
        for (Boolean value : validateList) {
            if (!value) {
                isValidated = false;
                break;
            }
        }
        return isValidated;
    }

    private void setPayingCustomerVisible() {
        gui.getPayingCustomerLabel().setVisible(false);
        gui.getPayingCustomerChoice().setVisible(false);
        gui.getAccountNumberLabel().setVisible(true);
        gui.getAccountNumberTextField().setVisible(true);
        gui.getCardType().setVisible(true);
    }

    private void setAssociateCustomerVisible() {
        gui.getPayingCustomerLabel().setVisible(true);
        gui.getPayingCustomerChoice().setValue(null);
        gui.getPayingCustomerChoice().setVisible(true);
        gui.getAccountNumberLabel().setVisible(false);
        gui.getAccountNumberTextField().clear();
        gui.getAccountNumberTextField().setVisible(false);
        gui.getCardType().setValue(null);
        gui.getCardType().setVisible(false);
    }

    private void monitorMainButtons() {
        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getCreateButton().setOnAction(e -> switchToCreateMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
    }
}
