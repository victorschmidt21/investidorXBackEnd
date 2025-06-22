package com.java.Invista.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.Invista.dto.request.OwnerRequest;
import com.java.Invista.entity.OwnerEntity;
import com.java.Invista.service.OwnerService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
@DisplayName("Testes do OwnerController")
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    private OwnerEntity ownerEntity;
    private OwnerRequest ownerRequest;

    @BeforeEach
    void setUp() {
        ownerEntity = new OwnerEntity();
        ownerEntity.setId(1L);
        ownerEntity.setName("João da Silva");

        ownerRequest = new OwnerRequest();
        ownerRequest.setName("João da Silva");
        ownerRequest.setPhone("11999999999");

    }

    @Test
    @DisplayName("Deve criar um proprietário com sucesso")
    void deveCriarOwnerComSucesso() throws Exception {
        when(ownerService.create(any(OwnerRequest.class))).thenReturn(ownerEntity);

        mockMvc.perform(post("/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("João da Silva"));

        verify(ownerService, times(1)).create(any(OwnerRequest.class));
    }

    @Test
    @DisplayName("Deve listar todos os proprietários")
    void deveListarTodosOwners() throws Exception {
        when(ownerService.getOwners()).thenReturn(Arrays.asList(ownerEntity));

        mockMvc.perform(get("/owner")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("João da Silva"));

        verify(ownerService, times(1)).getOwners();
    }

    @Test
    @DisplayName("Deve atualizar um proprietário com sucesso")
    void deveAtualizarOwnerComSucesso() throws Exception {
        when(ownerService.update(eq(1L), any(OwnerRequest.class))).thenReturn(ownerEntity);

        mockMvc.perform(put("/owner/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("João da Silva"));

        verify(ownerService, times(1)).update(eq(1L), any(OwnerRequest.class));
    }

    @Test
    @DisplayName("Deve buscar proprietário por usuário")
    void deveBuscarOwnerPorUsuario() throws Exception {
        when(ownerService.getByUserId("user123")).thenReturn(Arrays.asList(ownerEntity));

        mockMvc.perform(get("/owner/user123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("João da Silva"));

        verify(ownerService, times(1)).getByUserId("user123");
    }

    @Test
    @DisplayName("Deve deletar proprietário com sucesso")
    void deveDeletarOwnerComSucesso() throws Exception {
        when(ownerService.delete(1L)).thenReturn("Proprietário removido com sucesso!");

        mockMvc.perform(delete("/owner/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Proprietário removido com sucesso!"));

        verify(ownerService, times(1)).delete(1L);
    }
}