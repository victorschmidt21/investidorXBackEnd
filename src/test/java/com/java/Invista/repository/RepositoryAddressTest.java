package com.java.Invista.repository;

import com.java.Invista.entity.AddressEntity;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.StateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // <- ADICIONE ESTA LINHA
@DisplayName("Testes do RepositoryAddress")
class RepositoryAddressTest {

    @Autowired
    private RepositoryAddress repositoryAddress;

    @Autowired
    private RepositoryCity repositoryCity;

    @Autowired
    private RepositoryState repositoryState;

    @Test
    @DisplayName("Deve salvar e buscar um endereço")
    void deveSalvarEBuscarEndereco() {
        // Cria e salva um estado (ajuste os campos conforme sua StateEntity)
        StateEntity state = new StateEntity();
        state.setName("São Paulo");
        state = repositoryState.save(state);

        // Cria e salva uma cidade (ajuste os campos conforme sua CityEntity)
        CityEntity city = new CityEntity();
        city.setNome("São Paulo");
        city.setState(state);
        city = repositoryCity.save(city);

        // Cria e salva um endereço
        AddressEntity address = new AddressEntity();
        address.setStreet("Rua das Flores");
        address.setNumber(123);
        address.setNeighborhood("Centro");
        address.setCep(12345678);
        address.setCity(city);
        address = repositoryAddress.save(address);

        // Busca o endereço salvo
        AddressEntity found = repositoryAddress.findById(address.getId()).orElse(null);

        // ADICIONE ESTE PRINT
        System.out.println("✅ TESTE PASSOU! Endereço salvo: " + found.getStreet());

        // Validações
        assertThat(found).isNotNull();
        assertThat(found.getStreet()).isEqualTo("Rua das Flores");
        assertThat(found.getNeighborhood()).isEqualTo("Centro");
        assertThat(found.getCep()).isEqualTo(12345678);
        assertThat(found.getCity()).isNotNull();
        assertThat(found.getCity().getNome()).isEqualTo("São Paulo");
        assertThat(found.getCity().getState().getName()).isEqualTo("São Paulo");
    }
}