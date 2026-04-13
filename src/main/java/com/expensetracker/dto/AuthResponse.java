package com.expensetracker.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private boolean success;
    private String token;
    private UserDto user;
    private String message;
}
