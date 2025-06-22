package com.java.Invista.service;

import com.java.Invista.dto.request.ImovelRequest;
import com.java.Invista.entity.AddressEntity;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient; // <- ADICIONE SE USAR LENIENT

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ImovelService")
class ImovelServiceTest {

    @Mock
    private RepositoryImovel repositoryImovel;

    @Mock
    private RepositoryCity repositoryCity;

    @Mock
    private RepositoryOwner repositoryOwner;

    @Mock
    private RepositoryAddress repositoryAddress;

    @Mock
    private RepositoryUser repositoryUser;

    @Mock
    private CityService cityService;

    @Mock
    private AddressService addressService;

    @Mock
    private ImovelRequest imovelRequest;

    // NÃO USA @InjectMocks - Vamos criar manualmente
    private ImovelService imovelService;

    private ImovelEntity imovelEntity;
    private AddressEntity addressEntity;
    private CityEntity cityEntity;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA O SERVICE MANUALMENTE
        imovelService = new ImovelService(repositoryImovel);

        // INJETA AS DEPENDÊNCIAS @Autowired USANDO REFLECTION
        ReflectionTestUtils.setField(imovelService, "repositoryCity", repositoryCity);
        ReflectionTestUtils.setField(imovelService, "repositoryOwner", repositoryOwner);
        ReflectionTestUtils.setField(imovelService, "repositoryAddress", repositoryAddress);
        ReflectionTestUtils.setField(imovelService, "repositoryUser", repositoryUser);
        ReflectionTestUtils.setField(imovelService, "cityService", cityService);
        ReflectionTestUtils.setField(imovelService, "addressService", addressService);

        // CRIA CIDADE
        cityEntity = new CityEntity();
        cityEntity.setId(1L);
        cityEntity.setNome("São Paulo");

        // CRIA ENDEREÇO
        addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua das Flores");
        addressEntity.setNumber(123);
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCep(12345678);
        addressEntity.setCity(cityEntity);

        // CRIA IMÓVEL
        imovelEntity = new ImovelEntity();
        imovelEntity.setId_imovel(1L);
        imovelEntity.setNome_imovel("Apartamento Centro");
        imovelEntity.setValueRegistration(300000.0);
        imovelEntity.setDate_Value(LocalDate.now());
        imovelEntity.setAdress(addressEntity);

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar imóvel")
    void deveCriarImovel() {
        System.out.println("🏠 Testando criação de imóvel...");

        // CONFIGURA MOCKS - USA ANY() PARA EVITAR ARGUMENT MISMATCH
        when(imovelRequest.toModel(any(), any(), any(), any())).thenReturn(imovelEntity);
        when(repositoryImovel.save(imovelEntity)).thenReturn(imovelEntity);

        // EXECUTA
        ImovelEntity result = imovelService.create(imovelRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId_imovel()).isEqualTo(1L);
        assertThat(result.getNome_imovel()).isEqualTo("Apartamento Centro");

        // VERIFICA CHAMADAS
        verify(imovelRequest, times(1)).toModel(any(), any(), any(), any());
        verify(repositoryImovel, times(1)).save(imovelEntity);

        System.out.println("✅ Criação funcionou!");
    }

    @Test
    @DisplayName("Deve listar todos os imóveis")
    void deveListarTodosOsImoveis() {
        System.out.println("📋 Testando listagem de imóveis...");

        // CRIA LISTA DE IMÓVEIS
        ImovelEntity imovel2 = new ImovelEntity();
        imovel2.setId_imovel(2L);
        imovel2.setNome_imovel("Casa Praia");

        List<ImovelEntity> imoveis = Arrays.asList(imovelEntity, imovel2);

        // CONFIGURA MOCK
        when(repositoryImovel.findAll()).thenReturn(imoveis);

        // EXECUTA
        List<ImovelEntity> result = imovelService.list();

        // VERIFICA RESULTADO
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNome_imovel()).isEqualTo("Apartamento Centro");
        assertThat(result.get(1).getNome_imovel()).isEqualTo("Casa Praia");

        // VERIFICA CHAMADA
        verify(repositoryImovel, times(1)).findAll();

