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

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MagazineServiceApp extends Application {

    private MagazineServiceGUI gui;
    private AlertHandler alert = new AlertHandler();
    private MagazineHandler magazineHandler = new MagazineHandler();
    private String magazineName;
    private MagazineService magazineService = new MagazineService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gui = new MagazineServiceGUI(primaryStage);
        gui.createMode();
        switchToCreateMode();
        monitorMainButtons();
    }

    private void switchToViewMode(String magazineName) {
        gui.viewMode();

        magazineService = magazineHandler.getMagazineService(magazineName);

        gui.getSupplementsView().getItems().addAll(magazineService.getSupplements());
        gui.getSupplementsView().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            showSupplementInfo(newValue);
        });

        // 
        gui.getCustomersView().getItems().addAll(magazineService.getCustomers());
        gui.getCustomersView().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            showCustomerInfo(newValue);
        });

        gui.getCurrentMagazine().setText("Currently viewing: " + magazineName);

        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getCreateButton().setOnAction(e -> switchToCreateMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
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
            if (!gui.getMagazineNameTextField().getText().trim().isEmpty()) {
                magazineName = gui.getMagazineNameTextField().getText();
                if (magazineName.length() > 50) {
                    alert.showAlert("Magazine name cannot be more than 50 characters");
                } else {
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

        if (gui.getSelectedFile() != null) {
            //String filePath = gui.getSelectedFile().getAbsolutePath();

            magazineName = gui.getSelectedFile().getName().replace(".ser", "");
            magazineHandler.loadMagazineFromFile(magazineName);

        } else {
            alert.showAlert("No file selected");
        }
    }

    private void saveMagazineMode() {
        gui.saveMagazineMode();
        monitorMainButtons();

        gui.getMagazineChoice().getItems().addAll(magazineHandler.getAllMagazineNames());

        gui.getSubmitButton().setOnAction(e -> {
            magazineName = gui.getMagazineChoice().getSelectionModel().getSelectedItem();
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
            if (!gui.getSupplementNameTextField().getText().trim().isEmpty()) {
                try {
                    double supplementCost = Double.parseDouble(gui.getSupplementCostTextField().getText());
                    Supplement supplement = new Supplement(gui.getSupplementNameTextField().getText(), supplementCost);
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

        gui.getTypeOfCustomerComboBox().getItems().addAll("Paying Customer", "Associate Customer");
        // 
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());
        ArrayList<Supplement>[] supplements = new ArrayList[]{new ArrayList<>()};
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            supplements[0] = new ArrayList<>(gui.getSupplementChoice().getSelectionModel().getSelectedItems());
        });
        //
        ArrayList<Customer> customerList = new ArrayList<>();
        ArrayList<PayingCustomer> payingCustomerList = new ArrayList<>();
        customerList.addAll(magazineService.getCustomers());
        for (Customer customer : customerList) {
            if (customer instanceof PayingCustomer) {
                payingCustomerList.add((PayingCustomer) customer);
            }
        }
        gui.getPayingCustomerChoice().getItems().addAll(payingCustomerList);
        gui.getPayingCustomerChoice().setPromptText("Select One");

        // Set event handler for type of customer
        gui.getTypeOfCustomerComboBox().setOnAction(e -> {
            switch (gui.getTypeOfCustomerComboBox().getValue()) {
                case "Paying Customer":
                    gui.getPayingCustomerLabel().setVisible(false);
                    gui.getPayingCustomerChoice().setVisible(false);
                    gui.getAccountNumberLabel().setVisible(true);
                    gui.getAccountNumberTextField().setVisible(true);
                    gui.getCardType().setVisible(true);
                    break;
                case "Associate Customer":
                    gui.getPayingCustomerLabel().setVisible(true);
                    gui.getPayingCustomerChoice().setValue(null);
                    gui.getPayingCustomerChoice().setVisible(true);
                    gui.getAccountNumberLabel().setVisible(false);
                    gui.getAccountNumberTextField().clear();
                    gui.getAccountNumberTextField().setVisible(false);
                    gui.getCardType().setValue(null);
                    gui.getCardType().setVisible(false);
                    break;
            }
        });

        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            ArrayList<Boolean> validateList = new ArrayList<>();

            // Validation for customer type field
            if (gui.getTypeOfCustomerComboBox().getSelectionModel().isEmpty()) {
                alert.showAlert("Please select type of customer");
                validateList.add(false);
            } else {
                validateList.add(true);
            }

            // Validation for relevant fields
            validateIfEmpty(gui.getCustomersNameTextField(), "Customer name cannot be empty or contain only whitespaces", validateList);
            validateIfEmpty(gui.getEmailAddressTextField(), "Email address cannot be empty or contain only whitespaces", validateList);
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
            int accountNumber = 0;
            if (gui.getTypeOfCustomerComboBox().getValue().equals("Paying Customer")) {
                try {
                    accountNumber = Integer.parseInt(gui.getAccountNumberTextField().getText());
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
            if (gui.getTypeOfCustomerComboBox().getValue().equals("Associate Customer")) {
                if (gui.getPayingCustomerChoice().getSelectionModel().isEmpty()) {
                    alert.showAlert("Please select a paying customer");
                    validateList.add(false);
                } else {
                    validateList.add(true);
                }
            }

            // Check validation array
            Boolean isValidated = true;
            for (Boolean value : validateList) {
                if (!value) {
                    isValidated = false;
                    break;
                }
            }

            // All fields validated
            if (isValidated) {
                switch (gui.getTypeOfCustomerComboBox().getValue()) {
                    case "Paying Customer":
                        PayingCustomer payingCustomer = new PayingCustomer(gui.getCustomersNameTextField().getText(),
                                gui.getEmailAddressTextField().getText(), new Address(gui.getStreetNumberTextField().getText(),
                                        gui.getStreetNameTextField().getText(), gui.getSuburbTextField().getText(),
                                        gui.getPostCodeTextField().getText()), new PaymentMethod(gui.getCardType().getValue(), accountNumber));
                        payingCustomer.setSupplement(supplements[0]);
                        magazineService.addCustomer(payingCustomer);
                        break;
                    case "Associate Customer":
                        AssociateCustomer associateCustomer = new AssociateCustomer(gui.getCustomersNameTextField().getText(),
                                gui.getEmailAddressTextField().getText(), new Address(gui.getStreetNumberTextField().getText(),
                                        gui.getStreetNameTextField().getText(), gui.getSuburbTextField().getText(),
                                        gui.getPostCodeTextField().getText()));
                        associateCustomer.setSupplement(supplements[0]);
                        PayingCustomer selectedPayingCustomer = gui.getPayingCustomerChoice().getValue();
                        selectedPayingCustomer.addAssociateCustomer(associateCustomer);
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

        // Selecting supplement
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());

        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            gui.getSupplementNameTextField().setText(newValue.getName());
            gui.getSupplementCostTextField().setText(String.valueOf(newValue.getCost()));
        });
        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            if (gui.getSupplementChoice().getSelectionModel().getSelectedItem() != null) {
                String supplementName = gui.getSupplementNameTextField().getText();
                if (!supplementName.trim().isEmpty()) {
                    try {
                        double supplementCost = Double.parseDouble(gui.getSupplementCostTextField().getText());
                        gui.getSupplementChoice().getSelectionModel().getSelectedItem().setName(supplementName);
                        gui.getSupplementChoice().getSelectionModel().getSelectedItem().setCost(supplementCost);
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
        /*
         ASSUMPTIONS: CANNOT EDIT TYPE OF CUSTOMER
         */
        gui.editCustomerMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        // Selecting customer
        gui.getCustomerChoice().getItems().addAll(magazineService.getCustomers());
        // Supplement field
        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());
        ArrayList<Supplement>[] supplements = new ArrayList[]{new ArrayList<>()};
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            supplements[0] = new ArrayList<>(gui.getSupplementChoice().getSelectionModel().getSelectedItems());
        });
        // Paying customer field
        ArrayList<Customer> customerList = new ArrayList<>();
        ArrayList<PayingCustomer> payingCustomerList = new ArrayList<>();
        customerList.addAll(magazineService.getCustomers());
        for (Customer customer : customerList) {
            if (customer instanceof PayingCustomer) {
                payingCustomerList.add((PayingCustomer) customer);
            }
        }
        gui.getPayingCustomerChoice().getItems().addAll(payingCustomerList);
        // Card type field
        gui.getCardType().getItems().addAll("Credit Card", "Debit Card");
        // Set text based on selected customer
        gui.getCustomerChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue instanceof PayingCustomer) {
                gui.getPayingCustomerLabel().setVisible(false);
                gui.getPayingCustomerChoice().setVisible(false);
                gui.getAccountNumberLabel().setVisible(true);
                gui.getAccountNumberTextField().setVisible(true);
                gui.getCardType().setVisible(true);
                gui.getTypeOfCustomerTextField().setText("Paying Customer");
                PayingCustomer payingCustomer = (PayingCustomer) newValue;
                gui.getAccountNumberTextField().setText(String.valueOf(payingCustomer.getPaymentMethod().getAccountNo()));
                gui.getCardType().setValue(payingCustomer.getPaymentMethod().getCardType());
            } else if (newValue instanceof AssociateCustomer) {
                gui.getPayingCustomerLabel().setVisible(true);
                gui.getPayingCustomerChoice().setValue(null);
                gui.getPayingCustomerChoice().setVisible(true);
                gui.getAccountNumberLabel().setVisible(false);
                gui.getAccountNumberTextField().clear();
                gui.getAccountNumberTextField().setVisible(false);
                gui.getCardType().setValue(null);
                gui.getCardType().setVisible(false);
                gui.getTypeOfCustomerTextField().setText("Associate Customer");
                for (Customer customer : magazineService.getCustomers()) {
                    if (customer instanceof PayingCustomer) {
                        PayingCustomer payingCustomer = (PayingCustomer) customer;
                        if (payingCustomer.compareAssociateCustomer(newValue.getName())) {
                            // Delete associate customer from paying customer
                            gui.getPayingCustomerChoice().setValue(payingCustomer);
                        }
                    }
                }
            }
            gui.getCustomersNameTextField().setText(newValue.getName());
            gui.getEmailAddressTextField().setText(newValue.getEmail());
            gui.getStreetNumberTextField().setText(newValue.getAddress().getStreetNumber());
            gui.getStreetNameTextField().setText(newValue.getAddress().getStreetName());
            gui.getSuburbTextField().setText(newValue.getAddress().getSuburb());
            gui.getPostCodeTextField().setText(newValue.getAddress().getPostcode());
            gui.getOldSupplements().getItems().setAll(newValue.getSupplement());
        });
        // Set event handler for the submit button
        gui.getSubmitButton().setOnAction(e -> {
            if (gui.getCustomerChoice().getSelectionModel().getSelectedItem() != null) {
                ArrayList<Boolean> validateList = new ArrayList<>();
                // Validation for relevant fields
                validateIfEmpty(gui.getCustomersNameTextField(), "Customer name cannot be empty or contain only whitespaces", validateList);
                validateIfEmpty(gui.getEmailAddressTextField(), "Email address cannot be empty or contain only whitespaces", validateList);
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
                int accountNumber = 0;
                if (gui.getTypeOfCustomerTextField().getText().equals("Paying Customer")) {
                    try {
                        accountNumber = Integer.parseInt(gui.getAccountNumberTextField().getText());
                        validateList.add(true);
                    } catch (Exception ex) {
                        gui.getAccountNumberTextField().clear();
                        alert.showAlert("Please input numbers only for account number");
                        validateList.add(false);
                    }
                }
                // Check validation array
                Boolean isValidated = true;
                for (Boolean value : validateList) {
                    if (!value) {
                        isValidated = false;
                        break;
                    }
                }
                // All fields validated
                if (isValidated) {
                    if (gui.getTypeOfCustomerTextField().getText().equals("Paying Customer")) {
                        PayingCustomer selectedPayingCustomer = (PayingCustomer) gui.getCustomerChoice().getSelectionModel().getSelectedItem();
                        selectedPayingCustomer.setName(gui.getCustomersNameTextField().getText());
                        selectedPayingCustomer.setEmail(gui.getEmailAddressTextField().getText());
                        Address newAddress = new Address(gui.getStreetNumberTextField().getText(), gui.getStreetNameTextField().getText(),
                                gui.getSuburbTextField().getText(), gui.getPostCodeTextField().getText());
                        selectedPayingCustomer.setAddress(newAddress);
                        selectedPayingCustomer.setSupplement(supplements[0]);
                        PaymentMethod newPaymentMethod = new PaymentMethod(gui.getCardType().getValue(), accountNumber);
                        selectedPayingCustomer.setPaymentMethod(newPaymentMethod);
                    } else if (gui.getTypeOfCustomerTextField().getText().equals("Associate Customer")) {
                        AssociateCustomer selectedAssociateCustomer = (AssociateCustomer) gui.getCustomerChoice().getSelectionModel().getSelectedItem();
                        selectedAssociateCustomer.setName(gui.getCustomersNameTextField().getText());
                        selectedAssociateCustomer.setEmail(gui.getEmailAddressTextField().getText());
                        Address newAddress = new Address(gui.getStreetNumberTextField().getText(), gui.getStreetNameTextField().getText(),
                                gui.getSuburbTextField().getText(), gui.getPostCodeTextField().getText());
                        selectedAssociateCustomer.setAddress(newAddress);
                        selectedAssociateCustomer.setSupplement(supplements[0]);
                        if (gui.getPayingCustomerChoice().getValue() != null) {
                            PayingCustomer selectedPayingCustomer = gui.getPayingCustomerChoice().getValue();
                            if (!selectedPayingCustomer.compareAssociateCustomer(selectedAssociateCustomer.getName())) {
                                for (Customer customer : magazineService.getCustomers()) {
                                    if (customer instanceof PayingCustomer) {
                                        PayingCustomer payingCustomer = (PayingCustomer) customer;
                                        if (payingCustomer.compareAssociateCustomer(gui.getCustomerChoice().getSelectionModel().getSelectedItem().getName())) {
                                            // Delete associate customer from paying customer
                                            payingCustomer.removeAssociateCustomer(gui.getCustomerChoice().getSelectionModel().getSelectedItem());
                                        }
                                    }
                                }
                                selectedPayingCustomer.addAssociateCustomer(selectedAssociateCustomer);
                            }
                        }
                    }
                    switchToEditMode(magazineName);
                }
            } else {
                alert.showAlert("Please select a customer to edit");
            }
        });
    }

    private void deleteSupplementMode(String magazineName) {
        /*
         CANNOT DELETE A SUPPLEMENT THAT A CUSTOMER IS CURRENTLY SUBSCRIBED TO
         */
        gui.deleteSupplementMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        gui.getSupplementChoice().getItems().addAll(magazineService.getSupplements());
        gui.getSupplementChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            showSupplementInfo(newValue);
        });

        Label supplementsDetails = new Label("Supplement information:");
        supplementsDetails.setStyle("-fx-font-weight: bold;");

        gui.getSubmitButton().setOnAction(e -> {
            if (gui.getSupplementChoice().getSelectionModel().getSelectedItem() != null) {
                boolean isSubscribed = false;
                for (Customer customer : magazineService.getCustomers()) {
                    for (Supplement supplement : customer.getSupplement()) {
                        if (supplement.equals(gui.getSupplementChoice().getSelectionModel().getSelectedItem())) {
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
                    magazineService.removeSupplement(gui.getSupplementChoice().getSelectionModel().getSelectedItem());
                    switchToEditMode(magazineName);
                }
            } else {
                alert.showAlert("Please select a supplement to delete");
            }
        });
    }

    private void deleteCustomerMode(String magazineName) {
        /*
         ASSUMPTIONS: CANNOT DELETE A PAYING CUSTOMER THAT HAS ASSOCIATE CUSTOMER
         */
        gui.deleteCustomerMode();
        monitorMainButtons();

        magazineService = magazineHandler.getMagazineService(magazineName);

        gui.getCustomerChoice().getItems().addAll(magazineService.getCustomers());
        gui.getCustomerChoice().getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
            showCustomerInfo(newValue);
        });

        gui.getSubmitButton().setOnAction(e -> {
            if (gui.getCustomerChoice().getSelectionModel().getSelectedItem() != null) {
                if (gui.getCustomerChoice().getSelectionModel().getSelectedItem() instanceof PayingCustomer) {
                    PayingCustomer selectedPayingCustomer = (PayingCustomer) gui.getCustomerChoice().getSelectionModel().getSelectedItem();
                    if (selectedPayingCustomer.containsAssociateCustomer()) {
                        alert.showAlert("You are not able to delete a paying customer that has associate customer(s)");
                    } else {
                        magazineService.removeCustomer(gui.getCustomerChoice().getSelectionModel().getSelectedItem());
                        switchToEditMode(magazineName);
                    }
                } else if (gui.getCustomerChoice().getSelectionModel().getSelectedItem() instanceof AssociateCustomer) {
                    // For loop to remove associate customer from paying customer
                    for (Customer tempCustomer : magazineService.getCustomers()) {
                        if (tempCustomer instanceof PayingCustomer) {
                            PayingCustomer tempPayingCustomer = (PayingCustomer) tempCustomer;
                            if (tempPayingCustomer.compareAssociateCustomer(gui.getCustomerChoice().getSelectionModel().getSelectedItem().getName())) {
                                // Delete associate customer from paying customer
                                tempPayingCustomer.removeAssociateCustomer(gui.getCustomerChoice().getSelectionModel().getSelectedItem());
                            }
                        }
                    }
                    magazineService.removeCustomer(gui.getCustomerChoice().getSelectionModel().getSelectedItem());
                    switchToEditMode(magazineName);
                }
            } else {
                alert.showAlert("Please select a customer to delete");
            }
        });
    }

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
                + "\nCost: $" + supplement.getCost();
        // SUB CUSTOMERS

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
        } else if (customer instanceof AssociateCustomer) {
            text += "\n\nYou are an Associate Customer.";
        }
        // BILLING HISTORY

        gui.getInfoPanelBox().setText(text);
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

    private void monitorMainButtons() {
        gui.getViewButton().setOnAction(e -> checkMagazineViewMode());
        gui.getCreateButton().setOnAction(e -> switchToCreateMode());
        gui.getEditButton().setOnAction(e -> checkMagazineEditMode());
    }
}
