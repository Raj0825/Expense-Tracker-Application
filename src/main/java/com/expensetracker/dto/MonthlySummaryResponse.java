package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MonthlySummaryResponse {
    private boolean success;
    private MonthlySummary summary;
}
