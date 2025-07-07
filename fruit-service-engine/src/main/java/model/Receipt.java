package model;

import java.io.Serializable;

public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;
    private final double totalCost;
    private final double amountGiven;
    private final double changeDue;
    private final String cashier;

    //To show the receipt details
    public Receipt(double totalCost, double amountGiven, double changeDue, String cashier) {
        this.totalCost = totalCost;
        this.amountGiven = amountGiven;
        this.changeDue = changeDue;
        this.cashier = cashier;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getAmountGiven() {
        return amountGiven;
    }

    public double getChangeDue() {
        return changeDue;
    }

    public String getCashier() {
        return cashier;
    }

    @Override
    public String toString() {
        return String.format("Receipt:%nTotal Cost: $%.2f%nAmount Given: $%.2f%nChange Due: $%.2f%nCashier: %s", 
                           totalCost, amountGiven, changeDue, cashier);
    }
}