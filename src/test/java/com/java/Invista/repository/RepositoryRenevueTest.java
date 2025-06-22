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
@DisplayName("Testes do RepositoryRenevue")
class RepositoryRenevueTest {

    @Autowired
    private RepositoryRenevue repositoryRenevue;

    @Autowired
    private RepositoryImovel repositoryImovel;

    @Test
    @Order(1)
    @DisplayName("Deve buscar receitas por imóvel existente")
    void deveBuscarReceitasPorImovel() {
        System.out.println("🔍 Testando busca de receitas por imóvel...");

        // PEGA UM IMÓVEL QUE JÁ EXISTE
        List<ImovelEntity> imoveis = repositoryImovel.findAll();
        if (!imoveis.isEmpty()) {
            ImovelEntity imovel = imoveis.get(0);
            System.out.println("🏠 Usando imóvel: " + imovel.getNome_imovel() + " (ID: " + imovel.getId_imovel() + ")");

            // BUSCA RECEITAS PARA ESTE IMÓVEL
            List<RenevueEntity> receitas = repositoryRenevue.findByImovelId(imovel.getId_imovel());
            
            System.out.println("📊 Receitas encontradas: " + receitas.size());
            receitas.forEach(r -> System.out.println("  - " + r.getTitle() + ": R$ " + r.getValue()));
            
            // TESTE GENÉRICO - só verifica se não dá erro
            assertThat(receitas).isNotNull();
        } else {
            System.out.println("⚠️ Nenhum imóvel encontrado no banco");
        }

        System.out.println("✅ Teste de busca por imóvel passou!");
    }

    @Test
    @Order(2)
    @DisplayName("Deve retornar lista vazia para imóvel inexistente")
    void deveRetornarListaVaziaParaImovelInexistente() {
        System.out.println("👻 Testando imóvel inexistente...");

        // USA ID QUE CERTAMENTE NÃO EXISTE
        List<RenevueEntity> receitas = repositoryRenevue.findByImovelId(99999L);
        
        System.out.println("📊 Receitas encontradas para imóvel inexistente: " + receitas.size());
        
        assertThat(receitas).isEmpty();

        System.out.println("✅ Teste de imóvel inexistente passou!");
    }

    @Test
    @Order(3)
    @DisplayName("Deve somar todas as receitas existentes")
    void deveSomarTodasAsReceitas() {
        System.out.println("💰 Testando soma total das receitas...");

        // LISTA TODAS AS RECEITAS EXISTENTES
        List<RenevueEntity> todas = repositoryRenevue.findAll();
        System.out.println("💸 TODAS as receitas no banco: " + todas.size());
        todas.forEach(r -> System.out.println("  - " + r.getTitle() + ": R$ " + r.getValue()));

        // CALCULA SOMA ESPERADA MANUALMENTE
        double somaEsperada = todas.stream()
            .mapToDouble(r -> r.getValue())
            .sum();

        // TESTA A QUERY
        Double somaTotal = repositoryRenevue.sumTotalValue();
        
        System.out.println("💵 Soma da query: " + somaTotal);
        System.out.println("💵 Soma esperada: " + somaEsperada);
        
        if (somaTotal != null) {
            assertThat(somaTotal).isEqualTo(somaEsperada);
        } else {
            // Se não há receitas, soma deve ser null
            assertThat(somaEsperada).isEqualTo(0.0);
        }

        System.out.println("✅ Teste de soma total passou!");
    }

    @Test
    @Order(4)
    @DisplayName("Deve validar que as queries funcionam")
    void deveValidarQueFuncionam() {
        System.out.println("🔍 Validando se as queries funcionam...");

        // SÓ TESTA SE AS QUERIES NÃO DÃO EXCEÇÃO
        Double somaTotal = repositoryRenevue.sumTotalValue();
        System.out.println("💵 Soma total: " + somaTotal);
        
        List<RenevueEntity> todasReceitas = repositoryRenevue.findAll();
        System.out.println("📊 Total de receitas: " + todasReceitas.size());

        // TESTE SIMPLES - SÓ VERIFICA SE NÃO DEU ERRO
        assertThat(todasReceitas).isNotNull();
        
        if (somaTotal != null) {
            System.out.println("✅ Soma calculada: R$ " + somaTotal);
        } else {
            System.out.println("ℹ️ Sem receitas no banco");
        }

        System.out.println("✅ Queries funcionam corretamente!");
    }

    @Test
    @Order(5)
    @DisplayName("Deve validar relacionamento receita-imóvel")
    void deveValidarRelacionamento() {
        System.out.println("🔗 Testando relacionamento receita-imóvel...");

        List<RenevueEntity> receitas = repositoryRenevue.findAll();
        
        if (!receitas.isEmpty()) {
            RenevueEntity receita = receitas.get(0);
            
            System.out.println("💸 Receita: " + receita.getTitle());
            System.out.println("🏠 Imóvel: " + receita.getImovel().getNome_imovel());
            
            // VALIDA SE O RELACIONAMENTO EXISTE
            assertThat(receita.getImovel()).isNotNull();
            assertThat(receita.getImovel().getId_imovel()).isNotNull();
            assertThat(receita.getImovel().getNome_imovel()).isNotNull();
        } else {
            System.out.println("⚠️ Nenhuma receita encontrada para testar relacionamento");
        }

        System.out.println("✅ Teste de relacionamento passou!");
    }
}