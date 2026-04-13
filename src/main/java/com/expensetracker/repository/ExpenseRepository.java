package com.expensetracker.repository;

import com.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    // Paginated with filters
    @Query("""
        SELECT e FROM Expense e
        WHERE e.user.id = :userId
          AND (:category IS NULL OR e.category = :category)
          AND (:startDate IS NULL OR e.date >= :startDate)
          AND (:endDate IS NULL OR e.date <= :endDate)
          AND (:search IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<Expense> findWithFilters(
            @Param("userId") Long userId,
            @Param("category") String category,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("search") String search,
            Pageable pageable
    );

    // All expenses in a date range for a user
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // All expenses for a user (for dashboard stats)
    List<Expense> findByUserId(Long userId);

    // Recent N expenses
    List<Expense> findTop5ByUserIdOrderByDateDesc(Long userId);
}
