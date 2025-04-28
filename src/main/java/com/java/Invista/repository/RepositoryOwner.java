package com.java.Invista.repository;

import com.java.Invista.entity.OwnerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositoryOwner extends JpaRepository<OwnerEntity, Long> {
//    @Query("SELECT i FROM OwnerEntity i WHERE i.user.id = :userid AND  i.ativo = :true")
//    List<OwnerEntity> findByAtivoTrue(@Param("userId") String userId);
}
