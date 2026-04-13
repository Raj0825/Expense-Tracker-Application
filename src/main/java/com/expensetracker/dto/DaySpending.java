package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DaySpending {
    private String date;
    private Double total;
    private int count;
}
