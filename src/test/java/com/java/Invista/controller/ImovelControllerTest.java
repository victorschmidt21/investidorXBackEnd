package com.java.Invista.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.Invista.dto.request.ImovelRequest;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.repository.RepositoryImovel;
import com.java.Invista.service.ImovelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImovelController.class)
@DisplayName("Testes do ImovelController")
class ImovelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImovelService imovelService;

    @MockBean
    private RepositoryImovel repositoryImovel;

    @Autowired
    private ObjectMapper objectMapper;

    private ImovelEntity imovelEntity;
    private ImovelRequest imovelRequest;

    @BeforeEach
    void setUp() {
        imovelEntity = new ImovelEntity();
        imovelEntity.setId_imovel(1L);
        imovelEntity.setNome_imovel("Casa Teste");
        imovelEntity.setValueRegistration(100000.0);
        imovelEntity.setDate_Value(LocalDate.of(2024, 6, 20));
        imovelEntity.setAtivo(true);

        imovelRequest = new ImovelRequest();
    }

    @Test
    @DisplayName("Deve criar um imóvel com sucesso")
    void deveCriarImovelComSucesso() throws Exception {
        when(imovelService.create(any(ImovelRequest.class))).thenReturn(imovelEntity);

        mockMvc.perform(post("/imovel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imovelRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_imovel").value(1L))
                .andExpect(jsonPath("$.nome_imovel").value("Casa Teste"));

        verify(imovelService, times(1)).create(any(ImovelRequest.class));
    }

    @Test
    @DisplayName("Deve atualizar um imóvel com sucesso")
    void deveAtualizarImovelComSucesso() throws Exception {
        when(imovelService.update(eq(1L), any(ImovelRequest.class))).thenReturn(imovelEntity);

        mockMvc.perform(put("/imovel/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imovelRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_imovel").value(1L))
                .andExpect(jsonPath("$.nome_imovel").value("Casa Teste"));

        verify(imovelService, times(1)).update(eq(1L), any(ImovelRequest.class));
    }

    @Test
    @DisplayName("Deve listar todos os imóveis")
    void deveListarTodosImoveis() throws Exception {
        when(imovelService.list()).thenReturn(Arrays.asList(imovelEntity));

        mockMvc.perform(get("/imovel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_imovel").value(1L))
                .andExpect(jsonPath("$[0].nome_imovel").value("Casa Teste"));

        verify(imovelService, times(1)).list();
    }

    @Test
    @DisplayName("Deve listar nomes dos imóveis")
    void deveListarNomesDosImoveis() throws Exception {
        when(imovelService.listImoveis()).thenReturn(Arrays.asList("Casa Teste", "Apartamento 2"));

        mockMvc.perform(get("/imovel/nomes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Casa Teste"))
                .andExpect(jsonPath("$[1]").value("Apartamento 2"));

        verify(imovelService, times(1)).listImoveis();
    }

    @Test
    @DisplayName("Deve buscar imóveis por usuário")
    void deveBuscarImoveisPorUsuario() throws Exception {
        when(repositoryImovel.findByUser("user123")).thenReturn(Arrays.asList(imovelEntity));

        mockMvc.perform(get("/imovel/user123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_imovel").value(1L))
                .andExpect(jsonPath("$[0].nome_imovel").value("Casa Teste"));

        verify(repositoryImovel, times(1)).findByUser("user123");
    }

    @Test
    @DisplayName("Deve deletar imóvel com sucesso")
    void deveDeletarImovelComSucesso() throws Exception {
        when(imovelService.delete(1L)).thenReturn("Imovel removido com sucesso!");

        mockMvc.perform(delete("/imovel/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Imovel removido com sucesso!"));

        verify(imovelService, times(1)).delete(1L);
    }
}