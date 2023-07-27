/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 MagazineService.java
 MagazineService class, to handle adding and removing of customers and 
 supplements as well as retrieving arraylist of supplements and customers in 
 magazine.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class MagazineService implements Serializable {

    private ArrayList<Supplement> supplements;
    private ArrayList<Customer> customers;

    // Default constructor
    public MagazineService() {
        this.supplements = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    // Adding a single customer to arraylist of customer
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    // Removing a single customer from arraylist of customer
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    // Adding a single supplement to arraylist of supplement
    public void addSupplement(Supplement supplement) {
        supplements.add(supplement);
    }

    // Removing a single supplement from arraylist of supplement
    public void removeSupplement(Supplement supplement) {
        supplements.remove(supplement);
    }

    // Getter for arraylist of supplement
    public ArrayList<Supplement> getSupplements() {
        return supplements;
    }

    // Getter for arraylist of customer
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
