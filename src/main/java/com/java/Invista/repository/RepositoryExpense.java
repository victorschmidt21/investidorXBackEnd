package com.java.Invista.repository;

import com.java.Invista.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryExpense extends JpaRepository<ExpenseEntity, Long> {
}
