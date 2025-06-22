package com.java.Invista.controller;

import com.java.Invista.service.ExpenseService;
import com.java.Invista.service.ImovelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboarController.class)
@DisplayName("Testes do DashboardController")
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImovelService imovelService;

    @MockBean
    private ExpenseService expenseService;

    private Map<String, Object> resumoDespesas;
    private List<Map<String, Object>> listaDespesas;

    @BeforeEach
    void setUp() {
        Map<String, Object> despesa1 = new HashMap<>();
        despesa1.put("título", "Conserto");
        despesa1.put("valor", 500.0);
        despesa1.put("data", LocalDate.of(2025, 5, 1));

        Map<String, Object> despesa2 = new HashMap<>();
        despesa2.put("título", "Limpeza");
        despesa2.put("valor", 200.0);
        despesa2.put("data", LocalDate.of(2025, 5, 15));

        listaDespesas = Arrays.asList(despesa1, despesa2);

        resumoDespesas = new HashMap<>();
        resumoDespesas.put("nomeImovel", "Casa do João");
        resumoDespesas.put("despesas", listaDespesas);
    }

    @Test
    @DisplayName("Deve buscar resumo de despesas do imóvel com sucesso")
    void deveBuscarResumoDespesasDoImovelComSucesso() throws Exception {
        Long imovelId = 1L;
        when(expenseService.getResumoDespesasPorImovel(imovelId)).thenReturn(resumoDespesas);

        mockMvc.perform(get("/dashboard/imovel/{id}/resumo-despesa", imovelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeImovel").value("Casa do João"))
                .andExpect(jsonPath("$.despesas").isArray())
                .andExpect(jsonPath("$.despesas.length()").value(2))
                .andExpect(jsonPath("$.despesas[0].título").value("Conserto"))
                .andExpect(jsonPath("$.despesas[0].valor").value(500.0))
                .andExpect(jsonPath("$.despesas[0].data").value("2025-05-01"))
                .andExpect(jsonPath("$.despesas[1].título").value("Limpeza"))
                .andExpect(jsonPath("$.despesas[1].valor").value(200.0));

        verify(expenseService, times(1)).getResumoDespesasPorImovel(imovelId);
    }

    @Test
    @DisplayName("Deve retornar resumo vazio quando imóvel não tem despesas")
    void deveRetornarResumoVazioQuandoImovelNaoTemDespesas() throws Exception {
        Long imovelId = 999L;
        Map<String, Object> resumoVazio = new HashMap<>();
        resumoVazio.put("nomeImovel", "Casa Vazia");
        resumoVazio.put("despesas", Arrays.asList());

        when(expenseService.getResumoDespesasPorImovel(imovelId)).thenReturn(resumoVazio);

        mockMvc.perform(get("/dashboard/imovel/{id}/resumo-despesa", imovelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeImovel").value("Casa Vazia"))
                .andExpect(jsonPath("$.despesas").isArray())
                .andExpect(jsonPath("$.despesas.length()").value(0));

        verify(expenseService, times(1)).getResumoDespesasPorImovel(imovelId);
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando ID do imóvel é inválido para resumo")
    void deveRetornarErro400QuandoIdDoImovelEInvalidoParaResumo() throws Exception {
        mockMvc.perform(get("/dashboard/imovel/{id}/resumo-despesa", "abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve buscar quantidade de imóveis com sucesso")
    void deveBuscarQuantidadeDeImoveisComSucesso() throws Exception {
        Long quantidadeEsperada = 25L;
        when(imovelService.contarImoveis()).thenReturn(quantidadeEsperada);

        mockMvc.perform(get("/dashboard/imovel/quantidade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("25"));

        verify(imovelService, times(1)).contarImoveis();
    }

    @Test
    @DisplayName("Deve retornar zero quando não há imóveis cadastrados")
    void deveRetornarZeroQuandoNaoHaImoveisCadastrados() throws Exception {
        when(imovelService.contarImoveis()).thenReturn(0L);

        mockMvc.perform(get("/dashboard/imovel/quantidade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        verify(imovelService, times(1)).contarImoveis();
    }

    @Test
    @DisplayName("Deve buscar valor total dos imóveis com sucesso")
    void deveBuscarValorTotalDosImoveisComSucesso() throws Exception {
        Double valorTotalEsperado = 1500000.0;
        when(imovelService.calcularValorTotalImoveis()).thenReturn(valorTotalEsperado);

        mockMvc.perform(get("/dashboard/valor-total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1500000.0"));

        verify(imovelService, times(1)).calcularValorTotalImoveis();
    }

    @Test
    @DisplayName("Deve retornar zero quando não há valor total de imóveis")
    void deveRetornarZeroQuandoNaoHaValorTotalDeImoveis() throws Exception {
        when(imovelService.calcularValorTotalImoveis()).thenReturn(0.0);

        mockMvc.perform(get("/dashboard/valor-total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));

        verify(imovelService, times(1)).calcularValorTotalImoveis();
    }

    @Test
    @DisplayName("Deve retornar valor nulo quando valor total é nulo")
    void deveRetornarValorNuloQuandoValorTotalENulo() throws Exception {
        when(imovelService.calcularValorTotalImoveis()).thenReturn(null);

        mockMvc.perform(get("/dashboard/valor-total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(imovelService, times(1)).calcularValorTotalImoveis();
    }

    @Test
    @DisplayName("Deve verificar se todas as dependências são chamadas corretamente")
    void deveVerificarSeTodasAsDependenciasSaoChamadasCorretamente() throws Exception {
        Long imovelId = 1L;
        when(expenseService.getResumoDespesasPorImovel(imovelId)).thenReturn(resumoDespesas);
        when(imovelService.contarImoveis()).thenReturn(10L);
        when(imovelService.calcularValorTotalImoveis()).thenReturn(1000000.0);

        mockMvc.perform(get("/dashboard/imovel/{id}/resumo-despesa", imovelId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/dashboard/imovel/quantidade"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/dashboard/valor-total"))
                .andExpect(status().isOk());

        verify(expenseService, times(1)).getResumoDespesasPorImovel(imovelId);
        verify(imovelService, times(1)).contarImoveis();
        verify(imovelService, times(1)).calcularValorTotalImoveis();
    }
}