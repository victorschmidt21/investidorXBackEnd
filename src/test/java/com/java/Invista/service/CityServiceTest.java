package com.java.Invista.service;

import com.java.Invista.dto.request.CityRequest;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.StateEntity;
import com.java.Invista.repository.RepositoryCity;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CityService")
class CityServiceTest {

    @Mock
    private RepositoryCity repositoryCity;

    @Mock
    private RepositoryState repositoryState;

    @Mock
    private CityRequest cityRequest;

    @InjectMocks
    private CityService cityService;

    private CityEntity cityEntity;
    private StateEntity stateEntity;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA ESTADO
        stateEntity = new StateEntity();
        stateEntity.setId(1L);
        stateEntity.setName("São Paulo");

        // CRIA CIDADE
        cityEntity = new CityEntity();
        cityEntity.setId(1L);
        cityEntity.setNome("São Paulo");
        cityEntity.setState(stateEntity);

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve buscar cidade por ID existente")
    void deveBuscarCidadePorIdExistente() {
        System.out.println("🔍 Testando busca de cidade por ID existente...");

        // CONFIGURA MOCK
        when(repositoryCity.findById(1L)).thenReturn(Optional.of(cityEntity));

        // EXECUTA
        Optional<CityEntity> result = cityService.getById(1L);

        // VERIFICA RESULTADO
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getNome()).isEqualTo("São Paulo");
        assertThat(result.get().getState().getName()).isEqualTo("São Paulo");

        // VERIFICA CHAMADA
        verify(repositoryCity, times(1)).findById(1L);

