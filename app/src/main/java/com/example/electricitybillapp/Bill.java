package com.example.electricitybillapp;

import java.io.Serializable;

public class Bill implements Serializable {
    private String month;
    private int unit;
    private double totalCharges, rebate, finalCost;

    public Bill(String month, int unit, double totalCharges, double rebate, double finalCost) {
        this.month = month;
        this.unit = unit;
        this.totalCharges = totalCharges;
        this.rebate = rebate;
        this.finalCost = finalCost;
    }

    public String getMonth() { return month; }
    public int getUnit() { return unit; }
    public double getTotalCharges() { return totalCharges; }
    public double getRebate() { return rebate; }
    public double getFinalCost() { return finalCost; }

    @Override
    public String toString() {
        return month + " - RM " + String.format("%.2f", finalCost);
    }
}
