package com.java.Invista.service;

import com.java.Invista.dto.request.ImovelRequest;
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

    public String create(ImovelRequest imovelRequest) {
        ImovelEntity imovel = imovelRequest.toModel();
        repositoryImovel.save(imovel);
        return "Cadastrado com sucesso!";
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
        if (imovelOptional.isEmpty()) {
            throw new RuntimeException("Erro: ID do imóvel não encontrado!");
        }

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
    }

    public String delete(Long id) {
        if(repositoryImovel.findById(id).isEmpty()) {
            throw new RuntimeException("Erro: ID inexistente!");
        }

        repositoryImovel.deleteById(id);
        return "Imovel removido com sucesso!";
    }


}
