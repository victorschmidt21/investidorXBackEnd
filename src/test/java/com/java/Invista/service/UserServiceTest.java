package com.java.Invista.service;

import com.java.Invista.entity.UserEntity;
import com.java.Invista.repository.RepositoryUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UserService")
class UserServiceTest {

    @Mock
    private RepositoryUser repositoryUser;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA USUÁRIO
        userEntity = new UserEntity();
        userEntity.setId("user123");
        userEntity.setUsername("João Silva");

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar usuário")
    void deveCriarUsuario() {
        System.out.println("👤 Testando criação de usuário...");

        // CONFIGURA MOCK
        when(repositoryUser.save(userEntity)).thenReturn(userEntity);

        // EXECUTA
        UserEntity result = userService.create(userEntity);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("user123");
        assertThat(result.getUsername()).isEqualTo("João Silva");

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(userEntity);

        System.out.println("✅ Criação funcionou!");
    }

    @Test
    @DisplayName("Deve criar usuário com dados mínimos")
    void deveCriarUsuarioComDadosMinimos() {
        System.out.println("🔧 Testando usuário com dados mínimos...");

        // CRIA USUÁRIO COM DADOS MÍNIMOS
        UserEntity minimalUser = new UserEntity();
        minimalUser.setId("minimal123");

        // CONFIGURA MOCK
        when(repositoryUser.save(minimalUser)).thenReturn(minimalUser);

        // EXECUTA
        UserEntity result = userService.create(minimalUser);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("minimal123");

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(minimalUser);

        System.out.println("✅ Usuário com dados mínimos funcionou!");
    }

    @Test
    @DisplayName("Deve criar usuário sem ID definido")
    void deveCriarUsuarioSemIdDefinido() {
        System.out.println("🆔 Testando usuário sem ID...");

        // CRIA USUÁRIO SEM ID
        UserEntity noIdUser = new UserEntity();
        noIdUser.setUsername("Sem ID");
        // ID fica null

        // USUÁRIO RETORNADO COM ID GERADO
        UserEntity savedUser = new UserEntity();
        savedUser.setId("generated123");
        savedUser.setUsername("Sem ID");

        // CONFIGURA MOCK
        when(repositoryUser.save(noIdUser)).thenReturn(savedUser);

        // EXECUTA
        UserEntity result = userService.create(noIdUser);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("generated123");

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(noIdUser);

        System.out.println("✅ Usuário sem ID funcionou!");
    }

    @Test
    @DisplayName("Deve propagar exceção do repository")
    void devePropagarExcecaoDoRepository() {
        System.out.println("❌ Testando exceção do repository...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(repositoryUser.save(any(UserEntity.class)))
            .thenThrow(new RuntimeException("Erro de banco de dados"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> userService.create(userEntity))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro de banco de dados");

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(userEntity);

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve aceitar usuário com todos os campos null")
    void deveAceitarUsuarioComTodosCamposNull() {
        System.out.println("🆔 Testando usuário vazio...");

        // CRIA USUÁRIO VAZIO
        UserEntity emptyUser = new UserEntity();
        // Todos os campos ficam null

        // CONFIGURA MOCK
        when(repositoryUser.save(emptyUser)).thenReturn(emptyUser);

        // EXECUTA
        UserEntity result = userService.create(emptyUser);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(emptyUser);

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(emptyUser);

        System.out.println("✅ Usuário vazio funcionou!");
    }

    @Test
    @DisplayName("Deve criar usuário com apenas email")
    void deveCriarUsuarioComApenasEmail() {
        System.out.println("📧 Testando usuário só com email...");

        // CRIA USUÁRIO APENAS COM EMAIL
        UserEntity emailOnlyUser = new UserEntity();

        // CONFIGURA MOCK
        when(repositoryUser.save(emailOnlyUser)).thenReturn(emailOnlyUser);

        // EXECUTA
        UserEntity result = userService.create(emailOnlyUser);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(emailOnlyUser);

        System.out.println("✅ Usuário só com email funcionou!");
    }

    @Test
    @DisplayName("Deve criar usuário com apenas nome")
    void deveCriarUsuarioComApenasNome() {
        System.out.println("📝 Testando usuário só com nome...");

        // CRIA USUÁRIO APENAS COM NOME
        UserEntity nameOnlyUser = new UserEntity();
        nameOnlyUser.setUsername("Apenas Nome");

        // CONFIGURA MOCK
        when(repositoryUser.save(nameOnlyUser)).thenReturn(nameOnlyUser);

        // EXECUTA
        UserEntity result = userService.create(nameOnlyUser);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(nameOnlyUser);

        System.out.println("✅ Usuário só com nome funcionou!");
    }

    @Test
    @DisplayName("Deve retornar o mesmo objeto salvo pelo repository")
    void deveRetornarOMesmoObjetoSalvoPeloRepository() {
        System.out.println("🔄 Testando retorno do objeto...");

        // CONFIGURA MOCK PARA RETORNAR OBJETO DIFERENTE
        UserEntity savedUser = new UserEntity();
        savedUser.setId("saved123");
        savedUser.setUsername("Nome Salvo");

        when(repositoryUser.save(userEntity)).thenReturn(savedUser);

        // EXECUTA
        UserEntity result = userService.create(userEntity);

        // VERIFICA QUE RETORNA O OBJETO DO REPOSITORY
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(savedUser);
        assertThat(result.getId()).isEqualTo("saved123");
        assertThat(result.getUsername()).isEqualTo("Nome Salvo");

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(userEntity);

        System.out.println("✅ Retorno do objeto funcionou!");
    }

    @Test
    @DisplayName("Deve aceitar null como parâmetro")
    void deveAceitarNullComoParametro() {
        System.out.println("❌ Testando null como parâmetro...");

        // CONFIGURA MOCK PARA ACEITAR NULL
        when(repositoryUser.save(null)).thenReturn(null);

        // EXECUTA
        UserEntity result = userService.create(null);

        // VERIFICA RESULTADO
        assertThat(result).isNull();

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(null);

        System.out.println("✅ Null como parâmetro funcionou!");
    }

    @Test
    @DisplayName("Deve tratar exceção de validação")
    void deveTratarExcecaoDeValidacao() {
        System.out.println("❌ Testando exceção de validação...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO DE VALIDAÇÃO
        when(repositoryUser.save(any(UserEntity.class)))
            .thenThrow(new IllegalArgumentException("Email inválido"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> userService.create(userEntity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email inválido");

        // VERIFICA CHAMADA
        verify(repositoryUser, times(1)).save(userEntity);

        System.out.println("✅ Exceção de validação tratada!");
    }
}