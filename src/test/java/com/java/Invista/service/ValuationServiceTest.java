package com.java.Invista.service;

import com.java.Invista.dto.request.ValuationRequest;
import com.java.Invista.entity.ValuationEntity;
import com.java.Invista.repository.RepositoryImovel;
import com.java.Invista.repository.RepositoryValuation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ValuationService")
class ValuationServiceTest {

    @Mock
    private RepositoryValuation repositoryValuation;

    @Mock
    private RepositoryImovel repositoryImovel;

    @Mock
    private ValuationRequest valuationRequest;

    @InjectMocks
    private ValuationService valuationService;

    private ValuationEntity valuationEntity;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA VALORIZAÇÃO
        valuationEntity = new ValuationEntity();
        valuationEntity.setId(1L);
        valuationEntity.setNameResponsible("João Avaliador");
        valuationEntity.setDescription("Avaliação do apartamento");
        valuationEntity.setDate(LocalDate.now());
        valuationEntity.setRotaImage("/images/valuation1.jpg");
        valuationEntity.setValue(350000.0);

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar valorização")
    void deveCriarValorizacao() {
        System.out.println("📈 Testando criação de valorização...");

        // CONFIGURA MOCKS
        when(valuationRequest.toModel(repositoryImovel)).thenReturn(valuationEntity);
        when(repositoryValuation.save(valuationEntity)).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.create(valuationRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNameResponsible()).isEqualTo("João Avaliador");
        assertThat(result.getValue()).isEqualTo(350000.0);

        // VERIFICA CHAMADAS
        verify(valuationRequest, times(1)).toModel(repositoryImovel);
        verify(repositoryValuation, times(1)).save(valuationEntity);

        System.out.println("✅ Criação funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar valorização existente")
    void deveAtualizarValorizacaoExistente() {
        System.out.println("✏️ Testando atualização...");

        // CRIA REQUEST DE ATUALIZAÇÃO
        ValuationRequest updateRequest = mock(ValuationRequest.class);
        when(updateRequest.getNameResponsible()).thenReturn("Novo Avaliador");
        when(updateRequest.getDescription()).thenReturn("Nova Descrição");
        when(updateRequest.getDate()).thenReturn(LocalDate.of(2024, 12, 31));
        when(updateRequest.getValue()).thenReturn(400000.0);

        // CONFIGURA MOCKS
        when(repositoryValuation.findById(1L)).thenReturn(Optional.of(valuationEntity));
        when(repositoryValuation.save(any(ValuationEntity.class))).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.update(updateRequest, 1L);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getNameResponsible()).isEqualTo("Novo Avaliador");
        assertThat(result.getDescription()).isEqualTo("Nova Descrição");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(result.getValue()).isEqualTo(400000.0);

        // VERIFICA CHAMADAS
        verify(repositoryValuation, times(1)).findById(1L);
        verify(repositoryValuation, times(1)).save(valuationEntity);

        System.out.println("✅ Atualização funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas campos não nulos")
    void deveAtualizarApenasCamposNaoNulos() {
        System.out.println("🔧 Testando atualização parcial...");

        // CRIA REQUEST PARCIAL
        ValuationRequest partialRequest = mock(ValuationRequest.class);
        when(partialRequest.getNameResponsible()).thenReturn("Só o Responsável");
        when(partialRequest.getDescription()).thenReturn(null);
        when(partialRequest.getDate()).thenReturn(null);
        when(partialRequest.getValue()).thenReturn(0.0); // Valor 0 não atualiza

        // CONFIGURA MOCKS
        when(repositoryValuation.findById(1L)).thenReturn(Optional.of(valuationEntity));
        when(repositoryValuation.save(any(ValuationEntity.class))).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.update(partialRequest, 1L);

        // VERIFICA QUE APENAS O RESPONSÁVEL FOI ALTERADO
        assertThat(result.getNameResponsible()).isEqualTo("Só o Responsável");
        assertThat(result.getDescription()).isEqualTo("Avaliação do apartamento"); // Valor original
        assertThat(result.getDate()).isEqualTo(LocalDate.now()); // Valor original
        assertThat(result.getValue()).isEqualTo(350000.0); // Valor original

        System.out.println("✅ Atualização parcial funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar valorização inexistente")
    void deveLancarExcecaoAoAtualizarValorizacaoInexistente() {
        System.out.println("❌ Testando atualização de valorização inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryValuation.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> valuationService.update(valuationRequest, 999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Valorização não encontrada");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryValuation, never()).save(any(ValuationEntity.class));

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve buscar valorização por ID do imóvel")
    void deveBuscarValorizacaoPorIdDoImovel() {
        System.out.println("🔍 Testando busca por ID do imóvel...");

        // CONFIGURA MOCK
        when(repositoryValuation.findByImovelId(1L)).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.getByIdImovel(1L);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNameResponsible()).isEqualTo("João Avaliador");

        // VERIFICA CHAMADA
        verify(repositoryValuation, times(1)).findByImovelId(1L);

        System.out.println("✅ Busca por ID do imóvel funcionou!");
    }

    @Test
    @DisplayName("Deve retornar null quando valorização não encontrada por ID do imóvel")
    void deveRetornarNullQuandoValorizacaoNaoEncontradaPorIdDoImovel() {
        System.out.println("🔍 Testando busca de valorização inexistente...");

        // CONFIGURA MOCK PARA RETORNAR NULL
        when(repositoryValuation.findByImovelId(999L)).thenReturn(null);

        // EXECUTA
        ValuationEntity result = valuationService.getByIdImovel(999L);

        // VERIFICA RESULTADO
        assertThat(result).isNull();

        // VERIFICA CHAMADA
        verify(repositoryValuation, times(1)).findByImovelId(999L);

        System.out.println("✅ Busca de inexistente funcionou!");
    }

    @Test
    @DisplayName("Deve propagar exceção do toModel")
    void devePropagarExcecaoDoToModel() {
        System.out.println("❌ Testando exceção do toModel...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(valuationRequest.toModel(repositoryImovel))
            .thenThrow(new RuntimeException("Imóvel não encontrado!"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> valuationService.create(valuationRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Imóvel não encontrado!");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryValuation, never()).save(any(ValuationEntity.class));

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve tratar valor zero na atualização")
    void deveTratarValorZeroNaAtualizacao() {
        System.out.println("🔧 Testando valor zero...");

        // CRIA REQUEST COM VALOR ZERO
        ValuationRequest zeroRequest = mock(ValuationRequest.class);
        when(zeroRequest.getNameResponsible()).thenReturn(null);
        when(zeroRequest.getDescription()).thenReturn(null);
        when(zeroRequest.getDate()).thenReturn(null);
        when(zeroRequest.getValue()).thenReturn(0.0); // Valor zero

        // CONFIGURA MOCKS
        when(repositoryValuation.findById(1L)).thenReturn(Optional.of(valuationEntity));
        when(repositoryValuation.save(any(ValuationEntity.class))).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.update(zeroRequest, 1L);

        // VERIFICA QUE NENHUM CAMPO FOI ALTERADO (valor 0 não atualiza)
        assertThat(result.getNameResponsible()).isEqualTo("João Avaliador"); // Valor original
        assertThat(result.getDescription()).isEqualTo("Avaliação do apartamento"); // Valor original
        assertThat(result.getValue()).isEqualTo(350000.0); // Valor original

        System.out.println("✅ Valor zero não atualizou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas valor quando outros campos são null")
    void deveAtualizarApenasValorQuandoOutrosCamposSaoNull() {
        System.out.println("💰 Testando atualização só do valor...");

        // CRIA REQUEST APENAS COM VALOR
        ValuationRequest valueOnlyRequest = mock(ValuationRequest.class);
        when(valueOnlyRequest.getNameResponsible()).thenReturn(null);
        when(valueOnlyRequest.getDescription()).thenReturn(null);
        when(valueOnlyRequest.getDate()).thenReturn(null);
        when(valueOnlyRequest.getValue()).thenReturn(450000.0); // Só valor

        // CONFIGURA MOCKS
        when(repositoryValuation.findById(1L)).thenReturn(Optional.of(valuationEntity));
        when(repositoryValuation.save(any(ValuationEntity.class))).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.update(valueOnlyRequest, 1L);

        // VERIFICA QUE APENAS O VALOR FOI ALTERADO
        assertThat(result.getNameResponsible()).isEqualTo("João Avaliador"); // Valor original
        assertThat(result.getDescription()).isEqualTo("Avaliação do apartamento"); // Valor original
        assertThat(result.getValue()).isEqualTo(450000.0); // Valor atualizado

        System.out.println("✅ Apenas valor atualizado!");
    }

    @Test
    @DisplayName("Deve propagar exceção do repository no save")
    void devePropagarExcecaoDoRepositoryNoSave() {
        System.out.println("❌ Testando exceção do repository...");

        // CONFIGURA MOCKS
        when(valuationRequest.toModel(repositoryImovel)).thenReturn(valuationEntity);
        when(repositoryValuation.save(any(ValuationEntity.class)))
            .thenThrow(new RuntimeException("Erro de banco de dados"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> valuationService.create(valuationRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro de banco de dados");

        // VERIFICA CHAMADAS
        verify(valuationRequest, times(1)).toModel(repositoryImovel);
        verify(repositoryValuation, times(1)).save(valuationEntity);

        System.out.println("✅ Exceção do repository propagada!");
    }

    @Test
    @DisplayName("Deve atualizar data quando fornecida")
    void deveAtualizarDataQuandoFornecida() {
        System.out.println("📅 Testando atualização de data...");

        // CRIA REQUEST APENAS COM DATA
        ValuationRequest dateOnlyRequest = mock(ValuationRequest.class);
        when(dateOnlyRequest.getNameResponsible()).thenReturn(null);
        when(dateOnlyRequest.getDescription()).thenReturn(null);
        when(dateOnlyRequest.getDate()).thenReturn(LocalDate.of(2025, 1, 15));
        when(dateOnlyRequest.getValue()).thenReturn(0.0); // Valor zero não atualiza

        // CONFIGURA MOCKS
        when(repositoryValuation.findById(1L)).thenReturn(Optional.of(valuationEntity));
        when(repositoryValuation.save(any(ValuationEntity.class))).thenReturn(valuationEntity);

        // EXECUTA
        ValuationEntity result = valuationService.update(dateOnlyRequest, 1L);

        // VERIFICA QUE APENAS A DATA FOI ALTERADA
        assertThat(result.getNameResponsible()).isEqualTo("João Avaliador"); // Valor original
        assertThat(result.getDescription()).isEqualTo("Avaliação do apartamento"); // Valor original
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2025, 1, 15)); // Data atualizada
        assertThat(result.getValue()).isEqualTo(350000.0); // Valor original

        System.out.println("✅ Apenas data atualizada!");
    }

    @Test
    @DisplayName("Deve criar valorização com dados completos")
    void deveCriarValorizacaoComDadosCompletos() {
        System.out.println("📋 Testando criação completa...");

        // CRIA VALORIZAÇÃO COMPLETA
        ValuationEntity completeValuation = new ValuationEntity();
        completeValuation.setId(2L);
        completeValuation.setNameResponsible("Maria Avaliadora");
        completeValuation.setDescription("Avaliação completa");
        completeValuation.setDate(LocalDate.of(2024, 6, 15));
        completeValuation.setRotaImage("/images/complete.jpg");
        completeValuation.setValue(500000.0);

        // CONFIGURA MOCKS
        when(valuationRequest.toModel(repositoryImovel)).thenReturn(completeValuation);
        when(repositoryValuation.save(completeValuation)).thenReturn(completeValuation);

        // EXECUTA
        ValuationEntity result = valuationService.create(valuationRequest);

        // VERIFICA RESULTADO COMPLETO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getNameResponsible()).isEqualTo("Maria Avaliadora");
        assertThat(result.getDescription()).isEqualTo("Avaliação completa");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(result.getRotaImage()).isEqualTo("/images/complete.jpg");
        assertThat(result.getValue()).isEqualTo(500000.0);

        System.out.println("✅ Criação completa funcionou!");
    }
}