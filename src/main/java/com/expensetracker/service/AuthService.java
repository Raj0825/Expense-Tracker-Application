package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.entity.User;
import com.expensetracker.exception.BadRequestException;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("User already exists with this email");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail().toLowerCase())
                .password(passwordEncoder.encode(req.getPassword()))
                .currency(req.getCurrency() != null ? req.getCurrency() : "INR")
                .monthlyBudget(req.getMonthlyBudget() != null ? req.getMonthlyBudget() : 0.0)
                .build();

        user = userRepository.save(user);
        String token = jwtUtils.generateToken(user.getId());

        return AuthResponse.builder()
                .success(true)
                .token(token)
                .user(toUserDto(user))
                .build();
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user.getId());
        return AuthResponse.builder()
                .success(true)
                .token(token)
                .user(toUserDto(user))
                .build();
    }

    public AuthResponse getMe(Long userId) {
        User user = findUser(userId);
        return AuthResponse.builder()
                .success(true)
                .user(toUserDto(user))
                .build();
    }

    @Transactional
    public AuthResponse updateProfile(Long userId, UpdateProfileRequest req) {
        User user = findUser(userId);
        if (req.getName() != null) user.setName(req.getName());
        if (req.getCurrency() != null) user.setCurrency(req.getCurrency());
        if (req.getMonthlyBudget() != null) user.setMonthlyBudget(req.getMonthlyBudget());
        user = userRepository.save(user);
        return AuthResponse.builder().success(true).user(toUserDto(user)).build();
    }

    @Transactional
    public ApiResponse changePassword(Long userId, ChangePasswordRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return new ApiResponse(true, "Password updated successfully");
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .currency(user.getCurrency())
                .monthlyBudget(user.getMonthlyBudget())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
