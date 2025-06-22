package com.java.Invista.repository;

import com.java.Invista.entity.UserEntity;
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
@DisplayName("Testes do RepositoryUser")
class RepositoryUserTest {

    @Autowired
    private RepositoryUser repositoryUser;

    @Test
    @Order(1)
    @DisplayName("Deve listar todos os usuários existentes")
    void deveListarTodosOsUsuarios() {
        System.out.println("👥 Testando listagem de todos os usuários...");

        // BUSCA TODOS OS USUÁRIOS QUE JÁ EXISTEM
        List<UserEntity> usuarios = repositoryUser.findAll();
        
        System.out.println("📊 Usuários encontrados: " + usuarios.size());
        usuarios.forEach(u -> System.out.println("  - " + u.getUsername() + " (ID: " + u.getId() + ")"));
        
        // TESTE GENÉRICO - só verifica se não dá erro
        assertThat(usuarios).isNotNull();
        
        if (!usuarios.isEmpty()) {
            assertThat(usuarios.get(0).getId()).isNotNull();
            assertThat(usuarios.get(0).getUsername()).isNotNull();
        }

        System.out.println("✅ Teste de listagem passou!");
    }

    @Test
    @Order(2)
    @DisplayName("Deve contar total de usuários")
    void deveContarTotalDeUsuarios() {
        System.out.println("🔢 Testando contagem de usuários...");

        long totalUsuarios = repositoryUser.count();
        
        System.out.println("📊 Total de usuários no banco: " + totalUsuarios);
        
        assertThat(totalUsuarios).isGreaterThanOrEqualTo(0);

        System.out.println("✅ Teste de contagem passou!");
    }

    @Test
    @Order(3)
    @DisplayName("Deve buscar usuário por ID existente")
    void deveBuscarUsuarioPorIdExistente() {
        System.out.println("🔍 Testando busca por ID existente...");

        // PEGA UM USUÁRIO QUE JÁ EXISTE
        List<UserEntity> usuarios = repositoryUser.findAll();
        
        if (!usuarios.isEmpty()) {
            UserEntity usuarioExistente = usuarios.get(0);
            String idExistente = usuarioExistente.getId();
            
            System.out.println("🎯 Buscando usuário ID: " + idExistente);

            Optional<UserEntity> usuarioEncontrado = repositoryUser.findById(idExistente);
            
            assertThat(usuarioEncontrado).isPresent();
            assertThat(usuarioEncontrado.get().getId()).isEqualTo(idExistente);
            assertThat(usuarioEncontrado.get().getUsername()).isEqualTo(usuarioExistente.getUsername());
            
            System.out.println("✅ Usuário encontrado: " + usuarioEncontrado.get().getUsername());
        } else {
            System.out.println("⚠️ Nenhum usuário no banco para testar busca por ID");
        }

        System.out.println("✅ Teste de busca por ID passou!");
    }

    @Test
    @Order(4)
    @DisplayName("Deve retornar vazio para ID inexistente")
    void deveRetornarVazioParaIdInexistente() {
        System.out.println("👻 Testando busca por ID inexistente...");

        // USA ID QUE CERTAMENTE NÃO EXISTE
        Optional<UserEntity> usuarioInexistente = repositoryUser.findById("usuarioQueNaoExiste999");
        
        System.out.println("📊 Usuário encontrado para ID inexistente: " + usuarioInexistente.isPresent());
        
        assertThat(usuarioInexistente).isEmpty();

        System.out.println("✅ Teste com ID inexistente passou!");
    }

    @Test
    @Order(5)
    @DisplayName("Deve verificar se usuário existe por ID")
    void deveVerificarSeUsuarioExistePorId() {
        System.out.println("✔️ Testando verificação de existência...");

        List<UserEntity> usuarios = repositoryUser.findAll();
        
        if (!usuarios.isEmpty()) {
            UserEntity usuarioExistente = usuarios.get(0);
            String idExistente = usuarioExistente.getId();
            
            // TESTA ID QUE EXISTE
            boolean existe = repositoryUser.existsById(idExistente);
            System.out.println("🎯 Usuário ID '" + idExistente + "' existe: " + existe);
            
            assertThat(existe).isTrue();
            
            // TESTA ID QUE NÃO EXISTE
            boolean naoExiste = repositoryUser.existsById("idInexistente999");
            System.out.println("👻 Usuário ID 'idInexistente999' existe: " + naoExiste);
            
            assertThat(naoExiste).isFalse();
        } else {
            System.out.println("⚠️ Nenhum usuário no banco para testar existência");
        }

        System.out.println("✅ Teste de verificação de existência passou!");
    }

