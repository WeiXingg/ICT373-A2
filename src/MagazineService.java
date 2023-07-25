/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 13/6/2023
 MagazineService.java
 MagazineService class, implementation for the emails
 */

import java.io.Serializable;
import java.util.ArrayList;

public class MagazineService implements Serializable {

    private ArrayList<Supplement> supplements;
    private ArrayList<Customer> customers;

    public MagazineService() {
        this.supplements = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public void addSupplement(Supplement supplement) {
        supplements.add(supplement);
    }

    public void removeSupplement(Supplement supplement) {
        supplements.remove(supplement);
    }

    public ArrayList<Supplement> getSupplements() {
        return supplements;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
