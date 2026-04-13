package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateProfileRequest {
    private String name;
    private String currency;
    private Double monthlyBudget;
}
