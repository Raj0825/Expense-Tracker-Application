package com.expensetracker.dto;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MonthlySummary {
    private int year;
    private int month;
    private Double totalAmount;
    private int totalExpenses;
    private Map<String, CategoryStat> byCategory;
    private Map<Integer, Double> dailySpending;
    private List<ExpenseResponse> expenses;
}
