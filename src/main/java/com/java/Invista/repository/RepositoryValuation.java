package com.java.Invista.repository;

import com.java.Invista.entity.ValuationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryValuation extends JpaRepository<ValuationEntity, Long> {
}
