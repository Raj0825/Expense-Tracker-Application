package com.expensetracker.dto;
import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PagedExpenseResponse {
    private boolean success;
    private int count;
    private long total;
    private int totalPages;
    private int currentPage;
    private List<ExpenseResponse> expenses;
}
