package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MonthTotal {
    private int month;
    private Double total;
    private int count;
}
