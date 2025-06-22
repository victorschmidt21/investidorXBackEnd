package com.java.Invista.service;

import com.java.Invista.dto.request.OwnerRequest;
import com.java.Invista.entity.AddressEntity;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.OwnerEntity;
import com.java.Invista.entity.UserEntity;
import com.java.Invista.repository.RepositoryAddress;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.repository.RepositoryOwner;
import com.java.Invista.repository.RepositoryUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do OwnerService")
class OwnerServiceTest {

    @Mock
    private RepositoryOwner repositoryOwner;

    @Mock
    private RepositoryCity repositoryCity;

    @Mock
    private RepositoryAddress repositoryAddress;

    @Mock
    private RepositoryUser repositoryUser;

    @Mock
    private AddressService addressService;

    @Mock
    private OwnerRequest ownerRequest;

    // NÃO USA @InjectMocks - Vamos criar manualmente
    private OwnerService ownerService;

    private OwnerEntity ownerEntity;
    private AddressEntity addressEntity;
    private CityEntity cityEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA O SERVICE MANUALMENTE
        ownerService = new OwnerService(repositoryOwner);

        // INJETA AS DEPENDÊNCIAS @Autowired USANDO REFLECTION
        ReflectionTestUtils.setField(ownerService, "repositoryCity", repositoryCity);
        ReflectionTestUtils.setField(ownerService, "repositoryAddress", repositoryAddress);
        ReflectionTestUtils.setField(ownerService, "repositoryUser", repositoryUser);
        ReflectionTestUtils.setField(ownerService, "addressService", addressService);

        // CRIA USUÁRIO - CORRIGE O MÉTODO
        userEntity = new UserEntity();
        userEntity.setId("user123");
        // userEntity.setName("João Silva"); // <- REMOVE ESTA LINHA
        // OU USE O MÉTODO CORRETO:
        // userEntity.setUsername("João Silva"); // SE FOR setUsername()
        // userEntity.setNome("João Silva"); // SE FOR setNome()

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

