package com.expensetracker.service;

import com.expensetracker.dto.EmiRequest;
import com.expensetracker.dto.EmiResponse;
import com.expensetracker.dto.EmiScheduleRow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        List<EmiScheduleRow> schedule = new ArrayList<>();

        double balance = principal;

        for (int month = 1; month <= months; month++) {

            double interestPart =
                    balance * monthlyRate;

            double principalPart =
                    emi - interestPart;

            balance =
                    balance - principalPart;

            if (balance < 0) {
                balance = 0;
            }

            schedule.add(
                    new EmiScheduleRow(
                            month,
                            round(emi),
                            round(principalPart),
                            round(interestPart),
                            round(balance)
                    )
            );
        }

        return new EmiResponse(
                round(emi),
                round(totalInterest),
                round(totalPayment),
                schedule
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}