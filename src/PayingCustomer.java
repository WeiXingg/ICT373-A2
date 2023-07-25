/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 13/6/2023
 PayingCustomer.java
 PayingCustomer class, getters and setters for the necessary variables
 */

import java.util.ArrayList;

public class PayingCustomer extends Customer {

    private PaymentMethod paymentMethod;
    private ArrayList<Customer> associateCustomers;

    // Parameterized constructor
    public PayingCustomer(String newName, String newEmail, Address newAddress, PaymentMethod newPaymentMethod) {
        super(newName, newEmail, newAddress);
        paymentMethod = newPaymentMethod;
        this.associateCustomers = new ArrayList<>();
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod newPaymentMethod) {
        paymentMethod = newPaymentMethod;
    }

    public ArrayList<Customer> getAssociateCustomers() {
        return associateCustomers;
    }

    public void setAssociateCustomers(ArrayList<Customer> newAssociateCustomers) {
        associateCustomers = newAssociateCustomers;
    }

    public void addAssociateCustomer(Customer customer) {
        associateCustomers.add(customer);
    }

    public void removeAssociateCustomer(Customer customer) {
        associateCustomers.remove(customer);
    }

    public boolean compareAssociateCustomer(String associateCustomerName) {
        for (Customer customer : associateCustomers) {
            if (customer instanceof AssociateCustomer) {
                AssociateCustomer associateCustomer = (AssociateCustomer) customer;
                if (associateCustomer.getName().equals(associateCustomerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAssociateCustomer() {
        for (Customer customer : associateCustomers) {
            if (customer instanceof AssociateCustomer) {
                return true;
            }
        }
        return false;
    }
    
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
    
    private double calculateSupplementsCost(Customer customer) {
        double cost = 0.0;
        for (int i = 0; i < customer.getSupplement().size(); i++) {
            // Assuming monthly = weeklycost x 4
            cost += (customer.getSupplement().get(i).getCost() * 4);
        }
        return cost;
    }
}
