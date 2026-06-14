package com.expensetracker.service;

import com.expensetracker.dto.EmiRequest;
import com.expensetracker.dto.EmiResponse;
import org.springframework.stereotype.Service;

@Service
public class EmiCalculatorService {

    public EmiResponse calculate(EmiRequest request) {

        double principal = request.getPrincipal();

        double monthlyRate =
                request.getAnnualInterestRate() / 12 / 100;

        int months = request.getTenureMonths();

        double emi =
                (principal * monthlyRate *
                        Math.pow(1 + monthlyRate, months))
                        /
                        (Math.pow(1 + monthlyRate, months) - 1);

        double totalPayment = emi * months;

        double totalInterest = totalPayment - principal;

        return new EmiResponse(
                Math.round(emi * 100.0) / 100.0,
                Math.round(totalInterest * 100.0) / 100.0,
                Math.round(totalPayment * 100.0) / 100.0
        );
    }
}