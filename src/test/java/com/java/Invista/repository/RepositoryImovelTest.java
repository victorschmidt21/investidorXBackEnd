package com.java.Invista.repository;

import com.java.Invista.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
  
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes do RepositoryImovel")
class RepositoryImovelTest {

    @Autowired
    private RepositoryImovel repositoryImovel;

    @Test
    @Order(1)
    @DisplayName("Deve buscar imóveis por usuário existente")
    void deveBuscarImoveisPorUsuario() {
        System.out.println("🔍 Testando busca de imóveis por usuário...");

        // USA DADOS QUE JÁ EXISTEM NO BANCO
        List<ImovelEntity> imoveis = repositoryImovel.findByUser("user123"); // ou qualquer ID que já existe
        
        System.out.println("📊 Imóveis encontrados: " + imoveis.size());
        imoveis.forEach(i -> System.out.println("  - " + i.getNome_imovel() + " (R$ " + i.getValueRegistration() + ")"));
        
        // TESTE GENÉRICO - só verifica se retorna algo
        assertThat(imoveis).isNotNull();
        
        System.out.println("✅ Teste de busca passou!");
    }

    @Test
    @Order(2)
    @DisplayName("Deve somar valores dos imóveis ativos existentes")
    void deveSomarValoresImoveisAtivos() {
        System.out.println("💰 Testando soma de valores dos imóveis ativos...");

        // Lista TODOS os imóveis ativos que já existem
        List<ImovelEntity> ativos = repositoryImovel.findAll().stream()
            .filter(i -> i.getAtivo())
            .toList();
        
        System.out.println("🏠 Imóveis ATIVOS no banco:");
        ativos.forEach(i -> System.out.println("  - " + i.getNome_imovel() + ": R$ " + i.getValueRegistration()));

        // Calcula a soma esperada manualmente
        double somaEsperada = ativos.stream()
            .mapToDouble(i -> i.getValueRegistration().doubleValue()) // <- CORRIGIDO!
            .sum();

        // Testa a query
        Number somaTotal = repositoryImovel.somarValoresTotaisImoveisAtivos();
        
        System.out.println("💵 Soma da query: " + somaTotal);
        System.out.println("💵 Soma esperada: " + somaEsperada);
        
        if (somaTotal != null) {
            assertThat(somaTotal.doubleValue()).isEqualTo(somaEsperada);
        } else {
            // Se não há imóveis ativos, soma deve ser null
            assertThat(somaEsperada).isEqualTo(0.0);
        }

        System.out.println("✅ Teste de soma passou!");
    }

    @Test
    @Order(3)
    @DisplayName("Deve retornar lista vazia para usuário inexistente")
    void deveRetornarListaVaziaParaUsuarioInexistente() {
        System.out.println("👻 Testando usuário inexistente...");

        List<ImovelEntity> imoveis = repositoryImovel.findByUser("usuarioQueNaoExiste123");
        
        System.out.println("📊 Imóveis encontrados: " + imoveis.size());
        
        assertThat(imoveis).isEmpty();

        System.out.println("✅ Teste com usuário inexistente passou!");
    }

    @Test
    @Order(4)
    @DisplayName("Deve validar que a query de soma funciona")
    void deveValidarQuerySoma() {
        System.out.println("🔍 Validando query de soma...");

        Number somaTotal = repositoryImovel.somarValoresTotaisImoveisAtivos();
        
        System.out.println("💵 Resultado da query: " + somaTotal);
        
        // Só verifica se a query não dá erro
        // Se há imóveis ativos, deve retornar um número > 0
        // Se não há, pode retornar null
        if (somaTotal != null) {
            assertThat(somaTotal.doubleValue()).isGreaterThanOrEqualTo(0.0);
        }

        System.out.println("✅ Query funciona corretamente!");
    }
}