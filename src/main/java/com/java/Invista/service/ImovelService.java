package com.java.Invista.service;

import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.repository.RepositoryImovel;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImovelService {
    private RepositoryImovel repositoryImovel;

    public ImovelService(RepositoryImovel repositoryImovel) {
        this.repositoryImovel = repositoryImovel;
    }

    public String create(ImovelEntity imovel) {
        // Validações iniciais
        validarCamposObrigatorios(imovel);

        // Salva o imóvel no repositório
        repositoryImovel.save(imovel);
        return "Cadastrado com sucesso!";
    }

    private void validarCamposObrigatorios(ImovelEntity imovel) {
        if (imovel == null) {
            throw new IllegalArgumentException("Imóvel não pode ser nulo");
        }
        List<String> camposFaltantes = new ArrayList<>();

        if (imovel.getNome_imovel() == null) {
            camposFaltantes.add("nome_imovel");
        }
        if (imovel.getId_cidade() == null) {
            camposFaltantes.add("id_cidade");
        }
        if (imovel.getId_state() == null) {
            camposFaltantes.add("id_state");
        }
        if (imovel.getAssessment() == null) {
            camposFaltantes.add("assessment");
        }
        if (imovel.getId_user() == null) {
            camposFaltantes.add("id_user");
        }

        if (!camposFaltantes.isEmpty()) {
            String mensagem = "Faltam dados para o cadastro do imóvel: " + String.join(", ", camposFaltantes);
            throw new RuntimeException(mensagem);
        }

        if (imovel.getAssessment() < 0) {
            throw new IllegalArgumentException("O valor de assessment não pode ser negativo");
        }
    }

    public List<ImovelEntity> list() {
        return repositoryImovel.findAll();
    }

    public List<String> listImoveis() {
        List<ImovelEntity> imoveis = repositoryImovel.findAll();
        List<String> nomes = new ArrayList<>();
        for (ImovelEntity imovel : imoveis) {
            nomes.add(imovel.getNome_imovel());
        }
        return nomes;
    }
    public ImovelEntity update(Long id, ImovelEntity imovel) {
        Optional<ImovelEntity> imovelOptional = repositoryImovel.findById(id);
        if (imovelOptional.isPresent()) {
            ImovelEntity imovelUpdate = imovelOptional.get();

            if (imovel.getNome_imovel() != null) {
                imovelUpdate.setNome_imovel(imovel.getNome_imovel());
            }
            if (imovel.getStreet() != null) {
                imovelUpdate.setStreet(imovel.getStreet());
            }
            if (imovel.getNumber() != null) {
                imovelUpdate.setNumber(imovel.getNumber());
            }
            return repositoryImovel.save(imovelUpdate);
        } else {
            throw new RuntimeException("Erro: ID do imóvel não encontrado!");
        }
    }

    public String delete(Long id) {
        if(repositoryImovel.findById(id).isPresent()) {
            repositoryImovel.deleteById(id);
        }else{
            throw new RuntimeException("Erro: ID inexistente!");
        }
        return "Imovel removido com sucesso!";
    }


}
