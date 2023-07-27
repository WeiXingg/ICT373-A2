/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 26/7/2023
 PayingCustomer.java
 PayingCustomer class, getters and setters for the necessary variables
 Contains a PaymentMethod object as well as an arraylist of associate customer
 */

import java.util.ArrayList;

public class PayingCustomer extends Customer {

    private PaymentMethod paymentMethod;
    private ArrayList<Customer> associateCustomers;

    public PayingCustomer() {
        super();
        this.paymentMethod = new PaymentMethod();
        this.associateCustomers = new ArrayList<>();
    }
    
    // Parameterized constructor
    public PayingCustomer(String newName, String newEmail, Address newAddress, PaymentMethod newPaymentMethod) {
        super(newName, newEmail, newAddress);
        paymentMethod = newPaymentMethod;
        this.associateCustomers = new ArrayList<>();
    }

    // Getter for payment method object
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    // Setter for payment method object
    public void setPaymentMethod(PaymentMethod newPaymentMethod) {
        paymentMethod = newPaymentMethod;
    }

    // Getter for arraylist of associate customers
    public ArrayList<Customer> getAssociateCustomers() {
        return associateCustomers;
    }

    // Setter for arraylist of associate customers
    public void setAssociateCustomers(ArrayList<Customer> newAssociateCustomers) {
        associateCustomers = newAssociateCustomers;
    }

    // Adding a customer to become an associate customer
    public void addAssociateCustomer(Customer customer) {
        associateCustomers.add(customer);
    }

    // Removing a customer from an associate customer
    public void removeAssociateCustomer(Customer customer) {
        associateCustomers.remove(customer);
    }

    // Comparison method for associate customer
    public boolean compareAssociateCustomer(String associateCustomerName) {
        for (Customer customer : associateCustomers) {
            // Check for associate customer only
            if (customer instanceof AssociateCustomer) {
                // Casting to associate customer
                AssociateCustomer associateCustomer = (AssociateCustomer) customer;
                // Check same name return true
                if (associateCustomer.getName().equals(associateCustomerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // To check if paying customer contains an associate customer
    public boolean containsAssociateCustomer() {
        for (Customer customer : associateCustomers) {
            // Check for associate customer and return true if found
            if (customer instanceof AssociateCustomer) {
                return true;
            }
        }
        return false;
    }

    // Total supplement cost for paying customer and all associate customer
    public double calculateTotalSupplementsCost() {
        double totalCost = 0.0;

        // Calculate the cost of supplements for the main paying customer
        totalCost += calculateSupplementsCost(this);

        // Calculate the cost of supplements for each associate customer
        for (Customer associateCustomer : associateCustomers) {
            if (associateCustomer instanceof AssociateCustomer) {
                totalCost += calculateSupplementsCost(associateCustomer);
            }
        }
        return totalCost;
    }

    // Calculation of paying customer
    private double calculateSupplementsCost(Customer customer) {
        double cost = 0.0;

        for (Supplement supplement : customer.getSupplement()) {
            // Monthly = weeklycost x 4
            cost += (supplement.getCost() * 4);
        }
        return cost;
    }
}
