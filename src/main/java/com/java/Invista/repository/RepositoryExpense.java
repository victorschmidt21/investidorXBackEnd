package com.java.Invista.repository;

import com.java.Invista.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RepositoryExpense extends JpaRepository<ExpenseEntity, Long> {
    @Query("SELECT i FROM ExpenseEntity i WHERE i.imovel.id_imovel = :imovelId ")
    List<ExpenseEntity> findByImovelId(@Param("imovelId") Long imovelId);
}