        System.out.println("✅ Listagem funcionou!");
    }

    @Test
    @DisplayName("Deve listar apenas nomes dos imóveis")
    void deveListarApenasNomesDoImoveis() {
        System.out.println("📝 Testando listagem de nomes...");

        // CRIA LISTA DE IMÓVEIS
        ImovelEntity imovel2 = new ImovelEntity();
        imovel2.setNome_imovel("Casa Praia");

        List<ImovelEntity> imoveis = Arrays.asList(imovelEntity, imovel2);

        // CONFIGURA MOCK
        when(repositoryImovel.findAll()).thenReturn(imoveis);

        // EXECUTA
        List<String> result = imovelService.listImoveis();

        // VERIFICA RESULTADO
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly("Apartamento Centro", "Casa Praia");

        System.out.println("✅ Listagem de nomes funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas nome e valor")
    void deveAtualizarApenasNomeEValor() {
        System.out.println("✏️ Testando atualização simples...");

        // CRIA REQUEST SEM DADOS DE ENDEREÇO
        ImovelRequest updateRequest = mock(ImovelRequest.class);
        when(updateRequest.getNomeImovel()).thenReturn("Novo Nome");
        when(updateRequest.getValueRegistration()).thenReturn(400000.0);
        // TODOS OS CAMPOS DE ENDEREÇO RETORNAM NULL
        when(updateRequest.getStreet()).thenReturn(null);
        when(updateRequest.getNeighborhood()).thenReturn(null);
        when(updateRequest.getNumber()).thenReturn(null);
        when(updateRequest.getCityId()).thenReturn(null);
        when(updateRequest.getCep()).thenReturn(null);

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(repositoryImovel.save(any(ImovelEntity.class))).thenReturn(imovelEntity);

        // EXECUTA
        ImovelEntity result = imovelService.update(1L, updateRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getNome_imovel()).isEqualTo("Novo Nome");
        assertThat(result.getValueRegistration()).isEqualTo(400000.0);
        assertThat(result.getDate_Value()).isEqualTo(LocalDate.now());

        // VERIFICA CHAMADAS
        verify(repositoryImovel, times(1)).findById(1L);
        verify(repositoryImovel, times(1)).save(imovelEntity);
        
        // VERIFICA QUE NÃO CHAMOU CITY SERVICE (porque cityId é null)
        verify(cityService, never()).getById(anyLong());

        System.out.println("✅ Atualização simples funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar endereço do imóvel")
    void deveAtualizarEnderecoDoImovel() {
        System.out.println("🏠 Testando atualização de endereço...");

        // CRIA REQUEST COM DADOS DE ENDEREÇO
        ImovelRequest updateRequest = mock(ImovelRequest.class);
        when(updateRequest.getNomeImovel()).thenReturn(null); // Não atualiza nome
        when(updateRequest.getValueRegistration()).thenReturn(null); // Não atualiza valor
        when(updateRequest.getStreet()).thenReturn("Nova Rua");
        when(updateRequest.getNeighborhood()).thenReturn("Novo Bairro");
        when(updateRequest.getNumber()).thenReturn(456);
        when(updateRequest.getCep()).thenReturn(87654321);
        when(updateRequest.getCityId()).thenReturn(2L);

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(cityService.getById(2L)).thenReturn(Optional.of(cityEntity));
        when(repositoryImovel.save(any(ImovelEntity.class))).thenReturn(imovelEntity);

        // EXECUTA
        ImovelEntity result = imovelService.update(1L, updateRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getAdress().getStreet()).isEqualTo("Nova Rua");
        assertThat(result.getAdress().getNeighborhood()).isEqualTo("Novo Bairro");
        assertThat(result.getAdress().getNumber()).isEqualTo(456);
        assertThat(result.getAdress().getCep()).isEqualTo(87654321);

        // VERIFICA CHAMADAS
        verify(cityService, times(1)).getById(2L);
        verify(addressService, times(1)).update(eq(1L), any(AddressEntity.class));

        System.out.println("✅ Atualização de endereço funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar imóvel inexistente")
    void deveLancarExcecaoAoAtualizarImovelInexistente() {
        System.out.println("❌ Testando atualização de imóvel inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryImovel.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> imovelService.update(999L, imovelRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro: ID do imóvel não encontrado!");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryImovel, never()).save(any(ImovelEntity.class));

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve deletar imóvel e endereço não utilizado")
    void deveDeletarImovelEEnderecoNaoUtilizado() {
        System.out.println("🗑️ Testando deleção com endereço não utilizado...");

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(repositoryOwner.existsByAddressId(1L)).thenReturn(false); // Endereço não usado
        doNothing().when(repositoryImovel).deleteById(1L);
        doNothing().when(addressService).delete(1L);

        // EXECUTA
        String result = imovelService.delete(1L);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Imovel removido com sucesso!");

        // VERIFICA CHAMADAS
        verify(repositoryImovel, times(1)).deleteById(1L);
        verify(repositoryOwner, times(1)).existsByAddressId(1L);
        verify(addressService, times(1)).delete(1L);

        System.out.println("✅ Deleção com endereço funcionou!");
    }

    @Test
    @DisplayName("Deve deletar imóvel mas manter endereço em uso")
    void deveDeletarImovelMasManterEnderecoEmUso() {
        System.out.println("🗑️ Testando deleção com endereço em uso...");

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(repositoryOwner.existsByAddressId(1L)).thenReturn(true); // Endereço usado por owner
        doNothing().when(repositoryImovel).deleteById(1L);

        // EXECUTA
        String result = imovelService.delete(1L);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Imovel removido com sucesso!");

        // VERIFICA CHAMADAS
        verify(repositoryImovel, times(1)).deleteById(1L);
        verify(repositoryOwner, times(1)).existsByAddressId(1L);
        verify(addressService, never()).delete(anyLong()); // Endereço NÃO foi deletado

        System.out.println("✅ Deleção preservando endereço funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar imóvel inexistente")
    void deveLancarExcecaoAoDeletarImovelInexistente() {
        System.out.println("❌ Testando deleção de imóvel inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryImovel.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> imovelService.delete(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro: ID do imóvel não encontrado!");

        // VERIFICA QUE DELETE NÃO FOI CHAMADO
        verify(repositoryImovel, never()).deleteById(anyLong());

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve buscar imóvel por ID")
    void deveBuscarImovelPorId() {
        System.out.println("🔍 Testando busca por ID...");

        // CONFIGURA MOCK
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));

        // EXECUTA
        ImovelEntity result = imovelService.buscarImovelPorId(1L);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId_imovel()).isEqualTo(1L);
        assertThat(result.getNome_imovel()).isEqualTo("Apartamento Centro");

        // VERIFICA CHAMADA
        verify(repositoryImovel, times(1)).findById(1L);

        System.out.println("✅ Busca por ID funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar imóvel inexistente")
    void deveLancarExcecaoAoBuscarImovelInexistente() {
        System.out.println("❌ Testando busca de imóvel inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryImovel.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> imovelService.buscarImovelPorId(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Imóvel não encontrado com o ID: 999");

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve contar total de imóveis")
    void deveContarTotalDeImoveis() {
        System.out.println("🔢 Testando contagem de imóveis...");

        // CONFIGURA MOCK
        when(repositoryImovel.count()).thenReturn(5L);

        // EXECUTA
        Long result = imovelService.contarImoveis();

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo(5L);

        // VERIFICA CHAMADA
        verify(repositoryImovel, times(1)).count();

        System.out.println("✅ Contagem funcionou!");
    }

    @Test
    @DisplayName("Deve calcular valor total dos imóveis")
    void deveCalcularValorTotalDosImoveis() {
        System.out.println("💰 Testando cálculo de valor total...");

        // CONFIGURA MOCK
        when(repositoryImovel.somarValoresTotaisImoveisAtivos()).thenReturn(1500000.0);

        // EXECUTA
        Double result = imovelService.calcularValorTotalImoveis();

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo(1500000.0);

        // VERIFICA CHAMADA
        verify(repositoryImovel, times(1)).somarValoresTotaisImoveisAtivos();

        System.out.println("✅ Cálculo de valor total funcionou!");
    }

    @Test
    @DisplayName("Deve retornar zero quando valor total é null")
    void deveRetornarZeroQuandoValorTotalENull() {
        System.out.println("💰 Testando valor total null...");

        // CONFIGURA MOCK PARA RETORNAR NULL
        when(repositoryImovel.somarValoresTotaisImoveisAtivos()).thenReturn(null);

        // EXECUTA
        Double result = imovelService.calcularValorTotalImoveis();

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo(0.0);

        System.out.println("✅ Valor null tratado como zero!");
    }

    @Test
    @DisplayName("Não deve atualizar nome com string vazia")
    void naoDeveAtualizarNomeComStringVazia() {
        System.out.println("🚫 Testando nome vazio...");

        // CRIA REQUEST COM NOME VAZIO E SEM ENDEREÇO
        ImovelRequest updateRequest = mock(ImovelRequest.class);
        when(updateRequest.getNomeImovel()).thenReturn(""); // String vazia
        when(updateRequest.getValueRegistration()).thenReturn(null);
        // TODOS OS CAMPOS DE ENDEREÇO NULL
        when(updateRequest.getStreet()).thenReturn(null);
        when(updateRequest.getNeighborhood()).thenReturn(null);
        when(updateRequest.getNumber()).thenReturn(null);
        when(updateRequest.getCityId()).thenReturn(null);
        when(updateRequest.getCep()).thenReturn(null);

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(repositoryImovel.save(any(ImovelEntity.class))).thenReturn(imovelEntity);

        // EXECUTA
        ImovelEntity result = imovelService.update(1L, updateRequest);

        // VERIFICA QUE O NOME NÃO FOI ALTERADO
        assertThat(result.getNome_imovel()).isEqualTo("Apartamento Centro"); // Nome original

        System.out.println("✅ String vazia não atualizou!");
    }

    @Test
    @DisplayName("Deve lançar exceção quando cidade não encontrada na atualização")
    void deveLancarExcecaoQuandoCidadeNaoEncontradaNaAtualizacao() {
        System.out.println("❌ Testando cidade não encontrada...");

        // CRIA REQUEST COM CIDADE INEXISTENTE - SÓ O ESSENCIAL
        ImovelRequest updateRequest = mock(ImovelRequest.class);
        when(updateRequest.getCityId()).thenReturn(999L); // SÓ ISSO É NECESSÁRIO!

        // CONFIGURA MOCKS
        when(repositoryImovel.findById(1L)).thenReturn(Optional.of(imovelEntity));
        when(cityService.getById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> imovelService.update(1L, updateRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Cidade não encontrada");

        System.out.println("✅ Exceção de cidade funcionou!");
    }
}