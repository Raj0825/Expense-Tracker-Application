package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public PagedExpenseResponse getExpenses(Long userId, String category, String startDate,
                                             String endDate, int page, int limit, String search) {
        String cat = (category != null && !category.equals("all")) ? category : null;
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate + "T00:00:00") : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate + "T23:59:59") : null;
        String searchVal = (search != null && !search.isBlank()) ? search : null;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "date"));
        Page<Expense> result = expenseRepository.findWithFilters(userId, cat, start, end, searchVal, pageable);

        List<ExpenseResponse> expenses = result.getContent().stream()
                .map(this::toResponse).collect(Collectors.toList());

        return PagedExpenseResponse.builder()
                .success(true)
                .count(expenses.size())
                .total(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .currentPage(page)
                .expenses(expenses)
                .build();
    }

    public ExpenseResponse getExpense(Long id, Long userId) {
        Expense expense = expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return toResponse(expense);
    }

    @Transactional
    public ExpenseResponse createExpense(Long userId, ExpenseRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = Expense.builder()
                .user(user)
                .title(req.getTitle())
                .amount(req.getAmount())
                .category(req.getCategory())
                .description(req.getDescription())
                .date(req.getDate())
                .paymentMethod(req.getPaymentMethod() != null ? req.getPaymentMethod() : "cash")
                .tags(req.getTags() != null ? String.join(",", req.getTags()) : null)
                .isRecurring(req.getIsRecurring() != null ? req.getIsRecurring() : false)
                .recurringFrequency(req.getRecurringFrequency())
                .build();

        expense = expenseRepository.save(expense);
        return toResponse(expense);
    }

    @Transactional
    public ExpenseResponse updateExpense(Long id, Long userId, ExpenseRequest req) {
        Expense expense = expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (req.getTitle() != null) expense.setTitle(req.getTitle());
        if (req.getAmount() != null) expense.setAmount(req.getAmount());
        if (req.getCategory() != null) expense.setCategory(req.getCategory());
        if (req.getDescription() != null) expense.setDescription(req.getDescription());
        if (req.getDate() != null) expense.setDate(req.getDate());
        if (req.getPaymentMethod() != null) expense.setPaymentMethod(req.getPaymentMethod());
        if (req.getTags() != null) expense.setTags(String.join(",", req.getTags()));
        if (req.getIsRecurring() != null) expense.setIsRecurring(req.getIsRecurring());
        if (req.getRecurringFrequency() != null) expense.setRecurringFrequency(req.getRecurringFrequency());

        expense = expenseRepository.save(expense);
        return toResponse(expense);
    }

    @Transactional
    public ApiResponse deleteExpense(Long id, Long userId) {
        Expense expense = expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expenseRepository.delete(expense);
        return new ApiResponse(true, "Expense deleted");
    }

    public MonthlySummaryResponse getMonthlySummary(Long userId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, start, end);
        double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();

        Map<String, CategoryStat> byCategory = buildCategoryStats(expenses);
        Map<Integer, Double> dailySpending = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getDayOfMonth(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        return MonthlySummaryResponse.builder()
                .success(true)
                .summary(MonthlySummary.builder()
                        .year(year).month(month)
                        .totalAmount(totalAmount)
                        .totalExpenses(expenses.size())
                        .byCategory(byCategory)
                        .dailySpending(dailySpending)
                        .expenses(expenses.stream().map(this::toResponse).collect(Collectors.toList()))
                        .build())
                .build();
    }

    public YearlySummaryResponse getYearlySummary(Long userId, int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, start, end);

        List<MonthTotal> monthlyTotals = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            final int month = m;
            List<Expense> monthExpenses = expenses.stream()
                    .filter(e -> e.getDate().getMonthValue() == month)
                    .collect(Collectors.toList());
            monthlyTotals.add(MonthTotal.builder()
                    .month(m)
                    .total(monthExpenses.stream().mapToDouble(Expense::getAmount).sum())
                    .count(monthExpenses.size())
                    .build());
        }

        Map<String, Double> byCategory = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));

        return YearlySummaryResponse.builder()
                .success(true)
                .summary(YearlySummary.builder()
                        .year(year)
                        .totalAmount(expenses.stream().mapToDouble(Expense::getAmount).sum())
                        .totalExpenses(expenses.size())
                        .monthlyTotals(monthlyTotals)
                        .byCategory(byCategory)
                        .build())
                .build();
    }

    // ────────────────── helpers ──────────────────

    private Map<String, CategoryStat> buildCategoryStats(List<Expense> expenses) {
        Map<String, CategoryStat> map = new LinkedHashMap<>();
        for (Expense e : expenses) {
            map.compute(e.getCategory(), (k, v) -> {
                if (v == null) return new CategoryStat(e.getAmount(), 1);
                v.setTotal(v.getTotal() + e.getAmount());
                v.setCount(v.getCount() + 1);
                return v;
            });
        }
        return map;
    }

    public ExpenseResponse toResponse(Expense e) {
        List<String> tags = (e.getTags() != null && !e.getTags().isBlank())
                ? Arrays.asList(e.getTags().split(","))
                : Collections.emptyList();

        return ExpenseResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .amount(e.getAmount())
                .category(e.getCategory())
                .description(e.getDescription())
                .date(e.getDate())
                .paymentMethod(e.getPaymentMethod())
                .tags(tags)
                .isRecurring(e.getIsRecurring())
                .recurringFrequency(e.getRecurringFrequency())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
