package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiResponse {
    private boolean success;
    private String message;
}
