package com.java.Invista.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.Invista.dto.request.ValuationRequest;
import com.java.Invista.entity.ValuationEntity;
import com.java.Invista.service.ValuationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ValuationController.class)
@DisplayName("Testes do ValuationController")
class ValuationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValuationService valuationService;

    @Autowired
    private ObjectMapper objectMapper;

    private ValuationEntity valuationEntity;
    private ValuationRequest valuationRequest;

    @BeforeEach
    void setUp() {
        valuationEntity = new ValuationEntity();
        valuationEntity.setId(1L);
        valuationEntity.setValue(500000.0);

        valuationRequest = new ValuationRequest();
        valuationRequest.setValue(500000.0);
    }

    @Test
    @DisplayName("Deve criar uma avaliação com sucesso")
    void deveCriarValuationComSucesso() throws Exception {
        when(valuationService.create(any(ValuationRequest.class))).thenReturn(valuationEntity);

        mockMvc.perform(post("/valuation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valuationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(500000.0));

        verify(valuationService, times(1)).create(any(ValuationRequest.class));
    }

    @Test
    @DisplayName("Deve atualizar uma avaliação com sucesso")
    void deveAtualizarValuationComSucesso() throws Exception {
        when(valuationService.update(any(ValuationRequest.class), eq(1L))).thenReturn(valuationEntity);

        mockMvc.perform(put("/valuation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valuationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(500000.0));

        verify(valuationService, times(1)).update(any(ValuationRequest.class), eq(1L));
    }

    @Test
    @DisplayName("Deve buscar uma avaliação por ID do imóvel")
    void deveBuscarValuationPorIdImovel() throws Exception {
        when(valuationService.getByIdImovel(1L)).thenReturn(valuationEntity);

        mockMvc.perform(get("/valuation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(500000.0));

        verify(valuationService, times(1)).getByIdImovel(1L);
    }
}