        // CRIA PROPRIETÁRIO
        ownerEntity = new OwnerEntity();
        ownerEntity.setId(1L);
        ownerEntity.setName("Maria Santos");
        ownerEntity.setEmail("maria@email.com");
        ownerEntity.setPhone("11999999999");
        ownerEntity.setCpf_cnpj("12345678901");
        ownerEntity.setAtivo(true);
        ownerEntity.setAddress(addressEntity);
        ownerEntity.setUser(userEntity);

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve criar proprietário")
    void deveCriarProprietario() {
        System.out.println("👤 Testando criação de proprietário...");

        // CONFIGURA MOCKS
        when(ownerRequest.toModel(repositoryAddress, repositoryCity, repositoryUser))
            .thenReturn(ownerEntity);
        when(repositoryOwner.save(ownerEntity)).thenReturn(ownerEntity);

        // EXECUTA
        OwnerEntity result = ownerService.create(ownerRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Maria Santos");
        assertThat(result.getEmail()).isEqualTo("maria@email.com");

        // VERIFICA CHAMADAS
        verify(ownerRequest, times(1)).toModel(repositoryAddress, repositoryCity, repositoryUser);
        verify(repositoryOwner, times(1)).save(ownerEntity);

        System.out.println("✅ Criação funcionou!");
    }

    @Test
    @DisplayName("Deve listar todos os proprietários")
    void deveListarTodosOsProprietarios() {
        System.out.println("📋 Testando listagem de proprietários...");

        // CRIA LISTA DE PROPRIETÁRIOS
        OwnerEntity owner2 = new OwnerEntity();
        owner2.setId(2L);
        owner2.setName("João Silva");

        List<OwnerEntity> owners = Arrays.asList(ownerEntity, owner2);

        // CONFIGURA MOCK
        when(repositoryOwner.findAll()).thenReturn(owners);

        // EXECUTA
        List<OwnerEntity> result = ownerService.getOwners();

        // VERIFICA RESULTADO
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Maria Santos");
        assertThat(result.get(1).getName()).isEqualTo("João Silva");

        // VERIFICA CHAMADA
        verify(repositoryOwner, times(1)).findAll();

        System.out.println("✅ Listagem funcionou!");
    }

    @Test
    @DisplayName("Deve fazer delete lógico do proprietário")
    void deveFazerDeleteLogicoDoProprietario() {
        System.out.println("🗑️ Testando delete lógico...");

        // CONFIGURA MOCKS
        when(repositoryOwner.findById(1L)).thenReturn(Optional.of(ownerEntity));
        when(repositoryOwner.save(any(OwnerEntity.class))).thenReturn(ownerEntity);

        // EXECUTA
        String result = ownerService.delete(1L);

        // VERIFICA RESULTADO
        assertThat(result).isEqualTo("Proprietario deletado com sucesso!");
        
        // CORRIGE O MÉTODO - TESTA DE ACORDO COM O QUE EXISTE:
        // assertThat(ownerEntity.isAtivo()).isFalse(); // <- REMOVE ESTA LINHA
        
        // USE UMA DESTAS OPÇÕES:
        assertThat(ownerEntity.getAtivo()).isFalse(); // SE FOR getAtivo()
        // OU
        // assertThat(ownerEntity.isActive()).isFalse(); // SE FOR isActive()

        // VERIFICA CHAMADAS
        verify(repositoryOwner, times(1)).findById(1L);
        verify(repositoryOwner, times(1)).save(ownerEntity);

        System.out.println("✅ Delete lógico funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar proprietário inexistente")
    void deveLancarExcecaoAoDeletarProprietarioInexistente() {
        System.out.println("❌ Testando delete de proprietário inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryOwner.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> ownerService.delete(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Proprietario não encontrado!");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryOwner, never()).save(any(OwnerEntity.class));

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar dados pessoais do proprietário")
    void deveAtualizarDadosPessoaisDoProprietario() {
        System.out.println("✏️ Testando atualização de dados pessoais...");

        // CRIA REQUEST SEM DADOS DE ENDEREÇO
        OwnerRequest updateRequest = mock(OwnerRequest.class);
        when(updateRequest.getName()).thenReturn("Novo Nome");
        when(updateRequest.getPhone()).thenReturn("11888888888");
        when(updateRequest.getEmail()).thenReturn("novo@email.com");
        when(updateRequest.getCpf_cnpj()).thenReturn("98765432109");
        when(updateRequest.getUserId()).thenReturn("newuser123");

        // TODOS OS CAMPOS DE ENDEREÇO NULL
        when(updateRequest.getStreet()).thenReturn(null);
        when(updateRequest.getNeighborhood()).thenReturn(null);
        when(updateRequest.getNumber()).thenReturn(null);
        when(updateRequest.getCityId()).thenReturn(null);
        when(updateRequest.getCep()).thenReturn(null);

        // CONFIGURA MOCKS
        when(repositoryOwner.findById(1L)).thenReturn(Optional.of(ownerEntity));
        when(repositoryUser.findById("newuser123")).thenReturn(Optional.of(userEntity));
        when(repositoryOwner.save(any(OwnerEntity.class))).thenReturn(ownerEntity);

        // EXECUTA
        OwnerEntity result = ownerService.update(1L, updateRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Novo Nome");
        assertThat(result.getPhone()).isEqualTo("11888888888");
        assertThat(result.getEmail()).isEqualTo("novo@email.com");
        assertThat(result.getCpf_cnpj()).isEqualTo("98765432109");

        // VERIFICA CHAMADAS
        verify(repositoryOwner, times(1)).findById(1L);
        verify(repositoryUser, times(1)).findById("newuser123");
        verify(repositoryOwner, times(1)).save(ownerEntity);

        System.out.println("✅ Atualização de dados pessoais funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar endereço do proprietário")
    void deveAtualizarEnderecoDoProprietario() {
        System.out.println("🏠 Testando atualização de endereço...");

        // CRIA REQUEST COM DADOS DE ENDEREÇO
        OwnerRequest updateRequest = mock(OwnerRequest.class);
        when(updateRequest.getStreet()).thenReturn("Nova Rua");
        when(updateRequest.getNeighborhood()).thenReturn("Novo Bairro");
        when(updateRequest.getNumber()).thenReturn(456);
        when(updateRequest.getCep()).thenReturn(87654321);
        when(updateRequest.getCityId()).thenReturn(2L);

        // DADOS PESSOAIS NULL
        when(updateRequest.getName()).thenReturn(null);
        when(updateRequest.getPhone()).thenReturn(null);
        when(updateRequest.getEmail()).thenReturn(null);
        when(updateRequest.getCpf_cnpj()).thenReturn(null);
        when(updateRequest.getUserId()).thenReturn(null);

        // CONFIGURA MOCKS
        when(repositoryOwner.findById(1L)).thenReturn(Optional.of(ownerEntity));
        when(repositoryCity.findById(2L)).thenReturn(Optional.of(cityEntity));
        when(repositoryOwner.save(any(OwnerEntity.class))).thenReturn(ownerEntity);

        // EXECUTA
        OwnerEntity result = ownerService.update(1L, updateRequest);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getAddress().getStreet()).isEqualTo("Nova Rua");
        assertThat(result.getAddress().getNeighborhood()).isEqualTo("Novo Bairro");
        assertThat(result.getAddress().getNumber()).isEqualTo(456);
        assertThat(result.getAddress().getCep()).isEqualTo(87654321);

        // VERIFICA CHAMADAS
        verify(repositoryCity, times(1)).findById(2L);
        verify(addressService, times(1)).update(eq(1L), any(AddressEntity.class));

        System.out.println("✅ Atualização de endereço funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas campos não nulos")
    void deveAtualizarApenasCamposNaoNulos() {
        System.out.println("🔧 Testando atualização parcial...");

        // CRIA REQUEST PARCIAL
        OwnerRequest partialRequest = mock(OwnerRequest.class);
        when(partialRequest.getName()).thenReturn("Só o Nome");
        when(partialRequest.getPhone()).thenReturn(null);
        when(partialRequest.getEmail()).thenReturn(null);
        when(partialRequest.getCpf_cnpj()).thenReturn(null);
        when(partialRequest.getUserId()).thenReturn(null);

        // TODOS OS CAMPOS DE ENDEREÇO NULL
        when(partialRequest.getStreet()).thenReturn(null);
        when(partialRequest.getNeighborhood()).thenReturn(null);
        when(partialRequest.getNumber()).thenReturn(null);
        when(partialRequest.getCityId()).thenReturn(null);
        when(partialRequest.getCep()).thenReturn(null);

        // CONFIGURA MOCKS
        when(repositoryOwner.findById(1L)).thenReturn(Optional.of(ownerEntity));
        when(repositoryOwner.save(any(OwnerEntity.class))).thenReturn(ownerEntity);

        // EXECUTA
        OwnerEntity result = ownerService.update(1L, partialRequest);

        // VERIFICA QUE APENAS O NOME FOI ALTERADO
        assertThat(result.getName()).isEqualTo("Só o Nome");
        assertThat(result.getPhone()).isEqualTo("11999999999"); // Valor original
        assertThat(result.getEmail()).isEqualTo("maria@email.com"); // Valor original

        System.out.println("✅ Atualização parcial funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar proprietário inexistente")
    void deveLancarExcecaoAoAtualizarProprietarioInexistente() {
        System.out.println("❌ Testando atualização de proprietário inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryOwner.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> ownerService.update(999L, ownerRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Proprietário não encontrado!");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryOwner, never()).save(any(OwnerEntity.class));

        System.out.println("✅ Exceção funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção quando cidade não encontrada na atualização")
    void deveLancarExcecaoQuandoCidadeNaoEncontradaNaAtualizacao() {
        System.out.println("❌ Testando cidade não encontrada...");

        // CRIA REQUEST COM CIDADE INEXISTENTE
        OwnerRequest updateRequest = mock(OwnerRequest.class);
        when(updateRequest.getCityId()).thenReturn(999L);

        // CONFIGURA MOCKS
        when(repositoryOwner.findById(1L)).thenReturn(Optional.of(ownerEntity));
        when(repositoryCity.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> ownerService.update(1L, updateRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Cidade não encontrada!");

        System.out.println("✅ Exceção de cidade funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado na atualização")
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoNaAtualizacao() {
        System.out.println("❌ Testando usuário não encontrado...");

        // CRIA REQUEST COM USUÁRIO INEXISTENTE
        OwnerRequest updateRequest = mock(OwnerRequest.class);
        when(updateRequest.getUserId()).thenReturn("inexistente");

        // TODOS OS CAMPOS DE ENDEREÇO NULL
        when(updateRequest.getStreet()).thenReturn(null);
        when(updateRequest.getNeighborhood()).thenReturn(null);
        when(updateRequest.getNumber()).thenReturn(null);
        when(updateRequest.getCityId()).thenReturn(null);
        when(updateRequest.getCep()).thenReturn(null);

        // CONFIGURA MOCKS
        when(repositoryOwner.findById(1L)).thenReturn(Optional.of(ownerEntity));
        when(repositoryUser.findById("inexistente")).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> ownerService.update(1L, updateRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuário não encontrado");

        System.out.println("✅ Exceção de usuário funcionou!");
    }

    @Test
    @DisplayName("Deve buscar proprietários ativos por userId")
    void deveBuscarProprietariosAtivosPorUserId() {
        System.out.println("🔍 Testando busca por userId...");

        // CRIA LISTA DE PROPRIETÁRIOS ATIVOS
        OwnerEntity owner2 = new OwnerEntity();
        owner2.setId(2L);
        owner2.setName("Outro Proprietário");
        owner2.setAtivo(true); // ESTE MÉTODO EXISTE

        List<OwnerEntity> activeOwners = Arrays.asList(ownerEntity, owner2);

        // CONFIGURA MOCK
        when(repositoryOwner.findByAtivoTrue("user123")).thenReturn(activeOwners);

        // EXECUTA
        List<OwnerEntity> result = ownerService.getByUserId("user123");

        // VERIFICA RESULTADO
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Maria Santos");
        assertThat(result.get(1).getName()).isEqualTo("Outro Proprietário");

        // VERIFICA CHAMADA
        verify(repositoryOwner, times(1)).findByAtivoTrue("user123");

        System.out.println("✅ Busca por userId funcionou!");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há proprietários ativos")
    void deveRetornarListaVaziaQuandoNaoHaProprietariosAtivos() {
        System.out.println("📝 Testando lista vazia...");

        // CONFIGURA MOCK PARA RETORNAR LISTA VAZIA
        when(repositoryOwner.findByAtivoTrue("user123")).thenReturn(Arrays.asList());

        // EXECUTA
        List<OwnerEntity> result = ownerService.getByUserId("user123");

        // VERIFICA RESULTADO
        assertThat(result).isEmpty();

        // VERIFICA CHAMADA
        verify(repositoryOwner, times(1)).findByAtivoTrue("user123");

        System.out.println("✅ Lista vazia tratada corretamente!");
    }

    @Test
    @DisplayName("Deve propagar exceção do toModel")
    void devePropagarExcecaoDoToModel() {
        System.out.println("❌ Testando exceção do toModel...");

        // CONFIGURA MOCK PARA LANÇAR EXCEÇÃO
        when(ownerRequest.toModel(repositoryAddress, repositoryCity, repositoryUser))
            .thenThrow(new RuntimeException("Erro na criação"));

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> ownerService.create(ownerRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro na criação");

        // VERIFICA QUE SAVE NÃO FOI CHAMADO
        verify(repositoryOwner, never()).save(any(OwnerEntity.class));

        System.out.println("✅ Exceção propagada!");
    }
}