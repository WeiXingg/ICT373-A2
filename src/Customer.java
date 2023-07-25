/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 13/6/2023
 Customer.java
 Customer class, getters and setters for the necessary variables
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {

    private String name;
    private String email;
    private Address address;
    private ArrayList<Supplement> supplements;
    
    // Parameterized constructor
    public Customer(String newName, String newEmail, Address newAddress) {
        name = newName;
        email = newEmail;
        address = newAddress;
        this.supplements = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public ArrayList<Supplement> getSupplement() {
        return supplements;
    }

    public void setSupplement(ArrayList<Supplement> supplements) {
        this.supplements = supplements;
    }

    public void addSupplement(Supplement supplement) {
        this.supplements.add(supplement);
    }

    public void removeSupplement(Supplement supplement) {
        this.supplements.remove(supplement);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address newAddress) {
        address = newAddress;
    }

    @Override
    public String toString() {
        return name;
    }
}
