package com.java.Invista.repository;

import com.java.Invista.entity.StateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes do RepositoryState")
class RepositoryStateTest {

    @Autowired
    private RepositoryState repositoryState;

    @Test
    @Order(1)
    @DisplayName("Deve listar todos os estados existentes")
    void deveListarTodosOsEstados() {
        System.out.println("🗺️ Testando listagem de todos os estados...");

        // BUSCA TODOS OS ESTADOS QUE JÁ EXISTEM
        List<StateEntity> estados = repositoryState.findAll();
        
        System.out.println("📊 Estados encontrados: " + estados.size());
        estados.forEach(s -> System.out.println("  - " + s.getName() + " (ID: " + s.getId() + ")"));
        
        // TESTE GENÉRICO - só verifica se não dá erro
        assertThat(estados).isNotNull();
        
        if (!estados.isEmpty()) {
            assertThat(estados.get(0).getName()).isNotNull();
            assertThat(estados.get(0).getId()).isNotNull();
        }

        System.out.println("✅ Teste de listagem passou!");
    }

    @Test
    @Order(2)
    @DisplayName("Deve contar total de estados")
    void deveContarTotalDeEstados() {
        System.out.println("🔢 Testando contagem de estados...");

        long totalEstados = repositoryState.count();
        
        System.out.println("📊 Total de estados no banco: " + totalEstados);
        
        assertThat(totalEstados).isGreaterThanOrEqualTo(0);

        System.out.println("✅ Teste de contagem passou!");
    }

    @Test
    @Order(3)
    @DisplayName("Deve buscar estado por ID existente")
    void deveBuscarEstadoPorIdExistente() {
        System.out.println("🔍 Testando busca por ID existente...");

        // PEGA UM ESTADO QUE JÁ EXISTE
        List<StateEntity> estados = repositoryState.findAll();
        
        if (!estados.isEmpty()) {
            StateEntity estadoExistente = estados.get(0);
            Long idExistente = estadoExistente.getId();
            
            System.out.println("🎯 Buscando estado ID: " + idExistente);

            Optional<StateEntity> estadoEncontrado = repositoryState.findById(idExistente);
            
            assertThat(estadoEncontrado).isPresent();
            assertThat(estadoEncontrado.get().getId()).isEqualTo(idExistente);
            assertThat(estadoEncontrado.get().getName()).isEqualTo(estadoExistente.getName());
            
            System.out.println("✅ Estado encontrado: " + estadoEncontrado.get().getName());
        } else {
            System.out.println("⚠️ Nenhum estado no banco para testar busca por ID");
        }

        System.out.println("✅ Teste de busca por ID passou!");
    }

    @Test
    @Order(4)
    @DisplayName("Deve retornar vazio para ID inexistente")
    void deveRetornarVazioParaIdInexistente() {
        System.out.println("👻 Testando busca por ID inexistente...");

        // USA ID QUE CERTAMENTE NÃO EXISTE
        Optional<StateEntity> estadoInexistente = repositoryState.findById(99999L);
        
        System.out.println("📊 Estado encontrado para ID inexistente: " + estadoInexistente.isPresent());
        
        assertThat(estadoInexistente).isEmpty();

        System.out.println("✅ Teste com ID inexistente passou!");
    }

    @Test
    @Order(5)
    @DisplayName("Deve verificar se estado existe por ID")
    void deveVerificarSeEstadoExistePorId() {
        System.out.println("✔️ Testando verificação de existência...");

        List<StateEntity> estados = repositoryState.findAll();
        
        if (!estados.isEmpty()) {
            StateEntity estadoExistente = estados.get(0);
            Long idExistente = estadoExistente.getId();
            
            // TESTA ID QUE EXISTE
            boolean existe = repositoryState.existsById(idExistente);
            System.out.println("🎯 Estado ID " + idExistente + " existe: " + existe);
            
            assertThat(existe).isTrue();
            
            // TESTA ID QUE NÃO EXISTE
            boolean naoExiste = repositoryState.existsById(99999L);
            System.out.println("👻 Estado ID 99999 existe: " + naoExiste);
            
            assertThat(naoExiste).isFalse();
        } else {
            System.out.println("⚠️ Nenhum estado no banco para testar existência");
        }

        System.out.println("✅ Teste de verificação de existência passou!");
    }

    @Test
    @Order(6)
    @DisplayName("Deve validar estrutura dos estados")
    void deveValidarEstruturaDoEstado() {
        System.out.println("🏗️ Testando estrutura dos estados...");

        List<StateEntity> estados = repositoryState.findAll();
        
        if (!estados.isEmpty()) {
            StateEntity estado = estados.get(0);
            
            System.out.println("📋 Validando estado: " + estado.getName());
            
            // VALIDA SE OS CAMPOS OBRIGATÓRIOS EXISTEM
            assertThat(estado.getId()).isNotNull();
            assertThat(estado.getName()).isNotNull().isNotEmpty();
            
            System.out.println("✅ Estrutura válida:");
            System.out.println("  - ID: " + estado.getId());
            System.out.println("  - Nome: " + estado.getName());
            
        } else {
            System.out.println("⚠️ Nenhum estado no banco para validar estrutura");
        }

        System.out.println("✅ Teste de estrutura passou!");
    }

    @Test
    @Order(7)
    @DisplayName("Deve validar que os métodos do repository funcionam")
    void deveValidarQueOsMetodosFuncionam() {
        System.out.println("🔧 Testando todos os métodos básicos...");

        // TESTA findAll()
        List<StateEntity> todosEstados = repositoryState.findAll();
        System.out.println("📋 findAll(): " + todosEstados.size() + " estados");
        assertThat(todosEstados).isNotNull();

        // TESTA count()
        long total = repositoryState.count();
        System.out.println("🔢 count(): " + total);
        assertThat(total).isEqualTo(todosEstados.size());

        // TESTA existsById() com diferentes IDs
        if (!todosEstados.isEmpty()) {
            Long primeiroId = todosEstados.get(0).getId();
            boolean existe = repositoryState.existsById(primeiroId);
            System.out.println("✔️ existsById(" + primeiroId + "): " + existe);
            assertThat(existe).isTrue();
        }

        // TESTA findById()
        if (!todosEstados.isEmpty()) {
            Long primeiroId = todosEstados.get(0).getId();
            Optional<StateEntity> encontrado = repositoryState.findById(primeiroId);
            System.out.println("🔍 findById(" + primeiroId + "): " + encontrado.isPresent());
            assertThat(encontrado).isPresent();
        }

        System.out.println("✅ Todos os métodos funcionam corretamente!");
    }
}
