package com.java.Invista.repository;

import com.java.Invista.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // <- MUDE PARA NONE
@DisplayName("Testes do RepositoryOwner")
class RepositoryOwnerTest {

    @Autowired
    private RepositoryOwner repositoryOwner;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private RepositoryState repositoryState;

    @Autowired
    private RepositoryCity repositoryCity;

    @Autowired
    private RepositoryAddress repositoryAddress;

    @Autowired
    private TestEntityManager entityManager; // <- ADICIONE ESTE

    private UserEntity user1;
    private UserEntity user2;
    private AddressEntity address1;
    private AddressEntity address2;
    private AddressEntity address3;

    @BeforeEach
    void setUp() {
        // LIMPA DADOS ANTES DE CADA TESTE
        limparDados();
        
        System.out.println("🏗️ Preparando dados para teste...");

        // Cria usuários
        user1 = new UserEntity();
        user1.setId("user123");
        user1.setUsername("João Silva");
        user1 = repositoryUser.save(user1);

        user2 = new UserEntity();
        user2.setId("user456");
        user2.setUsername("Maria Santos");
        user2 = repositoryUser.save(user2);

        // Cria estado
        StateEntity state = new StateEntity();
        state.setName("São Paulo");
        state = repositoryState.save(state);

        // Cria cidade
        CityEntity city = new CityEntity();
        city.setNome("São Paulo");
        city.setState(state);
        city = repositoryCity.save(city);

        // Cria endereços
        address1 = criarEndereco("Rua das Flores", 123, city);
        address2 = criarEndereco("Av. Paulista", 456, city);
        address3 = criarEndereco("Rua Augusta", 789, city);

        System.out.println("✅ Dados preparados com sucesso!");
    }

    @Test
    @DisplayName("Deve buscar proprietários ativos por usuário")
    void deveBuscarProprietariosAtivosPorUsuario() {
        System.out.println("🔍 Testando busca de proprietários ativos por usuário...");

        // Cria proprietários ativos para user1
        criarProprietario("João Proprietário", "123.456.789-00", true, user1, address1);
        criarProprietario("Pedro Silva", "987.654.321-00", true, user1, address2);
        
        // Cria proprietário inativo para user1 (não deve aparecer)
        criarProprietario("Carlos Inativo", "111.222.333-44", false, user1, address3);
        
        // Cria proprietário ativo para user2
        criarProprietario("Maria Proprietária", "555.666.777-88", true, user2, address1);

        // Testa busca por user1 (deve retornar apenas os ativos)
        List<OwnerEntity> proprietariosUser1 = repositoryOwner.findByAtivoTrue("user123");
        
        System.out.println("📊 Proprietários ativos encontrados para user1: " + proprietariosUser1.size());
        proprietariosUser1.forEach(p -> System.out.println("  - " + p.getName() + " (Ativo: " + p.getAtivo() + ")"));
        
        assertThat(proprietariosUser1).hasSize(2);
        assertThat(proprietariosUser1).extracting(OwnerEntity::getName)
                .containsExactlyInAnyOrder("João Proprietário", "Pedro Silva");
        assertThat(proprietariosUser1).allMatch(OwnerEntity::getAtivo);

        // Testa busca por user2
        List<OwnerEntity> proprietariosUser2 = repositoryOwner.findByAtivoTrue("user456");
        
        System.out.println("📊 Proprietários ativos encontrados para user2: " + proprietariosUser2.size());
        
        assertThat(proprietariosUser2).hasSize(1);
        assertThat(proprietariosUser2.get(0).getName()).isEqualTo("Maria Proprietária");
        assertThat(proprietariosUser2.get(0).getAtivo()).isTrue();

        System.out.println("✅ Teste de busca por usuário passou!");
    }

    @Test
    @DisplayName("Deve retornar lista vazia para usuário sem proprietários ativos")
    void deveRetornarListaVaziaParaUsuarioSemProprietariosAtivos() {
        System.out.println("📝 Testando usuário sem proprietários ativos...");

        // Cria apenas proprietários inativos para user1
        criarProprietario("João Inativo", "123.456.789-00", false, user1, address1);
        criarProprietario("Pedro Inativo", "987.654.321-00", false, user1, address2);

        List<OwnerEntity> proprietarios = repositoryOwner.findByAtivoTrue("user123");
        
        System.out.println("📊 Proprietários ativos encontrados: " + proprietarios.size());
        
        assertThat(proprietarios).isEmpty();

        System.out.println("✅ Teste de usuário sem proprietários ativos passou!");
    }

