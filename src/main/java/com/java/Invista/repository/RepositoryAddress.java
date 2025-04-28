package com.java.Invista.repository;

import com.java.Invista.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryAddress extends JpaRepository<AddressEntity, Long> {
}
