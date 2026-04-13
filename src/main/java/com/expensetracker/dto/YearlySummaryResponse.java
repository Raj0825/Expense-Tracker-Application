package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class YearlySummaryResponse {
    private boolean success;
    private YearlySummary summary;
}
