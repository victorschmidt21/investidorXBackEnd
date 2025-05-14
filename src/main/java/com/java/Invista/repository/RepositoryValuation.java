package com.java.Invista.repository;

import com.java.Invista.entity.ValuationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryValuation extends JpaRepository<ValuationEntity, Long> {
    @Query("SELECT i FROM ValuationEntity i where i.imovel.id_imovel = :imovelId order by i.date desc limit 1 ")
    public ValuationEntity findByImovelId(Long imovelId);
}
