/*
ICT373 Assignment 1 Question 2
Ong Wei Xing 34444625
13/6/2023
Customer.java
Customer class, getters and setters for the necessary variables
*/

import java.io.Serializable;

public class Address implements Serializable
{
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String postcode;
    
    public Address(String newStreetNumber, String newStreetName, String newSuburb, String newPostcode) 
    {
        streetNumber = newStreetNumber;
        streetName = newStreetName;
        suburb = newSuburb;
        postcode = newPostcode;
    }
    
    public String getStreetNumber() 
    {
        return streetNumber;
    }

    public void setStreetNumber(String newStreetNumber) 
    {
        streetNumber = newStreetNumber;
    }

    public String getStreetName() 
    {
        return streetName;
    }

    public void setStreetName(String newStreetName)
    {
        streetName = newStreetName;
    }

    public String getSuburb() 
    {
        return suburb;
    }

    public void setSuburb(String newSuburb) 
    {
        suburb = newSuburb;
    }

    public String getPostcode() 
    {
        return postcode;
    }

    public void setPostcode(String newPostcode) 
    {
        postcode = newPostcode;
    }
    
    @Override
    public String toString()
    {
        return streetNumber + " " + streetName + " " + suburb + " " + postcode;
    }
}