        System.out.println("✅ Busca por ID funcionou!");
    }

    @Test
    @DisplayName("Deve retornar vazio para ID inexistente")
    void deveRetornarVazioParaIdInexistente() {
        System.out.println("👻 Testando busca por ID inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryCity.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA
        Optional<CityEntity> result = cityService.getById(999L);

        // VERIFICA RESULTADO
        assertThat(result).isEmpty();

        // VERIFICA CHAMADA
        verify(repositoryCity, times(1)).findById(999L);

        System.out.println("✅ ID inexistente tratado corretamente!");
    }

    @Test
    @DisplayName("Deve criar uma cidade")
    void deveCriarUmaCidade() {
        System.out.println("🏗️ Testando criação de cidade...");

        // CONFIGURA MOCK DO REQUEST
        when(cityRequest.toModel(repositoryState)).thenReturn(cityEntity);
        when(repositoryCity.save(any(CityEntity.class))).thenReturn(cityEntity);

        // EXECUTA
        cityService.create(cityRequest);

        // VERIFICA CHAMADAS
        verify(cityRequest, times(1)).toModel(repositoryState);
        verify(repositoryCity, times(1)).save(cityEntity);

        System.out.println("✅ Criação de cidade funcionou!");
    }

    @Test
    @DisplayName("Deve criar múltiplas cidades")
    void deveCriarMultiplasCidades() {
        System.out.println("🏗️ Testando criação de múltiplas cidades...");

        // CRIA MOCKS DE REQUESTS
        CityRequest request1 = mock(CityRequest.class);
        CityRequest request2 = mock(CityRequest.class);
        CityRequest request3 = mock(CityRequest.class);

        // CRIA ENTITIES PARA RETORNO
        CityEntity city1 = new CityEntity();
        city1.setId(1L);
        city1.setNome("São Paulo");

        CityEntity city2 = new CityEntity();
        city2.setId(2L);
        city2.setNome("Rio de Janeiro");

        CityEntity city3 = new CityEntity();
        city3.setId(3L);
        city3.setNome("Belo Horizonte");

        List<CityRequest> requests = Arrays.asList(request1, request2, request3);

        // CONFIGURA MOCKS
        when(request1.toModel(repositoryState)).thenReturn(city1);
        when(request2.toModel(repositoryState)).thenReturn(city2);
        when(request3.toModel(repositoryState)).thenReturn(city3);

        when(repositoryCity.save(city1)).thenReturn(city1);
        when(repositoryCity.save(city2)).thenReturn(city2);
        when(repositoryCity.save(city3)).thenReturn(city3);

        // EXECUTA
        cityService.createAll(requests);

        // VERIFICA CHAMADAS
        verify(request1, times(1)).toModel(repositoryState);
        verify(request2, times(1)).toModel(repositoryState);
        verify(request3, times(1)).toModel(repositoryState);

        verify(repositoryCity, times(1)).save(city1);
        verify(repositoryCity, times(1)).save(city2);
        verify(repositoryCity, times(1)).save(city3);

        System.out.println("✅ Criação múltipla funcionou!");
    }

    @Test
    @DisplayName("Deve tratar lista vazia na criação múltipla")
    void deveTratarListaVaziaNaCriacaoMultipla() {
        System.out.println("📝 Testando lista vazia...");

        // EXECUTA COM LISTA VAZIA
        cityService.createAll(Arrays.asList());

        // VERIFICA QUE NENHUMA CHAMADA FOI FEITA
        verify(repositoryCity, never()).save(any(CityEntity.class));

        System.out.println("✅ Lista vazia tratada corretamente!");
    }

    @Test
    @DisplayName("Deve propagar exceção do toModel")
    void devePropagarExcecaoDoToModel() {
        System.out.println("❌ Testando exceção do toModel...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(cityRequest.toModel(repositoryState))
            .thenThrow(new RuntimeException("Estado não encontrado"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> cityService.create(cityRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Estado não encontrado");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryCity, never()).save(any(CityEntity.class));

        System.out.println("✅ Exceção propagada corretamente!");
    }

    @Test
    @DisplayName("Deve propagar exceção do save")
    void devePropagarExcecaoDoSave() {
        System.out.println("❌ Testando exceção do save...");

        // CONFIGURA MOCKS
        when(cityRequest.toModel(repositoryState)).thenReturn(cityEntity);
        when(repositoryCity.save(cityEntity))
            .thenThrow(new RuntimeException("Erro ao salvar no banco"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> cityService.create(cityRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro ao salvar no banco");

        // VERIFICA CHAMADAS
        verify(cityRequest, times(1)).toModel(repositoryState);
        verify(repositoryCity, times(1)).save(cityEntity);

        System.out.println("✅ Exceção do save propagada!");
    }

    @Test
    @DisplayName("Deve continuar processamento mesmo com erro em um item da lista")
    void deveContinuarProcessamentoMesmoComErroEmUmItem() {
        System.out.println("⚠️ Testando comportamento com erro parcial...");

        // CRIA REQUESTS
        CityRequest request1 = mock(CityRequest.class);
        CityRequest request2 = mock(CityRequest.class); // Este vai dar erro

        CityEntity city1 = new CityEntity();
        city1.setNome("Cidade 1");

        List<CityRequest> requests = Arrays.asList(request1, request2);

        // CONFIGURA MOCKS - request2 vai dar erro
        when(request1.toModel(repositoryState)).thenReturn(city1);
        when(request2.toModel(repositoryState))
            .thenThrow(new RuntimeException("Erro no item 2"));

        // EXECUTA E VERIFICA QUE LANÇA EXCEÇÃO
        assertThatThrownBy(() -> cityService.createAll(requests))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro no item 2");

        // VERIFICA QUE APENAS O PRIMEIRO FOI PROCESSADO
        verify(request1, times(1)).toModel(repositoryState);
        verify(request2, times(1)).toModel(repositoryState);

        verify(repositoryCity, times(1)).save(city1);

        System.out.println("✅ Comportamento com erro parcial verificado!");
    }

    @Test
    @DisplayName("Deve validar interações corretas com repositories")
    void deveValidarInteracoesCorretasComRepositories() {
        System.out.println("🔍 Testando interações com repositories...");

        // TESTA getById
        when(repositoryCity.findById(1L)).thenReturn(Optional.of(cityEntity));
        cityService.getById(1L);

        // TESTA create
        when(cityRequest.toModel(repositoryState)).thenReturn(cityEntity);
        cityService.create(cityRequest);

        // VERIFICA TODAS AS INTERAÇÕES
        verify(repositoryCity, times(1)).findById(1L);
        verify(repositoryCity, times(1)).save(cityEntity);
        verify(cityRequest, times(1)).toModel(repositoryState);

        // VERIFICA QUE O REPOSITORY STATE FOI PASSADO PARA O REQUEST
        verify(cityRequest).toModel(eq(repositoryState));

        System.out.println("✅ Interações validadas!");
    }
}