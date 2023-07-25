/*
ICT373 Assignment 1 Question 2
Ong Wei Xing 34444625
13/6/2023
PaymentMethod.java
PaymentMethod class, getters and setters for the necessary variables
*/

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private String cardType;
    private int accountNo;

    // Parameterized constructor
    public PaymentMethod(String newCardType, int newAccountNo) {
        cardType = newCardType;
        accountNo = newAccountNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String newCardType) {
        cardType = newCardType;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int newAccountNo) {
        accountNo = newAccountNo;
    }
    
    @Override
    public String toString() {
        return cardType + "\n" + accountNo;
    }
}
