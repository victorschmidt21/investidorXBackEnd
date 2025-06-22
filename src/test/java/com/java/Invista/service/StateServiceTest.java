package com.java.Invista.service;

import com.java.Invista.entity.StateEntity;
import com.java.Invista.repository.RepositoryState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do StateService")
class StateServiceTest {

    @Mock
    private RepositoryState repositoryState;

    @InjectMocks
    private StateService stateService;

    private StateEntity stateEntity;
    private List<StateEntity> stateList;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA ESTADO - REMOVE setSigla()
        stateEntity = new StateEntity();
        stateEntity.setId(1L);
        stateEntity.setName("São Paulo");
        // stateEntity.setSigla("SP"); // <- REMOVE ESTA LINHA

        // CRIA LISTA DE ESTADOS - REMOVE setSigla()
        StateEntity state2 = new StateEntity();
        state2.setId(2L);
        state2.setName("Rio de Janeiro");
        // state2.setSigla("RJ"); // <- REMOVE ESTA LINHA

        StateEntity state3 = new StateEntity();
        state3.setId(3L);
        state3.setName("Minas Gerais");
        // state3.setSigla("MG"); // <- REMOVE ESTA LINHA

        stateList = Arrays.asList(stateEntity, state2, state3);

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar um estado")
    void deveCriarUmEstado() {
        System.out.println("🌎 Testando criação de estado...");

        // CONFIGURA MOCK
        when(repositoryState.save(stateEntity)).thenReturn(stateEntity);

        // EXECUTA
        String result = stateService.create(stateEntity);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).save(stateEntity);

        System.out.println("✅ Criação de estado funcionou!");
    }

    @Test
    @DisplayName("Deve criar múltiplos estados")
    void deveCriarMultiplosEstados() {
        System.out.println("🌎🌎🌎 Testando criação de múltiplos estados...");

        // CONFIGURA MOCK
        when(repositoryState.saveAll(stateList)).thenReturn(stateList);

        // EXECUTA
        String result = stateService.creates(stateList);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).saveAll(stateList);

        System.out.println("✅ Criação de múltiplos estados funcionou!");
    }

    @Test
    @DisplayName("Deve listar todos os estados")
    void deveListarTodosOsEstados() {
        System.out.println("📋 Testando listagem de estados...");

        // CONFIGURA MOCK
        when(repositoryState.findAll()).thenReturn(stateList);

        // EXECUTA
        List<StateEntity> result = stateService.getAll();

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getName()).isEqualTo("São Paulo");
        assertThat(result.get(1).getName()).isEqualTo("Rio de Janeiro");
        assertThat(result.get(2).getName()).isEqualTo("Minas Gerais");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).findAll();

        System.out.println("✅ Listagem funcionou!");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há estados")
    void deveRetornarListaVaziaQuandoNaoHaEstados() {
        System.out.println("📝 Testando lista vazia...");

        // CONFIGURA MOCK PARA RETORNAR LISTA VAZIA
        when(repositoryState.findAll()).thenReturn(Arrays.asList());

        // EXECUTA
        List<StateEntity> result = stateService.getAll();

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).findAll();

        System.out.println("✅ Lista vazia tratada corretamente!");
    }

    @Test
    @DisplayName("Deve criar estado mesmo com dados mínimos")
    void deveCriarEstadoMesmoComDadosMinimos() {
        System.out.println("🔧 Testando estado com dados mínimos...");

        // CRIA ESTADO COM DADOS MÍNIMOS - CORRIGE O MÉTODO
        StateEntity minimalState = new StateEntity();
        // minimalState.getName("Acre"); // <- ERRO! getName() não existe
        minimalState.setName("Acre"); // <- CORRETO!

        // CONFIGURA MOCK
        when(repositoryState.save(minimalState)).thenReturn(minimalState);

        // EXECUTA
        String result = stateService.create(minimalState);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).save(minimalState);

        System.out.println("✅ Estado com dados mínimos funcionou!");
    }

    @Test
    @DisplayName("Deve criar lista vazia de estados")
    void deveCriarListaVaziaDeEstados() {
        System.out.println("📝 Testando lista vazia de estados...");

        List<StateEntity> emptyList = Arrays.asList();

        // CONFIGURA MOCK
        when(repositoryState.saveAll(emptyList)).thenReturn(emptyList);

        // EXECUTA
        String result = stateService.creates(emptyList);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).saveAll(emptyList);

        System.out.println("✅ Lista vazia criada!");
    }

    @Test
    @DisplayName("Deve propagar exceção do repository no create")
    void devePropagarExcecaoDoRepositoryNoCreate() {
        System.out.println("❌ Testando exceção no create...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(repositoryState.save(any(StateEntity.class)))
            .thenThrow(new RuntimeException("Erro de banco de dados"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> stateService.create(stateEntity))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro de banco de dados");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).save(stateEntity);

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve propagar exceção do repository no creates")
    void devePropagarExcecaoDoRepositoryNoCreates() {
        System.out.println("❌ Testando exceção no creates...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(repositoryState.saveAll(anyList()))
            .thenThrow(new RuntimeException("Erro de transação"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> stateService.creates(stateList))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro de transação");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).saveAll(stateList);

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve propagar exceção do repository no getAll")
    void devePropagarExcecaoDoRepositoryNoGetAll() {
        System.out.println("❌ Testando exceção no getAll...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(repositoryState.findAll())
            .thenThrow(new RuntimeException("Erro de consulta"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> stateService.getAll())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro de consulta");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).findAll();

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve aceitar estado com ID null")
    void deveAceitarEstadoComIdNull() {
        System.out.println("🆔 Testando estado sem ID...");

        // CRIA ESTADO SEM ID - REMOVE setSigla()
        StateEntity noIdState = new StateEntity();
        noIdState.setName("Bahia");
        // noIdState.setSigla("BA"); // <- REMOVE ESTA LINHA
        // ID fica null

        // CONFIGURA MOCK
        when(repositoryState.save(noIdState)).thenReturn(noIdState);

        // EXECUTA
        String result = stateService.create(noIdState);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).save(noIdState);

        System.out.println("✅ Estado sem ID funcionou!");
    }

    @Test
    @DisplayName("Deve criar estado com todos os campos preenchidos")
    void deveCriarEstadoComTodosOsCamposPreenchidos() {
        System.out.println("📝 Testando estado completo...");

        // CRIA ESTADO COMPLETO - REMOVE setSigla()
        StateEntity completeState = new StateEntity();
        completeState.setId(99L);
        completeState.setName("Santa Catarina");
        // completeState.setSigla("SC"); // <- REMOVE ESTA LINHA

        // CONFIGURA MOCK
        when(repositoryState.save(completeState)).thenReturn(completeState);

        // EXECUTA
        String result = stateService.create(completeState);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).save(completeState);

        System.out.println("✅ Estado completo funcionou!");
    }

    @Test
    @DisplayName("Deve criar lista com um único estado")
    void deveCriarListaComUmUnicoEstado() {
        System.out.println("1️⃣ Testando lista com um estado...");

        List<StateEntity> singleStateList = Arrays.asList(stateEntity);

        // CONFIGURA MOCK
        when(repositoryState.saveAll(singleStateList)).thenReturn(singleStateList);

        // EXECUTA
        String result = stateService.creates(singleStateList);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Cadastrado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryState, times(1)).saveAll(singleStateList);

        System.out.println("✅ Lista com um estado funcionou!");
    }
}