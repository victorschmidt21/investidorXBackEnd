package com.java.Invista.repository;


import com.java.Invista.entity.ImovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryImovel extends JpaRepository<ImovelEntity, Long> {

}
