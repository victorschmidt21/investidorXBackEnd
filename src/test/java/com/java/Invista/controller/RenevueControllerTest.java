package com.java.Invista.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.Invista.dto.request.RenevueRequest;
import com.java.Invista.entity.RenevueEntity;
import com.java.Invista.service.RenevueService;
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

@WebMvcTest(RenevueController.class)
@DisplayName("Testes do RenevueController")
class RenevueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RenevueService renevueService;

    @Autowired
    private ObjectMapper objectMapper;

    private RenevueEntity renevueEntity;
    private RenevueRequest renevueRequest;

    @BeforeEach
    void setUp() {
        renevueEntity = new RenevueEntity();
        renevueEntity.setId(1L);
        renevueEntity.setValue(1000.0);

        renevueRequest = new RenevueRequest();

    }

    @Test
    @DisplayName("Deve criar uma receita com sucesso")
    void deveCriarRenevueComSucesso() throws Exception {
        when(renevueService.create(any(RenevueRequest.class))).thenReturn(renevueEntity);

        mockMvc.perform(post("/renevue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(renevueRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(1000.0));

        verify(renevueService, times(1)).create(any(RenevueRequest.class));
    }

    @Test
    @DisplayName("Deve retornar valor total das receitas")
    void deveRetornarValorTotalDasReceitas() throws Exception {
        when(renevueService.getValueTotal()).thenReturn(5000.0);

        mockMvc.perform(get("/renevue/imovel/total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.0"));

        verify(renevueService, times(1)).getValueTotal();
    }

    @Test
    @DisplayName("Deve deletar uma receita com sucesso")
    void deveDeletarRenevueComSucesso() throws Exception {
        when(renevueService.deleteById(1L)).thenReturn("Receita deletada com sucesso!");

        mockMvc.perform(delete("/renevue/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Receita deletada com sucesso!"));

        verify(renevueService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve atualizar uma receita com sucesso")
    void deveAtualizarRenevueComSucesso() throws Exception {
        when(renevueService.update(eq(1L), any(RenevueRequest.class))).thenReturn(renevueEntity);

        mockMvc.perform(put("/renevue/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(renevueRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(1000.0));

        verify(renevueService, times(1)).update(eq(1L), any(RenevueRequest.class));
    }
}