package com.java.Invista.controller;

import com.java.Invista.entity.StateEntity;
import com.java.Invista.service.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StateController.class)
@DisplayName("Testes do StateController")
class StateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StateService stateService;

    @Autowired
    private ObjectMapper objectMapper;

    private StateEntity stateEntity;

    @BeforeEach
    void setUp() {
        stateEntity = new StateEntity();
        stateEntity.setId(1L);
        stateEntity.setName("São Paulo");
    }

    @Test
    @DisplayName("Deve criar um estado com sucesso")
    void deveCriarEstadoComSucesso() throws Exception {
        // Corrija aqui:
        when(stateService.create(any(StateEntity.class))).thenReturn(null);

        mockMvc.perform(post("/state")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stateEntity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Imovel cadastrado com sucesso!"));

        verify(stateService, times(1)).create(any(StateEntity.class));
    }

    @Test
    @DisplayName("Deve criar múltiplos estados com sucesso")
    void deveCriarMultiplosEstadosComSucesso() throws Exception {
        List<StateEntity> states = Arrays.asList(stateEntity);
        when(stateService.creates(any())).thenReturn("Estados cadastrados com sucesso!");

        mockMvc.perform(post("/state/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(states)))
                .andExpect(status().isOk())
                .andExpect(content().string("Estados cadastrados com sucesso!"));

        verify(stateService, times(1)).creates(any());
    }

    @Test
    @DisplayName("Deve listar todos os estados")
    void deveListarTodosEstados() throws Exception {
        when(stateService.getAll()).thenReturn(Arrays.asList(stateEntity));

        mockMvc.perform(get("/state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("São Paulo"));

        verify(stateService, times(1)).getAll();
    }
}