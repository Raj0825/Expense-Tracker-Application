package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryStat {
    private Double total;
    private int count;
}
