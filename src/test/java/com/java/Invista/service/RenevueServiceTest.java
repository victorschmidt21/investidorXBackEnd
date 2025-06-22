package com.java.Invista.service;

import com.java.Invista.dto.request.RenevueRequest;
import com.java.Invista.entity.RenevueEntity;
import com.java.Invista.repository.RepositoryImovel;
import com.java.Invista.repository.RepositoryRenevue;
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
@DisplayName("Testes do RenevueService")
class RenevueServiceTest {

    @Mock
    private RepositoryRenevue repositoryRenevue;

    @Mock
    private RepositoryImovel repositoryImovel;

    @Mock
    private RenevueRequest renevueRequest;

    @InjectMocks
    private RenevueService renevueService;

    private RenevueEntity renevueEntity;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA RECEITA
        renevueEntity = new RenevueEntity();
        renevueEntity.setId(1L);
        renevueEntity.setTitle("Aluguel");
        renevueEntity.setDescription("Aluguel mensal do apartamento");
        renevueEntity.setValue(2500.0);
        renevueEntity.setDate(LocalDate.now());

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar receita")
    void deveCriarReceita() {
        System.out.println("💰 Testando criação de receita...");

        // CONFIGURA MOCKS
        when(renevueRequest.toModel(repositoryImovel)).thenReturn(renevueEntity);
        when(repositoryRenevue.save(renevueEntity)).thenReturn(renevueEntity);

        // EXECUTA
        RenevueEntity result = renevueService.create(renevueRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Aluguel");
        assertThat(result.getValue()).isEqualTo(2500.0);

        // VERIFICA CHAMADAS
        verify(renevueRequest, times(1)).toModel(repositoryImovel);
        verify(repositoryRenevue, times(1)).save(renevueEntity);

        System.out.println("✅ Criação funcionou!");
    }

    @Test
    @DisplayName("Deve calcular valor total das receitas")
    void deveCalcularValorTotalDasReceitas() {
        System.out.println("📊 Testando cálculo de valor total...");

        // CONFIGURA MOCK
        when(repositoryRenevue.sumTotalValue()).thenReturn(7500.0);

        // EXECUTA
        Double result = renevueService.getValueTotal();

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo(7500.0);

        // VERIFICA CHAMADA
        verify(repositoryRenevue, times(1)).sumTotalValue();

        System.out.println("✅ Cálculo de valor total funcionou!");
    }

    @Test
    @DisplayName("Deve retornar zero quando valor total é null")
    void deveRetornarZeroQuandoValorTotalENull() {
        System.out.println("💰 Testando valor total null...");

        // CONFIGURA MOCK PARA RETORNAR NULL
        when(repositoryRenevue.sumTotalValue()).thenReturn(null);

        // EXECUTA
        Double result = renevueService.getValueTotal();

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo(0.0);

        // VERIFICA CHAMADA
        verify(repositoryRenevue, times(1)).sumTotalValue();

        System.out.println("✅ Valor null tratado como zero!");
    }

    @Test
    @DisplayName("Deve deletar receita por ID")
    void deveDeletarReceitaPorId() {
        System.out.println("🗑️ Testando deleção...");

        // CONFIGURA MOCK
        doNothing().when(repositoryRenevue).deleteById(1L);

        // EXECUTA
        String result = renevueService.deleteById(1L);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Deletado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryRenevue, times(1)).deleteById(1L);

        System.out.println("✅ Deleção funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar receita existente")
    void deveAtualizarReceitaExistente() {
        System.out.println("✏️ Testando atualização...");

        // CRIA REQUEST DE ATUALIZAÇÃO
        RenevueRequest updateRequest = mock(RenevueRequest.class);
        when(updateRequest.getTitle()).thenReturn("Novo Título");
        when(updateRequest.getDescription()).thenReturn("Nova Descrição");
        when(updateRequest.getValue()).thenReturn(3000.0);

        // CONFIGURA MOCKS
        when(repositoryRenevue.findById(1L)).thenReturn(Optional.of(renevueEntity));
        when(repositoryRenevue.save(any(RenevueEntity.class))).thenReturn(renevueEntity);

        // EXECUTA
        RenevueEntity result = renevueService.update(1L, updateRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Novo Título");
        assertThat(result.getDescription()).isEqualTo("Nova Descrição");
        assertThat(result.getValue()).isEqualTo(3000.0);

        // VERIFICA CHAMADAS
        verify(repositoryRenevue, times(1)).findById(1L);
        verify(repositoryRenevue, times(1)).save(renevueEntity);

        System.out.println("✅ Atualização funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas campos não nulos")
    void deveAtualizarApenasCamposNaoNulos() {
        System.out.println("🔧 Testando atualização parcial...");

        // CRIA REQUEST PARCIAL
        RenevueRequest partialRequest = mock(RenevueRequest.class);
        when(partialRequest.getTitle()).thenReturn("Só o Título");
        when(partialRequest.getDescription()).thenReturn(null);
        when(partialRequest.getValue()).thenReturn(0.0); // Valor 0 não atualiza

        // CONFIGURA MOCKS
        when(repositoryRenevue.findById(1L)).thenReturn(Optional.of(renevueEntity));
        when(repositoryRenevue.save(any(RenevueEntity.class))).thenReturn(renevueEntity);

        // EXECUTA
        RenevueEntity result = renevueService.update(1L, partialRequest);

        // VERIFICA QUE APENAS O TÍTULO FOI ALTERADO
        assertThat(result.getTitle()).isEqualTo("Só o Título");
        assertThat(result.getDescription()).isEqualTo("Aluguel mensal do apartamento"); // Valor original
        assertThat(result.getValue()).isEqualTo(2500.0); // Valor original

        System.out.println("✅ Atualização parcial funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar receita inexistente")
    void deveLancarExcecaoAoAtualizarReceitaInexistente() {
        System.out.println("❌ Testando atualização de receita inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryRenevue.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> renevueService.update(999L, renevueRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Despesa não encontrada!"); // Mensagem do código original

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryRenevue, never()).save(any(RenevueEntity.class));

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve propagar exceção do toModel")
    void devePropagarExcecaoDoToModel() {
        System.out.println("❌ Testando exceção do toModel...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(renevueRequest.toModel(repositoryImovel))
            .thenThrow(new RuntimeException("Imóvel não encontrado"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> renevueService.create(renevueRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Imóvel não encontrado");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryRenevue, never()).save(any(RenevueEntity.class));

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve tratar valor zero na atualização")
    void deveTratarValorZeroNaAtualizacao() {
        System.out.println("🔧 Testando valor zero...");

        // CRIA REQUEST COM VALOR ZERO
        RenevueRequest zeroRequest = mock(RenevueRequest.class);
        when(zeroRequest.getTitle()).thenReturn(null);
        when(zeroRequest.getDescription()).thenReturn(null);
        when(zeroRequest.getValue()).thenReturn(0.0); // Valor zero

        // CONFIGURA MOCKS
        when(repositoryRenevue.findById(1L)).thenReturn(Optional.of(renevueEntity));
        when(repositoryRenevue.save(any(RenevueEntity.class))).thenReturn(renevueEntity);

        // EXECUTA
        RenevueEntity result = renevueService.update(1L, zeroRequest);

        // VERIFICA QUE NENHUM CAMPO FOI ALTERADO (valor 0 não atualiza)
        assertThat(result.getTitle()).isEqualTo("Aluguel"); // Valor original
        assertThat(result.getDescription()).isEqualTo("Aluguel mensal do apartamento"); // Valor original
        assertThat(result.getValue()).isEqualTo(2500.0); // Valor original

        System.out.println("✅ Valor zero não atualizou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas valor quando outros campos são null")
    void deveAtualizarApenasValorQuandoOutrosCamposSaoNull() {
        System.out.println("💰 Testando atualização só do valor...");

        // CRIA REQUEST APENAS COM VALOR
        RenevueRequest valueOnlyRequest = mock(RenevueRequest.class);
        when(valueOnlyRequest.getTitle()).thenReturn(null);
        when(valueOnlyRequest.getDescription()).thenReturn(null);
        when(valueOnlyRequest.getValue()).thenReturn(3500.0); // Só valor

        // CONFIGURA MOCKS
        when(repositoryRenevue.findById(1L)).thenReturn(Optional.of(renevueEntity));
        when(repositoryRenevue.save(any(RenevueEntity.class))).thenReturn(renevueEntity);

        // EXECUTA
        RenevueEntity result = renevueService.update(1L, valueOnlyRequest);

        // VERIFICA QUE APENAS O VALOR FOI ALTERADO
        assertThat(result.getTitle()).isEqualTo("Aluguel"); // Valor original
        assertThat(result.getDescription()).isEqualTo("Aluguel mensal do apartamento"); // Valor original
        assertThat(result.getValue()).isEqualTo(3500.0); // Valor atualizado

        System.out.println("✅ Apenas valor atualizado!");
    }

    @Test
    @DisplayName("Deve deletar mesmo se receita não existir")
    void deveDeletarMesmoSeReceitaNaoExistir() {
        System.out.println("🗑️ Testando deleção de receita inexistente...");

        // CONFIGURA MOCK - deleteById não lança exceção no JPA
        doNothing().when(repositoryRenevue).deleteById(999L);

        // EXECUTA
        String result = renevueService.deleteById(999L);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Deletado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryRenevue, times(1)).deleteById(999L);

        System.out.println("✅ Deleção de inexistente funcionou!");
    }
}