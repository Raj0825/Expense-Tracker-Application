package com.expensetracker.dto;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class YearlySummary {
    private int year;
    private Double totalAmount;
    private int totalExpenses;
    private List<MonthTotal> monthlyTotals;
    private Map<String, Double> byCategory;
}
