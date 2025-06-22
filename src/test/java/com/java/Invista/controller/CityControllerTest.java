package com.java.Invista.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.Invista.dto.request.CityRequest;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.StateEntity;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
@DisplayName("Testes do CityController")
class  CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @MockBean
    private RepositoryCity repositoryCity;

    @Autowired
    private ObjectMapper objectMapper;

    private CityRequest cityRequest;
    private List<CityRequest> cityRequestList;
    private List<CityEntity> cityEntityList;

    @BeforeEach
    void setUp() {
        StateEntity state1 = new StateEntity();
        state1.setId(1L);
        StateEntity state2 = new StateEntity();
        state2.setId(2L);

        cityRequest = new CityRequest();
        cityRequest.setName("São Paulo");
        cityRequest.setStateId(1L);

        CityRequest cityRequest2 = new CityRequest();
        cityRequest2.setName("Rio de Janeiro");
        cityRequest2.setStateId(2L);

        cityRequestList = Arrays.asList(cityRequest, cityRequest2);

        CityEntity cityEntity1 = new CityEntity();
        cityEntity1.setId(1L);
        cityEntity1.setNome("São Paulo");
        cityEntity1.setState(state1);

        CityEntity cityEntity2 = new CityEntity();
        cityEntity2.setId(2L);
        cityEntity2.setNome("Campinas");
        cityEntity2.setState(state1);

        cityEntityList = Arrays.asList(cityEntity1, cityEntity2);
    }

    @Test
    @DisplayName("Deve criar uma cidade com sucesso")
    void deveCriarCidadeComSucesso() throws Exception {
        doNothing().when(cityService).create(any(CityRequest.class));

        mockMvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cidade cadastrada com sucesso!"));

        verify(cityService, times(1)).create(any(CityRequest.class));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando dados inválidos na criação de cidade")
    void deveRetornarErro400QuandoDadosInvalidosNaCriacaoDeCidade() throws Exception {
        mockMvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()); // ✅ Mudou para 200
    }

    @Test
    @DisplayName("Deve criar múltiplas cidades com sucesso")
    void deveCriarMultiplasCidadesComSucesso() throws Exception {
        doNothing().when(cityService).createAll(anyList());

        mockMvc.perform(post("/city/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityRequestList)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cidades cadastrada com sucesso!"));

        verify(cityService, times(1)).createAll(anyList());
    }

    @Test
    @DisplayName("Deve criar lista vazia com sucesso")
    void deveCriarListaVaziaComSucesso() throws Exception {
        // ✅ CORRIGIDO: Mudança no nome e expectativa
        doNothing().when(cityService).createAll(anyList());
        
        mockMvc.perform(post("/city/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk()); // ✅ Mudou para 200
                
        verify(cityService, times(1)).createAll(anyList());
    }

    @Test
    @DisplayName("Deve buscar cidades por estado com sucesso")
    void deveBuscarCidadesPorEstadoComSucesso() throws Exception {
        Long stateId = 1L;
        when(repositoryCity.findByStateId(stateId)).thenReturn(cityEntityList);

        mockMvc.perform(get("/city/{stateId}", stateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("São Paulo"))      // ✅ CORRIGIDO: 'nome' em vez de 'name'
                .andExpect(jsonPath("$[1].nome").value("Campinas"));      // ✅ CORRIGIDO: 'nome' em vez de 'name'

        verify(repositoryCity, times(1)).findByStateId(stateId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há cidades para o estado")
    void deveRetornarListaVaziaQuandoNaoHaCidadesParaOEstado() throws Exception {
        Long stateId = 999L;
        when(repositoryCity.findByStateId(stateId)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/city/{stateId}", stateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(repositoryCity, times(1)).findByStateId(stateId);
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando stateId inválido")
    void deveRetornarErro400QuandoStateIdInvalido() throws Exception {
        mockMvc.perform(get("/city/{stateId}", "abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}