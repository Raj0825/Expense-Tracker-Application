package com.expensetracker.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Suggestion {
    private String type;
    private String icon;
    private String title;
    private String message;
    private String category;
}
