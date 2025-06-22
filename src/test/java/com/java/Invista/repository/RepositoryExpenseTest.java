package com.java.Invista.repository;

import com.java.Invista.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  
@DisplayName("Testes do RepositoryExpense")
class RepositoryExpenseTest {

    @Autowired
    private RepositoryExpense repositoryExpense;

    @Autowired
    private RepositoryImovel repositoryImovel;

    @Autowired
    private RepositoryAddress repositoryAddress;

    @Autowired
    private RepositoryCity repositoryCity;

    @Autowired
    private RepositoryState repositoryState;

    @Autowired
    private RepositoryUser repositoryUser;

    @Test
    @DisplayName("Deve buscar despesas por imovelId")
    void deveBuscarDespesasPorImovelId() {
        // Cria e salva estado, cidade e endereço
        StateEntity state = new StateEntity();
        state.setName("SP");
        state = repositoryState.save(state);

        CityEntity city = new CityEntity();
        city.setNome("São Paulo");
        city.setState(state);
        city = repositoryCity.save(city);

        AddressEntity address = new AddressEntity();
        address.setStreet("Rua X");
        address.setNumber(100);
        address.setNeighborhood("Centro");
        address.setCep(12345678);
        address.setCity(city);
        address = repositoryAddress.save(address);

        // Crie e salve um usuário
        UserEntity user = new UserEntity();
        user.setId("user1");
        user.setUsername("usuario1");
        user = repositoryUser.save(user);

        // Crie e salve o imóvel, associando o usuário
        ImovelEntity imovel = new ImovelEntity();
        imovel.setNome_imovel("Apartamento 1");
        imovel.setAdress(address);
        imovel.setAtivo(true);
        imovel.setDate_Value(LocalDate.now());
        imovel.setValueRegistration(100000.0);
        imovel.setUser(user); // <-- ASSOCIE O USUÁRIO
        imovel = repositoryImovel.save(imovel);

        // Cria e salva despesas
        ExpenseEntity expense1 = new ExpenseEntity();
        expense1.setTitle("Luz");
        expense1.setValue(100.0);
        expense1.setDate(LocalDate.now());
        expense1.setImovel(imovel);
        repositoryExpense.save(expense1);

        ExpenseEntity expense2 = new ExpenseEntity();
        expense2.setTitle("Água");
        expense2.setValue(50.0);
        expense2.setDate(LocalDate.now());
        expense2.setImovel(imovel);
        repositoryExpense.save(expense2);

        // Busca despesas pelo id do imóvel
        List<ExpenseEntity> despesas = repositoryExpense.findByImovelId(imovel.getId_imovel());

        // Validações
        assertThat(despesas).hasSize(2);
        assertThat(despesas).extracting(ExpenseEntity::getTitle)
                .containsExactlyInAnyOrder("Luz", "Água");
    }
}