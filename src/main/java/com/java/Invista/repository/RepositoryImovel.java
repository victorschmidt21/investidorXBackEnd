package com.java.Invista.repository;

import com.java.Invista.entity.ImovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryImovel extends JpaRepository<ImovelEntity, Long> {
    @Query("SELECT i FROM ImovelEntity i WHERE i.user.id = :userid")
    List<ImovelEntity> findByUser(@Param("userid") String user);

    @Query("SELECT SUM(i.valueRegistration) FROM ImovelEntity i WHERE i.ativo = true")
    Number somarValoresTotaisImoveisAtivos();
}
