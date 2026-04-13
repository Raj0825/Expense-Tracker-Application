package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // GET /api/expenses?category=food&startDate=2024-01-01&endDate=2024-01-31&page=1&limit=20&search=coffee
    @GetMapping
    public ResponseEntity<PagedExpenseResponse> getExpenses(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(expenseService.getExpenses(
                getUserId(userDetails), category, startDate, endDate, page, limit, search));
    }

    // GET /api/expenses/:id
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpense(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpense(id, getUserId(userDetails)));
    }

    // POST /api/expenses
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ExpenseRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(getUserId(userDetails), req));
    }

    // PUT /api/expenses/:id
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody ExpenseRequest req) {
        return ResponseEntity.ok(expenseService.updateExpense(id, getUserId(userDetails), req));
    }

    // DELETE /api/expenses/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExpense(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(expenseService.deleteExpense(id, getUserId(userDetails)));
    }

    // GET /api/expenses/summary/monthly/:year/:month
    @GetMapping("/summary/monthly/{year}/{month}")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(expenseService.getMonthlySummary(getUserId(userDetails), year, month));
    }

    // GET /api/expenses/summary/yearly/:year
    @GetMapping("/summary/yearly/{year}")
    public ResponseEntity<YearlySummaryResponse> getYearlySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int year) {
        return ResponseEntity.ok(expenseService.getYearlySummary(getUserId(userDetails), year));
    }

    private Long getUserId(UserDetails userDetails) {
        return Long.parseLong(userDetails.getUsername());
    }
}
