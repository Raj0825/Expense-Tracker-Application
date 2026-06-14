package com.expensetracker.dto;

import java.util.List;

public class EmiResponse {

    private double monthlyEmi;
    private double totalInterest;
    private double totalPayment;

    private List<EmiScheduleRow> schedule;

    public EmiResponse(double monthlyEmi,
                       double totalInterest,
                       double totalPayment,
                       List<EmiScheduleRow> schedule) {

        this.monthlyEmi = monthlyEmi;
        this.totalInterest = totalInterest;
        this.totalPayment = totalPayment;
        this.schedule = schedule;
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

    public List<EmiScheduleRow> getSchedule() {
        return schedule;
    }
}