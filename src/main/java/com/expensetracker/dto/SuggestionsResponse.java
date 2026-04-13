package com.expensetracker.dto;
import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SuggestionsResponse {
    private boolean success;
    private List<Suggestion> suggestions;
}
