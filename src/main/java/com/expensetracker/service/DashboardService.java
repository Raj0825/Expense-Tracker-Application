package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseService expenseService;

    public DashboardStatsResponse getDashboardStats(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        LocalDateTime startOfLastMonth = startOfMonth.minusMonths(1);
        LocalDateTime endOfLastMonth = startOfMonth.minusSeconds(1);

        List<Expense> thisMonthExpenses = expenseRepository.findByUserIdAndDateBetween(userId, startOfMonth, endOfMonth);
        List<Expense> lastMonthExpenses = expenseRepository.findByUserIdAndDateBetween(userId, startOfLastMonth, endOfLastMonth);
        List<Expense> allExpenses = expenseRepository.findByUserId(userId);

        double thisMonthTotal = sum(thisMonthExpenses);
        double lastMonthTotal = sum(lastMonthExpenses);
        double totalAllTime = sum(allExpenses);

        // Category breakdown this month
        Map<String, CategoryStat> categoryBreakdown = new LinkedHashMap<>();
        for (Expense e : thisMonthExpenses) {
            categoryBreakdown.compute(e.getCategory(), (k, v) -> {
                if (v == null) return new CategoryStat(e.getAmount(), 1);
                v.setTotal(v.getTotal() + e.getAmount());
                v.setCount(v.getCount() + 1);
                return v;
            });
        }

        // Last 7 days
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DaySpending> last7Days = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime dayEnd = dayStart.withHour(23).withMinute(59).withSecond(59);
            List<Expense> dayExpenses = allExpenses.stream()
                    .filter(e -> !e.getDate().isBefore(dayStart) && !e.getDate().isAfter(dayEnd))
                    .collect(Collectors.toList());
            last7Days.add(DaySpending.builder()
                    .date(dayStart.format(fmt))
                    .total(sum(dayExpenses))
                    .count(dayExpenses.size())
                    .build());
        }

        // Recent expenses
        List<ExpenseResponse> recentExpenses = expenseRepository
                .findTop5ByUserIdOrderByDateDesc(userId)
                .stream().map(expenseService::toResponse).collect(Collectors.toList());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        double budget = user.getMonthlyBudget() != null ? user.getMonthlyBudget() : 0.0;
        double budgetUsed = budget > 0 ? Math.min((thisMonthTotal / budget) * 100, 100) : 0;
        double budgetRemaining = Math.max(budget - thisMonthTotal, 0);
        double percentChange = lastMonthTotal > 0
                ? Double.parseDouble(String.format("%.1f", (thisMonthTotal - lastMonthTotal) / lastMonthTotal * 100))
                : 0;

        return DashboardStatsResponse.builder()
                .success(true)
                .stats(StatsDto.builder()
                        .thisMonthTotal(thisMonthTotal)
                        .lastMonthTotal(lastMonthTotal)
                        .totalAllTime(totalAllTime)
                        .thisMonthCount(thisMonthExpenses.size())
                        .monthlyBudget(budget)
                        .budgetUsed(budgetUsed)
                        .budgetRemaining(budgetRemaining)
                        .percentChange(percentChange)
                        .categoryBreakdown(categoryBreakdown)
                        .last7Days(last7Days)
                        .recentExpenses(recentExpenses)
                        .build())
                .build();
    }

    public SuggestionsResponse getSuggestions(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime last3MonthsStart = startOfMonth.minusMonths(3);

        List<Expense> thisMonthExpenses = expenseRepository.findByUserIdAndDateBetween(userId, startOfMonth, now);
        List<Expense> last3MonthsExpenses = expenseRepository.findByUserIdAndDateBetween(userId, last3MonthsStart, startOfMonth);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Suggestion> suggestions = new ArrayList<>();
        double thisMonthTotal = sum(thisMonthExpenses);
        double avgLast3Months = sum(last3MonthsExpenses) / 3.0;

        double budget = user.getMonthlyBudget() != null ? user.getMonthlyBudget() : 0.0;

        // Budget warning
        if (budget > 0 && thisMonthTotal > budget * 0.8) {
            double pct = (thisMonthTotal / budget) * 100;
            suggestions.add(Suggestion.builder()
                    .type("warning").icon("⚠️")
                    .title("Approaching Budget Limit")
                    .message(String.format("You've used %.0f%% of your monthly budget. Consider reducing discretionary spending.", pct))
                    .category("budget")
                    .build());
        }

        // Category spike analysis
        Map<String, Double> thisCat = categoryTotals(thisMonthExpenses);
        Map<String, Double> last3Cat = categoryTotals(last3MonthsExpenses);

        thisCat.forEach((category, thisAmt) -> {
            double avgPrev = last3Cat.getOrDefault(category, 0.0) / 3.0;
            if (avgPrev > 0 && thisAmt > avgPrev * 1.5) {
                double pct = (thisAmt / avgPrev - 1) * 100;
                suggestions.add(Suggestion.builder()
                        .type("alert").icon("📈")
                        .title("High " + capitalize(category) + " Spending")
                        .message(String.format("Your %s spending is %.0f%% higher than your 3-month average. Try to cut back.", category, pct))
                        .category(category)
                        .build());
            }
        });

        // Above average
        if (avgLast3Months > 0 && thisMonthTotal > avgLast3Months * 1.2) {
            double pct = (thisMonthTotal / avgLast3Months - 1) * 100;
            suggestions.add(Suggestion.builder()
                    .type("tip").icon("💡")
                    .title("Spending Above Average")
                    .message(String.format("You're spending %.0f%% more than your 3-month average this month.", pct))
                    .category("general")
                    .build());
        }

        // Positive reinforcement
        if (avgLast3Months > 0 && thisMonthTotal < avgLast3Months * 0.8) {
            double pct = (1 - thisMonthTotal / avgLast3Months) * 100;
            suggestions.add(Suggestion.builder()
                    .type("success").icon("🎉")
                    .title("Great Savings!")
                    .message(String.format("You're spending %.0f%% less than your average. Keep it up!", pct))
                    .category("general")
                    .build());
        }

        if (suggestions.isEmpty()) {
            suggestions.add(Suggestion.builder()
                    .type("info").icon("✅")
                    .title("Spending on Track")
                    .message("Your spending is within normal ranges. Keep tracking your expenses to stay on budget!")
                    .category("general")
                    .build());
        }

        return SuggestionsResponse.builder().success(true).suggestions(suggestions).build();
    }

    private double sum(List<Expense> expenses) {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    private Map<String, Double> categoryTotals(List<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
