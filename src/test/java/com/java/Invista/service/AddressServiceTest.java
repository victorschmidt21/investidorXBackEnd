package com.java.Invista.service;

import com.java.Invista.entity.AddressEntity;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.repository.RepositoryAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AddressService")
class AddressServiceTest {

    @Mock
    private RepositoryAddress repositoryAddress;

    @InjectMocks
    private AddressService addressService;

    private AddressEntity existingAddress;
    private AddressEntity updateData;
    private CityEntity city;

    @BeforeEach
    void setUp() {
        System.out.println("🏗️ Preparando dados de teste...");

        // CRIA CIDADE
        city = new CityEntity();
        city.setId(1L);
        city.setNome("São Paulo");

        // CRIA ENDEREÇO EXISTENTE
        existingAddress = new AddressEntity();
        existingAddress.setId(1L);
        existingAddress.setStreet("Rua das Flores");
        existingAddress.setNumber(123);
        existingAddress.setNeighborhood("Centro");
        existingAddress.setCep(12345678);
        existingAddress.setCity(city);

        // CRIA DADOS PARA ATUALIZAÇÃO
        updateData = new AddressEntity();

        System.out.println("✅ Dados preparados!");
    }

    @Test
    @DisplayName("Deve atualizar endereço com todos os campos")
    void deveAtualizarEnderecoComTodosOsCampos() {
        System.out.println("✏️ Testando atualização completa do endereço...");

        // CONFIGURA DADOS DE ATUALIZAÇÃO
        CityEntity newCity = new CityEntity();
        newCity.setId(2L);
        newCity.setNome("Rio de Janeiro");

        updateData.setStreet("Avenida Paulista");
        updateData.setNumber(456);
        updateData.setNeighborhood("Bela Vista");
        updateData.setCep(87654321);
        updateData.setCity(newCity);

        // CONFIGURA MOCKS
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(repositoryAddress.save(any(AddressEntity.class))).thenReturn(existingAddress);

        // EXECUTA
        AddressEntity result = addressService.update(1L, updateData);

        // VERIFICA RESULTADO
        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("Avenida Paulista");
        assertThat(result.getNumber()).isEqualTo(456);
        assertThat(result.getNeighborhood()).isEqualTo("Bela Vista");
        assertThat(result.getCity().getNome()).isEqualTo("Rio de Janeiro");

        // VERIFICA CHAMADAS
        verify(repositoryAddress, times(1)).findById(1L);
        verify(repositoryAddress, times(1)).save(any(AddressEntity.class));

        System.out.println("✅ Atualização completa funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas a rua")
    void deveAtualizarApenasARua() {
        System.out.println("🛣️ Testando atualização apenas da rua...");

        // CONFIGURA APENAS A RUA
        updateData.setStreet("Nova Rua");
        updateData.setNeighborhood(""); // String vazia - não deve atualizar
        updateData.setNumber(null);     // Null - não deve atualizar
        updateData.setCity(null);       // Null - não deve atualizar

        // CONFIGURA MOCKS
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(repositoryAddress.save(any(AddressEntity.class))).thenReturn(existingAddress);

        // EXECUTA
        AddressEntity result = addressService.update(1L, updateData);

        // VERIFICA QUE APENAS A RUA FOI ATUALIZADA
        assertThat(result.getStreet()).isEqualTo("Nova Rua");
        assertThat(result.getNumber()).isEqualTo(123); // Valor original
        assertThat(result.getNeighborhood()).isEqualTo("Centro"); // Valor original
        assertThat(result.getCity().getNome()).isEqualTo("São Paulo"); // Valor original

        verify(repositoryAddress, times(1)).findById(1L);
        verify(repositoryAddress, times(1)).save(any(AddressEntity.class));

        System.out.println("✅ Atualização parcial funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas o bairro")
    void deveAtualizarApenasOBairro() {
        System.out.println("🏘️ Testando atualização apenas do bairro...");

        // CONFIGURA APENAS O BAIRRO
        updateData.setNeighborhood("Novo Bairro");
        updateData.setStreet(""); // String vazia - não deve atualizar
        updateData.setNumber(null);
        updateData.setCity(null);

        // CONFIGURA MOCKS
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(repositoryAddress.save(any(AddressEntity.class))).thenReturn(existingAddress);

        // EXECUTA
        AddressEntity result = addressService.update(1L, updateData);

        // VERIFICA
        assertThat(result.getNeighborhood()).isEqualTo("Novo Bairro");
        assertThat(result.getStreet()).isEqualTo("Rua das Flores"); // Original
        assertThat(result.getNumber()).isEqualTo(123); // Original

        System.out.println("✅ Atualização do bairro funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas o número")
    void deveAtualizarApenasONumero() {
        System.out.println("🔢 Testando atualização apenas do número...");

        // CONFIGURA APENAS O NÚMERO
        updateData.setNumber(999);
        updateData.setStreet("");
        updateData.setNeighborhood("");
        updateData.setCity(null);

        // CONFIGURA MOCKS
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(repositoryAddress.save(any(AddressEntity.class))).thenReturn(existingAddress);

        // EXECUTA
        AddressEntity result = addressService.update(1L, updateData);

        // VERIFICA
        assertThat(result.getNumber()).isEqualTo(999);
        assertThat(result.getStreet()).isEqualTo("Rua das Flores"); // Original

        System.out.println("✅ Atualização do número funcionou!");
    }

    @Test
    @DisplayName("Deve atualizar apenas a cidade")
    void deveAtualizarApenasACidade() {
        System.out.println("🏙️ Testando atualização apenas da cidade...");

        // CONFIGURA APENAS A CIDADE
        CityEntity newCity = new CityEntity();
        newCity.setId(3L);
        newCity.setNome("Brasília");

        updateData.setCity(newCity);
        updateData.setStreet("");
        updateData.setNeighborhood("");
        updateData.setNumber(null);

        // CONFIGURA MOCKS
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(repositoryAddress.save(any(AddressEntity.class))).thenReturn(existingAddress);

        // EXECUTA
        AddressEntity result = addressService.update(1L, updateData);

        // VERIFICA
        assertThat(result.getCity().getNome()).isEqualTo("Brasília");
        assertThat(result.getStreet()).isEqualTo("Rua das Flores"); // Original

        System.out.println("✅ Atualização da cidade funcionou!");
    }

    @Test
    @DisplayName("Não deve atualizar com strings vazias")
    void naoDeveAtualizarComStringsVazias() {
        System.out.println("🚫 Testando que strings vazias não atualizam...");

        // CONFIGURA STRINGS VAZIAS
        updateData.setStreet(""); // Não deve atualizar
        updateData.setNeighborhood(""); // Não deve atualizar
        updateData.setNumber(null); // Não deve atualizar
        updateData.setCity(null); // Não deve atualizar

        // CONFIGURA MOCKS
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(repositoryAddress.save(any(AddressEntity.class))).thenReturn(existingAddress);

        // EXECUTA
        AddressEntity result = addressService.update(1L, updateData);

        // VERIFICA QUE NADA FOI ALTERADO
        assertThat(result.getStreet()).isEqualTo("Rua das Flores"); // Original
        assertThat(result.getNeighborhood()).isEqualTo("Centro"); // Original
        assertThat(result.getNumber()).isEqualTo(123); // Original
        assertThat(result.getCity().getNome()).isEqualTo("São Paulo"); // Original

        System.out.println("✅ Strings vazias não atualizaram!");
    }

    @Test
    @DisplayName("Deve lançar exceção quando endereço não existe para atualizar")
    void deveLancarExcecaoQuandoEnderecoNaoExisteParaAtualizar() {
        System.out.println("❌ Testando endereço não encontrado para atualização...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryAddress.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> addressService.update(999L, updateData))
            .isInstanceOf(RuntimeException.class); // O código atual vai dar NoSuchElementException

        verify(repositoryAddress, times(1)).findById(999L);
        verify(repositoryAddress, never()).save(any(AddressEntity.class));

        System.out.println("✅ Exceção para endereço não encontrado funcionou!");
    }

    @Test
    @DisplayName("Deve deletar endereço existente")
    void deveDeletarEnderecoExistente() {
        System.out.println("🗑️ Testando deleção de endereço existente...");

        // CONFIGURA MOCK
        when(repositoryAddress.findById(1L)).thenReturn(Optional.of(existingAddress));
        doNothing().when(repositoryAddress).deleteById(1L);

        // EXECUTA
        addressService.delete(1L);

        // VERIFICA CHAMADAS
        verify(repositoryAddress, times(1)).findById(1L);
        verify(repositoryAddress, times(1)).deleteById(1L);

        System.out.println("✅ Deleção funcionou!");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar endereço inexistente")
    void deveLancarExcecaoAoDeletarEnderecoInexistente() {
        System.out.println("❌ Testando deleção de endereço inexistente...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryAddress.findById(999L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA EXCEÇÃO
        assertThatThrownBy(() -> addressService.delete(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Endereço não encontrado");

        // VERIFICA QUE DELETE NÃO FOI CHAMADO
        verify(repositoryAddress, times(1)).findById(999L);
        verify(repositoryAddress, never()).deleteById(anyLong());

        System.out.println("✅ Exceção para endereço não encontrado funcionou!");
    }

    @Test
    @DisplayName("Deve tratar caso onde o Optional está vazio na atualização")
    void deveTratarCasoOndeOptionalEstaVazioNaAtualizacao() {
        System.out.println("⚠️ Testando comportamento com Optional vazio...");

        // CONFIGURA MOCK PARA RETORNAR VAZIO
        when(repositoryAddress.findById(1L)).thenReturn(Optional.empty());

        // EXECUTA E VERIFICA QUE LANÇA EXCEÇÃO
        assertThatThrownBy(() -> addressService.update(1L, updateData))
            .isInstanceOf(RuntimeException.class); // Na verdade será NoSuchElementException

        verify(repositoryAddress, times(1)).findById(1L);
        verify(repositoryAddress, never()).save(any(AddressEntity.class));

        System.out.println("✅ Comportamento com Optional vazio OK!");
    }
}