/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 Supplement.java
 Supplement class, getters and setters for the necessary variables
 */

import java.io.Serializable;

public class Supplement implements Serializable {

    private String name;
    private double weeklyCost;

    // Parameterized constructor
    public Supplement(String newName, double newWeeklyCost) {
        name = newName;
        weeklyCost = newWeeklyCost;
    }

    // Getter for supplement name
    public String getName() {
        return name;
    }

    // Setter for supplement name
    public void setName(String newName) {
        name = newName;
    }

    // Getter for supplement cost
    public double getCost() {
        return weeklyCost;
    }

    // Setter for supplement cost
    public void setCost(double newWeeklyCost) {
        weeklyCost = newWeeklyCost;
    }

    // Overriding method for toString()
    @Override
    public String toString() {
        return name;
    }
}
