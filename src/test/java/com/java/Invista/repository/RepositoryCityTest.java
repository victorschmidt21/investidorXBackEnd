package com.java.Invista.repository;

import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.StateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  
@DisplayName("Testes do RepositoryCity")
class RepositoryCityTest {

    @Autowired
    private RepositoryCity repositoryCity;

    @Autowired
    private RepositoryState repositoryState;

    @Test
    @DisplayName("Deve buscar cidades por stateId")
    void deveBuscarCidadesPorStateId() {
        // Cria e salva um estado
        StateEntity state = new StateEntity();
        state.setName("São Paulo");
        state = repositoryState.save(state);

        // Cria e salva cidades
        CityEntity city1 = new CityEntity();
        city1.setNome("São Paulo");
        city1.setState(state);
        repositoryCity.save(city1);

        CityEntity city2 = new CityEntity();
        city2.setNome("Campinas");
        city2.setState(state);
        repositoryCity.save(city2);

        // Busca cidades pelo stateId
        List<CityEntity> cidades = repositoryCity.findByStateId(state.getId());

        // Validações
        assertThat(cidades).hasSize(2);
        assertThat(cidades).extracting(CityEntity::getNome)
                .containsExactlyInAnyOrder("São Paulo", "Campinas");
    }
}