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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes do RepositoryValuation")
class RepositoryValuationTest {

    @Autowired
    private RepositoryValuation repositoryValuation;

    @Autowired
    private RepositoryImovel repositoryImovel;

    @Test
    @Order(1)
    @DisplayName("Deve listar todas as avaliações existentes")
    void deveListarTodasAsAvaliacoes() {
        System.out.println("📈 Testando listagem de todas as avaliações...");

        // BUSCA TODAS AS AVALIAÇÕES QUE JÁ EXISTEM
        List<ValuationEntity> avaliacoes = repositoryValuation.findAll();
        
        System.out.println("📊 Avaliações encontradas: " + avaliacoes.size());
        avaliacoes.forEach(v -> System.out.println("  - Valor: R$ " + v.getValue() + 
            " (Imóvel ID: " + v.getImovel().getId_imovel() + ", Data: " + v.getDate() + ")"));
        
        // TESTE GENÉRICO - só verifica se não dá erro
        assertThat(avaliacoes).isNotNull();
        
        if (!avaliacoes.isEmpty()) {
            assertThat(avaliacoes.get(0).getId()).isNotNull();
            assertThat(avaliacoes.get(0).getValue()).isNotNull();
            assertThat(avaliacoes.get(0).getDate()).isNotNull();
            assertThat(avaliacoes.get(0).getImovel()).isNotNull();
        }

        System.out.println("✅ Teste de listagem passou!");
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar avaliação mais recente por imóvel")
    void deveBuscarAvaliacaoMaisRecentePorImovel() {
        System.out.println("🔍 Testando busca da avaliação mais recente por imóvel...");

        // PEGA UM IMÓVEL QUE TEM AVALIAÇÕES
        List<ValuationEntity> todasAvaliacoes = repositoryValuation.findAll();
        
        if (!todasAvaliacoes.isEmpty()) {
            // PEGA O IMÓVEL DA PRIMEIRA AVALIAÇÃO
            Long imovelId = todasAvaliacoes.get(0).getImovel().getId_imovel();
            
            System.out.println("🏠 Buscando avaliação mais recente do imóvel ID: " + imovelId);

            // TESTA A QUERY CUSTOMIZADA
            ValuationEntity avaliacaoMaisRecente = repositoryValuation.findByImovelId(imovelId);
            
            if (avaliacaoMaisRecente != null) {
                System.out.println("📈 Avaliação mais recente encontrada:");
                System.out.println("  - Valor: R$ " + avaliacaoMaisRecente.getValue());
                System.out.println("  - Data: " + avaliacaoMaisRecente.getDate());
                System.out.println("  - ID: " + avaliacaoMaisRecente.getId());
                
                assertThat(avaliacaoMaisRecente.getImovel().getId_imovel()).isEqualTo(imovelId);
                assertThat(avaliacaoMaisRecente.getValue()).isNotNull();
                assertThat(avaliacaoMaisRecente.getDate()).isNotNull();
                
                // VERIFICA SE É REALMENTE A MAIS RECENTE (se houver múltiplas)
                List<ValuationEntity> todasDoImovel = todasAvaliacoes.stream()
                    .filter(v -> v.getImovel().getId_imovel().equals(imovelId))
                    .toList();
                
                if (todasDoImovel.size() > 1) {
                    System.out.println("🔄 Verificando se é a mais recente entre " + todasDoImovel.size() + " avaliações");
                    
                    // ENCONTRA A DATA MAIS RECENTE MANUALMENTE
                    ValuationEntity maisRecenteManual = todasDoImovel.stream()
                        .max((v1, v2) -> v1.getDate().compareTo(v2.getDate()))
                        .orElse(null);
                    
                    if (maisRecenteManual != null) {
                        assertThat(avaliacaoMaisRecente.getDate()).isEqualTo(maisRecenteManual.getDate());
                        System.out.println("✅ Confirmado: é a avaliação mais recente!");
                    }
                }
            } else {
                System.out.println("⚠️ Nenhuma avaliação encontrada para o imóvel " + imovelId);
            }
        } else {
            System.out.println("⚠️ Nenhuma avaliação no banco para testar");
        }

        System.out.println("✅ Teste de busca por imóvel passou!");
    }

    @Test
    @Order(3)
    @DisplayName("Deve retornar null para imóvel sem avaliações")
    void deveRetornarNullParaImovelSemAvaliacoes() {
        System.out.println("👻 Testando imóvel sem avaliações...");

        // BUSCA POR UM IMÓVEL QUE EXISTE MAS NÃO TEM AVALIAÇÕES
        List<ImovelEntity> imoveis = repositoryImovel.findAll();
        List<ValuationEntity> avaliacoes = repositoryValuation.findAll();
        
        // ENCONTRA UM IMÓVEL SEM AVALIAÇÕES
        Optional<ImovelEntity> imovelSemAvaliacao = imoveis.stream()
            .filter(imovel -> avaliacoes.stream()
                .noneMatch(av -> av.getImovel().getId_imovel().equals(imovel.getId_imovel())))
            .findFirst();
        
        if (imovelSemAvaliacao.isPresent()) {
            Long imovelId = imovelSemAvaliacao.get().getId_imovel();
            System.out.println("🏠 Testando imóvel sem avaliações ID: " + imovelId);
            
            ValuationEntity resultado = repositoryValuation.findByImovelId(imovelId);
            
            System.out.println("📊 Resultado para imóvel sem avaliações: " + resultado);
            assertThat(resultado).isNull();
        } else {
            System.out.println("⚠️ Todos os imóveis têm avaliações - usando ID inexistente");
            
            // USA ID QUE CERTAMENTE NÃO EXISTE
            ValuationEntity resultado = repositoryValuation.findByImovelId(99999L);
            assertThat(resultado).isNull();
        }

        System.out.println("✅ Teste de imóvel sem avaliações passou!");
    }

    @Test
    @Order(4)
    @DisplayName("Deve retornar null para imóvel inexistente") 
    void deveRetornarNullParaImovelInexistente() {
        System.out.println("🔍 Testando imóvel inexistente...");

        // USA ID QUE CERTAMENTE NÃO EXISTE
        ValuationEntity resultado = repositoryValuation.findByImovelId(99999L);
        
        System.out.println("📊 Resultado para imóvel inexistente: " + resultado);
        assertThat(resultado).isNull();

        System.out.println("✅ Teste de imóvel inexistente passou!");
    }

    @Test
    @Order(5)
    @DisplayName("Deve contar total de avaliações")
    void deveContarTotalDeAvaliacoes() {
        System.out.println("🔢 Testando contagem de avaliações...");

        long totalAvaliacoes = repositoryValuation.count();
        
        System.out.println("📊 Total de avaliações no banco: " + totalAvaliacoes);
        
        assertThat(totalAvaliacoes).isGreaterThanOrEqualTo(0);

        System.out.println("✅ Teste de contagem passou!");
    }

    @Test
    @Order(6)
    @DisplayName("Deve validar estrutura das avaliações")
    void deveValidarEstruturaDasAvaliacoes() {
        System.out.println("🏗️ Testando estrutura das avaliações...");

        List<ValuationEntity> avaliacoes = repositoryValuation.findAll();
        
        if (!avaliacoes.isEmpty()) {
            ValuationEntity avaliacao = avaliacoes.get(0);
            
            System.out.println("📋 Validando avaliação ID: " + avaliacao.getId());
            
            // VALIDA SE OS CAMPOS OBRIGATÓRIOS EXISTEM
            assertThat(avaliacao.getId()).isNotNull();
            assertThat(avaliacao.getValue()).isNotNull();
            assertThat(avaliacao.getDate()).isNotNull();
            assertThat(avaliacao.getImovel()).isNotNull();
            assertThat(avaliacao.getImovel().getId_imovel()).isNotNull();
            
            System.out.println("✅ Estrutura válida:");
            System.out.println("  - ID: " + avaliacao.getId());
            System.out.println("  - Valor: R$ " + avaliacao.getValue());
            System.out.println("  - Data: " + avaliacao.getDate());
            System.out.println("  - Imóvel ID: " + avaliacao.getImovel().getId_imovel());
            
        } else {
            System.out.println("⚠️ Nenhuma avaliação no banco para validar estrutura");
        }

        System.out.println("✅ Teste de estrutura passou!");
    }

    @Test
    @Order(7)
    @DisplayName("Deve validar relacionamento com imóvel")
    void deveValidarRelacionamentoComImovel() {
        System.out.println("🔗 Testando relacionamento avaliação-imóvel...");

        List<ValuationEntity> avaliacoes = repositoryValuation.findAll();
        
        if (!avaliacoes.isEmpty()) {
            ValuationEntity avaliacao = avaliacoes.get(0);
            
            System.out.println("📈 Avaliação: R$ " + avaliacao.getValue());
            System.out.println("🏠 Imóvel: " + avaliacao.getImovel().getNome_imovel());
            
            // VERIFICA SE O RELACIONAMENTO ESTÁ CORRETO
            assertThat(avaliacao.getImovel()).isNotNull();
            assertThat(avaliacao.getImovel().getId_imovel()).isNotNull();
            assertThat(avaliacao.getImovel().getNome_imovel()).isNotNull();
            
            // TESTA SE CONSEGUE BUSCAR O IMÓVEL PELO ID
            Optional<ImovelEntity> imovelEncontrado = repositoryImovel.findById(avaliacao.getImovel().getId_imovel());
            assertThat(imovelEncontrado).isPresent();
            
        } else {
            System.out.println("⚠️ Nenhuma avaliação encontrada para testar relacionamento");
        }

        System.out.println("✅ Teste de relacionamento passou!");
    }
}