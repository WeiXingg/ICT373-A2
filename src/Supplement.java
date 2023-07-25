/*
 ICT373 Assignment 1 Question 2
 Ong Wei Xing 34444625
 13/6/2023
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

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public double getCost() {
        return weeklyCost;
    }

    public void setCost(double newWeeklyCost) {
        weeklyCost = newWeeklyCost;
    }

    @Override
    public String toString() {
        return name;
    }
}
