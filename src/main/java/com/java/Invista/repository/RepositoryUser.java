package com.java.Invista.repository;

import com.java.Invista.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryUser extends JpaRepository<UserEntity, Long> {
}