    @Test
    @DisplayName("Deve retornar lista vazia para usuário inexistente")
    void deveRetornarListaVaziaParaUsuarioInexistente() {
        System.out.println("👻 Testando usuário inexistente...");

        // Cria proprietários para user1, mas busca por usuário inexistente
        criarProprietario("João Proprietário", "123.456.789-00", true, user1, address1);

        List<OwnerEntity> proprietarios = repositoryOwner.findByAtivoTrue("userInexistente");
        
        System.out.println("📊 Proprietários encontrados para usuário inexistente: " + proprietarios.size());
        
        assertThat(proprietarios).isEmpty();

        System.out.println("✅ Teste de usuário inexistente passou!");
    }

    @Test
    @DisplayName("Deve verificar se existe proprietário por endereço")
    void deveVerificarSeExisteProprietarioPorEndereco() {
        System.out.println("🏠 Testando verificação de proprietário por endereço...");

        // Cria proprietário para address1
        criarProprietario("João Proprietário", "123.456.789-00", true, user1, address1);

        // Testa endereço que tem proprietário
        boolean existeComProprietario = repositoryOwner.existsByAddressId(address1.getId());
        System.out.println("📍 Endereço " + address1.getId() + " tem proprietário: " + existeComProprietario);
        
        assertThat(existeComProprietario).isTrue();

        // Testa endereço que NÃO tem proprietário
        boolean existeSemProprietario = repositoryOwner.existsByAddressId(address2.getId());
        System.out.println("📍 Endereço " + address2.getId() + " tem proprietário: " + existeSemProprietario);
        
        assertThat(existeSemProprietario).isFalse();

        System.out.println("✅ Teste de verificação por endereço passou!");
    }

    @Test
    @DisplayName("Deve verificar existência mesmo com proprietário inativo")
    void deveVerificarExistenciaMesmoComProprietarioInativo() {
        System.out.println("😴 Testando verificação com proprietário inativo...");

        // Cria proprietário INATIVO para address1
        criarProprietario("João Inativo", "123.456.789-00", false, user1, address1);

        // O método existsByAddressId deve retornar true mesmo se o proprietário estiver inativo
        boolean existe = repositoryOwner.existsByAddressId(address1.getId());
        System.out.println("📍 Endereço com proprietário inativo existe: " + existe);
        
        assertThat(existe).isTrue();

        System.out.println("✅ Teste com proprietário inativo passou!");
    }

    @Test
    @DisplayName("Deve verificar com ID de endereço inexistente")
    void deveVerificarComIdEnderecoInexistente() {
        System.out.println("🔍 Testando ID de endereço inexistente...");

        // Testa com ID que não existe
        boolean existe = repositoryOwner.existsByAddressId(99999L);
        System.out.println("📍 Endereço inexistente (ID: 99999) tem proprietário: " + existe);
        
        assertThat(existe).isFalse();

        System.out.println("✅ Teste com ID inexistente passou!");
    }

    // MÉTODOS AUXILIARES
    private AddressEntity criarEndereco(String rua, int numero, CityEntity city) {
        AddressEntity address = new AddressEntity();
        address.setStreet(rua);
        address.setNumber(numero);
        address.setNeighborhood("Centro");
        address.setCep(12345678);
        address.setCity(city);
        return repositoryAddress.save(address);
    }

    private OwnerEntity criarProprietario(String nome, String cpf, boolean ativo, UserEntity user, AddressEntity address) {
        OwnerEntity owner = new OwnerEntity();
        owner.setName(nome);
        owner.setCpf_cnpj(cpf);
        owner.setEmail(nome.toLowerCase().replace(" ", ".") + "@teste.com");
        owner.setPhone("(11) 99999-9999");
        owner.setAddress(address);
        owner.setUser(user);
        owner.setAtivo(ativo);
        
        return repositoryOwner.save(owner);
    }

    // MÉTODO PARA LIMPAR DADOS
    private void limparDados() {
        System.out.println("🧹 Limpando dados anteriores...");
        
        entityManager.getEntityManager().createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM owner").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM address").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM city").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM states").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM user").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        
        entityManager.flush();
        entityManager.clear();
    }
}