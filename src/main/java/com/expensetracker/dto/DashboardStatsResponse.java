package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsResponse {
    private boolean success;
    private StatsDto stats;
}
