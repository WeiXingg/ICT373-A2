/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 Address.java
 Address class, getters and setters for the necessary variables
 */

import java.io.Serializable;

public class Address implements Serializable {

    private String streetNumber;
    private String streetName;
    private String suburb;
    private String postcode;
    
    public Address() {
        this.streetNumber = "";
        this.streetName = "";
        this.suburb = "";
        this.postcode = "";
    }

    // Parameterized constructor
    public Address(String newStreetNumber, String newStreetName, String newSuburb, String newPostcode) {
        streetNumber = newStreetNumber;
        streetName = newStreetName;
        suburb = newSuburb;
        postcode = newPostcode;
    }

    // Getter for street number
    public String getStreetNumber() {
        return streetNumber;
    }

    // Setter for street number
    public void setStreetNumber(String newStreetNumber) {
        streetNumber = newStreetNumber;
    }

    // Getter for street name
    public String getStreetName() {
        return streetName;
    }

    // Setter for street name
    public void setStreetName(String newStreetName) {
        streetName = newStreetName;
    }

    // Getter for suburb
    public String getSuburb() {
        return suburb;
    }

    // Setter for suburb
    public void setSuburb(String newSuburb) {
        suburb = newSuburb;
    }

    // Getter for postcode
    public String getPostcode() {
        return postcode;
    }

    // Setter for postcode
    public void setPostcode(String newPostcode) {
        postcode = newPostcode;
    }

    // Overriding method for toString()
    @Override
    public String toString() {
        return streetNumber + " " + streetName + " " + suburb + " " + postcode;
    }
}
