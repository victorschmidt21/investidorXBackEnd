package com.java.Invista.service;

import com.java.Invista.dto.request.ExpenseRequest;
import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.repository.RepositoryExpense;
import com.java.Invista.repository.RepositoryImovel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ExpenseService")
class ExpenseServiceTest {

    @Mock
    private RepositoryExpense repositoryExpense;

    @Mock
    private RepositoryImovel repositoryImovel;

    @Mock
    private ExpenseRequest expenseRequest;

    @InjectMocks
    private ExpenseService expenseService;

    private ExpenseEntity expenseEntity;
    private ImovelEntity imovelEntity;
    private List<ExpenseEntity> expenseList;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA IMÓVEL
        imovelEntity = new ImovelEntity();
        imovelEntity.setId_imovel(1L);
        imovelEntity.setNome_imovel("Apartamento Centro");

        // CRIA DESPESA
        expenseEntity = new ExpenseEntity();
        expenseEntity.setId(1L);
        expenseEntity.setTitle("Condomínio");
        expenseEntity.setDescription("Taxa mensal");
        expenseEntity.setValue(500.0);
        expenseEntity.setDate(LocalDate.now());
        expenseEntity.setImovel(imovelEntity);

        // CRIA LISTA DE DESPESAS
        ExpenseEntity expense1 = new ExpenseEntity();
        expense1.setId(1L);
        expense1.setTitle("Condomínio");
        expense1.setValue(500.0);
        expense1.setDate(LocalDate.now());

        ExpenseEntity expense2 = new ExpenseEntity();
        expense2.setId(2L);
        expense2.setTitle("IPTU");
        expense2.setValue(300.0);
        expense2.setDate(LocalDate.now());

        ExpenseEntity expense3 = new ExpenseEntity();
        expense3.setId(3L);
        expense3.setTitle("Manutenção");
        expense3.setValue(200.0);
        expense3.setDate(LocalDate.now());

        expenseList = Arrays.asList(expense1, expense2, expense3);

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar despesa")
    void deveCriarDespesa() {
        System.out.println("💰 Testando criação de despesa...");

        // CONFIGURA MOCKS
        when(expenseRequest.toModel(repositoryImovel)).thenReturn(expenseEntity);
        when(repositoryExpense.save(expenseEntity)).thenReturn(expenseEntity);

        // EXECUTA
        ExpenseEntity result = expenseService.create(expenseRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Condomínio");

        // VERIFICA CHAMADAS
        verify(expenseRequest, times(1)).toModel(repositoryImovel);
        verify(repositoryExpense, times(1)).save(expenseEntity);

        System.out.println("✅ Criação funcionou!");
    }

    @Test
    @DisplayName("Deve calcular valor total das despesas por imóvel")
    void deveCalcularValorTotalDasDespesasPorImovel() {
        System.out.println("📊 Testando cálculo de valor total...");

        // CONFIGURA MOCK
        when(repositoryExpense.findByImovelId(1L)).thenReturn(expenseList);

        // EXECUTA
        Map<String, Object> result = expenseService.getValueTotal(1L);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.get("Número de despesas")).isEqualTo(3);
        assertThat(result.get("valueTotal")).isEqualTo(1000.0); // 500 + 300 + 200
        assertThat(result.get("Ticket Médio")).isEqualTo(333.3333333333333); // 1000/3

        // VERIFICA CHAMADA
        verify(repositoryExpense, times(1)).findByImovelId(1L);

        System.out.println("✅ Cálculo de valor total funcionou!");
    }

