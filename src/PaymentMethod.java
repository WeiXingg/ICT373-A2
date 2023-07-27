/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 PaymentMethod.java
 PaymentMethod class, getters and setters for the necessary variables
 */

import java.io.Serializable;

public class PaymentMethod implements Serializable {

    private String cardType;
    private int accountNo;
    
    public PaymentMethod() {
        this.cardType = "";
        this.accountNo = 0;
    }

    // Parameterized constructor
    public PaymentMethod(String newCardType, int newAccountNo) {
        cardType = newCardType;
        accountNo = newAccountNo;
    }
    
    // Getter for card type
    public String getCardType() {
        return cardType;
    }

    // Setter for card type
    public void setCardType(String newCardType) {
        cardType = newCardType;
    }

    // Getter for account number
    public int getAccountNo() {
        return accountNo;
    }

    // Setter for account number
    public void setAccountNo(int newAccountNo) {
        accountNo = newAccountNo;
    }

    // Overriding method for toString()
    @Override
    public String toString() {
        return cardType + "\n" + accountNo;
    }
}
