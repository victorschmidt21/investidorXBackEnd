package com.java.Invista.repository;

import com.java.Invista.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryCity extends JpaRepository<CityEntity, Long> {
    List<CityEntity> findByStateId(Long stateId);
}
