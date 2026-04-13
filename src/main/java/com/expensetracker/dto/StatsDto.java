package com.expensetracker.dto;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StatsDto {
    private Double thisMonthTotal;
    private Double lastMonthTotal;
    private Double totalAllTime;
    private int thisMonthCount;
    private Double monthlyBudget;
    private Double budgetUsed;
    private Double budgetRemaining;
    private Double percentChange;
    private Map<String, CategoryStat> categoryBreakdown;
    private List<DaySpending> last7Days;
    private List<ExpenseResponse> recentExpenses;
}
