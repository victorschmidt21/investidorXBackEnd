package com.java.Invista.repository;

import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.entity.RenevueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryRenevue extends JpaRepository<RenevueEntity, Long> {
    @Query("SELECT i FROM RenevueEntity i WHERE i.imovel.id_imovel = :imovelId ")
    List<RenevueEntity> findByImovelId(@Param("imovelId") Long imovelId);
}
