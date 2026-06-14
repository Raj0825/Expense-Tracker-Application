package com.expensetracker.dto;

public class EmiResponse {

    private double monthlyEmi;
    private double totalInterest;
    private double totalPayment;

    public EmiResponse(double monthlyEmi,
                       double totalInterest,
                       double totalPayment) {
        this.monthlyEmi = monthlyEmi;
        this.totalInterest = totalInterest;
        this.totalPayment = totalPayment;
    }

    public double getMonthlyEmi() {
        return monthlyEmi;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public double getTotalPayment() {
        return totalPayment;
    }
}