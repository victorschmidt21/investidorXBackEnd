package com.java.Invista.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.Invista.entity.UserEntity;
import com.java.Invista.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("Testes do UserController")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setUsername("Maria Teste");
        // Preencha outros campos obrigatórios se houver
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() throws Exception {
        when(userService.create(any(UserEntity.class))).thenReturn(userEntity);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("Maria Teste"));

        verify(userService, times(1)).create(any(UserEntity.class));
    }
}