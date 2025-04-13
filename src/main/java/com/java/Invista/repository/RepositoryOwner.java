package com.java.Invista.repository;

import com.java.Invista.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryOwner extends JpaRepository<OwnerEntity, Long> {
}
