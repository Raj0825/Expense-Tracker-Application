package com.expensetracker.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseResponse {
    private Long id;
    private String title;
    private Double amount;
    private String category;
    private String description;
    private LocalDateTime date;
    private String paymentMethod;
    private List<String> tags;
    private Boolean isRecurring;
    private String recurringFrequency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
