/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 AssociateCustomer.java
 AssociateCustomer class, extending Customer class to add associate customer
 */

public class AssociateCustomer extends Customer {
    
    public AssociateCustomer() {
        super();
    }

    // Parameterized constructor
    public AssociateCustomer(String newName, String newEmail, Address newAddress) {
        super(newName, newEmail, newAddress);
    }
}