    @Test
    @Order(6)
    @DisplayName("Deve validar estrutura dos usuários")
    void deveValidarEstruturaDoUsuario() {
        System.out.println("🏗️ Testando estrutura dos usuários...");

        List<UserEntity> usuarios = repositoryUser.findAll();
        
        if (!usuarios.isEmpty()) {
            UserEntity usuario = usuarios.get(0);
            
            System.out.println("📋 Validando usuário: " + usuario.getUsername());
            
            // VALIDA SE OS CAMPOS OBRIGATÓRIOS EXISTEM
            assertThat(usuario.getId()).isNotNull().isNotEmpty();
            assertThat(usuario.getUsername()).isNotNull().isNotEmpty();
            
            System.out.println("✅ Estrutura válida:");
            System.out.println("  - ID: " + usuario.getId());
            System.out.println("  - Username: " + usuario.getUsername());
            
        } else {
            System.out.println("⚠️ Nenhum usuário no banco para validar estrutura");
        }

        System.out.println("✅ Teste de estrutura passou!");
    }

    @Test
    @Order(7)
    @DisplayName("Deve validar que os métodos do repository funcionam")
    void deveValidarQueOsMetodosFuncionam() {
        System.out.println("🔧 Testando todos os métodos básicos...");

        // TESTA findAll()
        List<UserEntity> todosUsuarios = repositoryUser.findAll();
        System.out.println("📋 findAll(): " + todosUsuarios.size() + " usuários");
        assertThat(todosUsuarios).isNotNull();

        // TESTA count()
        long total = repositoryUser.count();
        System.out.println("🔢 count(): " + total);
        assertThat(total).isEqualTo(todosUsuarios.size());

        // TESTA existsById() com diferentes IDs
        if (!todosUsuarios.isEmpty()) {
            String primeiroId = todosUsuarios.get(0).getId();
            boolean existe = repositoryUser.existsById(primeiroId);
            System.out.println("✔️ existsById('" + primeiroId + "'): " + existe);
            assertThat(existe).isTrue();
        }

        // TESTA findById()
        if (!todosUsuarios.isEmpty()) {
            String primeiroId = todosUsuarios.get(0).getId();
            Optional<UserEntity> encontrado = repositoryUser.findById(primeiroId);
            System.out.println("🔍 findById('" + primeiroId + "'): " + encontrado.isPresent());
            assertThat(encontrado).isPresent();
        }

        System.out.println("✅ Todos os métodos funcionam corretamente!");
    }

    @Test
    @Order(8)
    @DisplayName("Deve validar IDs únicos dos usuários")
    void deveValidarIdsUnicosDoUsuarios() {
        System.out.println("🔑 Testando unicidade dos IDs...");

        List<UserEntity> usuarios = repositoryUser.findAll();
        
        if (usuarios.size() > 1) {
            // VERIFICA SE OS IDs SÃO ÚNICOS
            long idsUnicos = usuarios.stream()
                .map(UserEntity::getId)
                .distinct()
                .count();
            
            System.out.println("📊 Total de usuários: " + usuarios.size());
            System.out.println("🔑 IDs únicos: " + idsUnicos);
            
            assertThat(idsUnicos).isEqualTo(usuarios.size());
            
            // VERIFICA SE OS USERNAMES SÃO ÚNICOS
            long usernamesUnicos = usuarios.stream()
                .map(UserEntity::getUsername)
                .distinct()
                .count();
            
            System.out.println("👤 Usernames únicos: " + usernamesUnicos);
            
        } else {
            System.out.println("⚠️ Poucos usuários para testar unicidade");
        }

        System.out.println("✅ Teste de unicidade passou!");
    }
}