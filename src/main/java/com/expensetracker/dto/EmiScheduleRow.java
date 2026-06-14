package com.expensetracker.dto;

public class EmiScheduleRow {

    private int month;
    private double emi;
    private double principal;
    private double interest;
    private double balance;

    public EmiScheduleRow(int month,
                          double emi,
                          double principal,
                          double interest,
                          double balance) {
        this.month = month;
        this.emi = emi;
        this.principal = principal;
        this.interest = interest;
        this.balance = balance;
    }

    public int getMonth() {
        return month;
    }

    public double getEmi() {
        return emi;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterest() {
        return interest;
    }

    public double getBalance() {
        return balance;
    }
}