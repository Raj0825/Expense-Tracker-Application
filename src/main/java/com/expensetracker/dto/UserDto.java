package com.expensetracker.dto;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String currency;
    private Double monthlyBudget;
    private LocalDateTime createdAt;
}
