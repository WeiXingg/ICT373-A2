/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 Customer.java
 Customer class, getters and setters for the necessary variables as well as an 
 arraylist containing subscribed supplements for each customer
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {

    private String name;
    private String email;
    private Address address;
    private ArrayList<Supplement> supplements;

    public Customer() {
        this.name = "";
        this.email = "";
        this.address = new Address();
        this.supplements = new ArrayList<>();
    }
    
    // Parameterized constructor
    public Customer(String newName, String newEmail, Address newAddress) {
        name = newName;
        email = newEmail;
        address = newAddress;
        this.supplements = new ArrayList<>();
    }

    // Getter for customer name
    public String getName() {
        return name;
    }

    // Setter for customer name
    public void setName(String newName) {
        name = newName;
    }

    // Getter for customer email
    public String getEmail() {
        return email;
    }

    // Setter for customer email
    public void setEmail(String newEmail) {
        email = newEmail;
    }

    // Getter for ArrayList containing supplements
    public ArrayList<Supplement> getSupplement() {
        return supplements;
    }

    // Setter for ArrayList containing supplements
    public void setSupplement(ArrayList<Supplement> supplements) {
        this.supplements = supplements;
    }

    // To add a single supplement to customer arraylist of supplements
    public void addSupplement(Supplement supplement) {
        this.supplements.add(supplement);
    }

    // To remove a single supplement from customer arraylist of supplements
    public void removeSupplement(Supplement supplement) {
        this.supplements.remove(supplement);
    }

    // Getter for customer address
    public Address getAddress() {
        return address;
    }

    // Setter for customer address
    public void setAddress(Address newAddress) {
        address = newAddress;
    }

    // Overriding method for toString()
    @Override
    public String toString() {
        return name;
    }
}