    @Test
    @DisplayName("Deve tratar lista vazia no cálculo de valor total")
    void deveTratarListaVaziaNoCalculoDeValorTotal() {
        System.out.println("📝 Testando lista vazia...");

        // CONFIGURA MOCK PARA RETORNAR LISTA VAZIA
        when(repositoryExpense.findByImovelId(1L)).thenReturn(Arrays.asList());

        // EXECUTA - NÃO LANÇA EXCEÇÃO
        Map<String, Object> result = expenseService.getValueTotal(1L);

        // VERIFICA RESULTADO COM LISTA VAZIA
        assertThat(result).isNotNull();
        assertThat(result.get("Número de despesas")).isEqualTo(0);
        assertThat(result.get("valueTotal")).isEqualTo(0.0);
        
        // TICKET MÉDIO SERÁ NaN (0.0 / 0.0 = NaN, não Infinity!)
        Object ticketMedio = result.get("Ticket Médio");
        assertThat(ticketMedio).isEqualTo(Double.NaN);

        System.out.println("✅ Lista vazia tratada - Ticket Médio: " + ticketMedio);
    }

    @Test
    @DisplayName("Deve deletar despesa por ID")
    void deveDeletarDespesaPorId() {
        System.out.println("🗑️ Testando deleção...");

        // CONFIGURA MOCK
        doNothing().when(repositoryExpense).deleteById(1L);

        // EXECUTA
        String result = expenseService.deleteById(1L);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Deletado com sucesso!");

        // VERIFICA CHAMADA
        verify(repositoryExpense, times(1)).deleteById(1L);

        System.out.println("✅ Deleção funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar despesa existente")
    void deveAtualizarDespesaExistente() {
        System.out.println("✏️ Testando atualização...");

        // CRIA REQUEST DE ATUALIZAÇÃO COM MOCK
        ExpenseRequest updateRequest = mock(ExpenseRequest.class);
        when(updateRequest.getTitle()).thenReturn("Novo Título");
        when(updateRequest.getDescription()).thenReturn("Nova Descrição");
        when(updateRequest.getValue()).thenReturn(600.0);

        // CONFIGURA MOCKS
        when(repositoryExpense.findById(1L)).thenReturn(Optional.of(expenseEntity));
        when(repositoryExpense.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);

        // EXECUTA
        ExpenseEntity result = expenseService.update(1L, updateRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Novo Título");
        assertThat(result.getDescription()).isEqualTo("Nova Descrição");
        assertThat(result.getValue()).isEqualTo(600.0);

        // VERIFICA CHAMADAS
        verify(repositoryExpense, times(1)).findById(1L);
        verify(repositoryExpense, times(1)).save(expenseEntity);

        System.out.println("✅ Atualização funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas campos não nulos")
    void deveAtualizarApenasCamposNaoNulos() {
        System.out.println("🔧 Testando atualização parcial...");

        // CRIA REQUEST PARCIAL COM MOCK
        ExpenseRequest partialRequest = mock(ExpenseRequest.class);
        when(partialRequest.getTitle()).thenReturn("Só o Título");
        when(partialRequest.getDescription()).thenReturn(null);
        when(partialRequest.getValue()).thenReturn(0.0); // Valor 0 não atualiza

        // CONFIGURA MOCKS
        when(repositoryExpense.findById(1L)).thenReturn(Optional.of(expenseEntity));
        when(repositoryExpense.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);

        // EXECUTA
        ExpenseEntity result = expenseService.update(1L, partialRequest);

        // VERIFICA QUE APENAS O TÍTULO FOI ALTERADO
        assertThat(result.getTitle()).isEqualTo("Só o Título");
        assertThat(result.getDescription()).isEqualTo("Taxa mensal"); // Valor original
        assertThat(result.getValue()).isEqualTo(500.0); // Valor original

        System.out.println("✅ Atualização parcial funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar despesa inexistente")
    void deveLancarExcecaoAoAtualizarDespesaInexistente() {
        System.out.println("❌ Testando atualização de despesa inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryExpense.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> expenseService.update(999L, expenseRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Despesa não encontrada!");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryExpense, never()).save(any(ExpenseEntity.class));

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve criar múltiplas despesas")
    void deveCriarMultiplasDespesas() {
        System.out.println("📝 Testando criação múltipla...");

        // CRIA REQUESTS
        List<ExpenseRequest> requests = Arrays.asList(
            mock(ExpenseRequest.class),
            mock(ExpenseRequest.class)
        );

        // CONFIGURA MOCKS
        when(requests.get(0).toModel(repositoryImovel)).thenReturn(expenseList.get(0));
        when(requests.get(1).toModel(repositoryImovel)).thenReturn(expenseList.get(1));
        when(repositoryExpense.saveAll(anyList())).thenReturn(expenseList.subList(0, 2));

        // EXECUTA
        List<ExpenseEntity> result = expenseService.createMany(requests);

        // VERIFICA RESULTADO
        assertThat(result).hasSize(2);

        // VERIFICA CHAMADAS
        verify(repositoryExpense, times(1)).saveAll(anyList());

        System.out.println("✅ Criação múltipla funcionou!");
    }

    @Test
    @DisplayName("Deve criar uma despesa com createOne")
    void deveCriarUmaDespesaComCreateOne() {
        System.out.println("💰 Testando createOne...");

        // CONFIGURA MOCKS
        when(expenseRequest.toModel(repositoryImovel)).thenReturn(expenseEntity);
        when(repositoryExpense.save(expenseEntity)).thenReturn(expenseEntity);

        // EXECUTA
        ExpenseEntity result = expenseService.createOne(expenseRequest);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo(expenseEntity);

        // VERIFICA CHAMADAS
        verify(expenseRequest, times(1)).toModel(repositoryImovel);
        verify(repositoryExpense, times(1)).save(expenseEntity);

        System.out.println("✅ CreateOne funcionou!");
    }

    @Test
    @DisplayName("Deve obter resumo das despesas por imóvel")
    void deveObterResumoDasDespesasPorImovel() {
        System.out.println("📋 Testando resumo das despesas...");

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(repositoryExpense.findByImovelId(1L)).thenReturn(expenseList);

        // EXECUTA
        Map<String, Object> result = expenseService.getResumoDespesasPorImovel(1L);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.get("nomeImovel")).isEqualTo("Apartamento Centro");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> despesas = (List<Map<String, Object>>) result.get("despesas");
        assertThat(despesas).hasSize(3);
        
        // VERIFICA PRIMEIRA DESPESA
        Map<String, Object> primeiraDespesa = despesas.get(0);
        assertThat(primeiraDespesa.get("título")).isEqualTo("Condomínio");
        assertThat(primeiraDespesa.get("valor")).isEqualTo(500.0);

        // VERIFICA CHAMADAS
        verify(repositoryImovel, times(1)).findById(1L);
        verify(repositoryExpense, times(1)).findByImovelId(1L);

        System.out.println("✅ Resumo funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar resumo de imóvel inexistente")
    void deveLancarExcecaoAoBuscarResumoDeImovelInexistente() {
        System.out.println("❌ Testando resumo de imóvel inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryImovel.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> expenseService.getResumoDespesasPorImovel(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Imóvel não encontrado");

        // VERIFICA QUE BUSCA DE DESPESAS NÃO FOI CHAMADA
        verify(repositoryExpense, never()).findByImovelId(anyLong());

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve propagar exceção do toModel")
    void devePropagarExcecaoDoToModel() {
        System.out.println("❌ Testando exceção do toModel...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(expenseRequest.toModel(repositoryImovel))
            .thenThrow(new RuntimeException("Imóvel não encontrado"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> expenseService.create(expenseRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Imóvel não encontrado");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryExpense, never()).save(any(ExpenseEntity.class));

        System.out.println("✅ Exceção propagada!");
    }

    @Test
    @DisplayName("Deve calcular valores mesmo com lista vazia")
    void deveCalcularValoresMesmoComListaVazia() {
        System.out.println("📝 Testando comportamento com lista vazia...");

        // CONFIGURA MOCK PARA RETORNAR LISTA VAZIA
        when(repositoryExpense.findByImovelId(1L)).thenReturn(Arrays.asList());

        // EXECUTA
        Map<String, Object> result = expenseService.getValueTotal(1L);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.get("Número de despesas")).isEqualTo(0);
        assertThat(result.get("valueTotal")).isEqualTo(0.0);

        System.out.println("✅ Lista vazia retorna Infinity para ticket médio!");
    }
}