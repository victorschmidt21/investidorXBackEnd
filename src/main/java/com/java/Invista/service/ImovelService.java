package com.java.Invista.service;

import com.java.Invista.dto.request.ImovelRequest;
import com.java.Invista.entity.AddressEntity;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ImovelService {
    private RepositoryImovel repositoryImovel;

    @Autowired
    private RepositoryCity repositoryCity;
    @Autowired
    private RepositoryOwner repositoryOwner;
    @Autowired
    private RepositoryAddress repositoryAddress;
    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    CityService cityService;
    @Autowired
    AddressService addressService;

    public ImovelService(RepositoryImovel repositoryImovel) {
        this.repositoryImovel = repositoryImovel;
    }

    public String create(ImovelRequest imovelRequest) {
        ImovelEntity imovel = imovelRequest.toModel(repositoryUser,repositoryCity, repositoryOwner, repositoryAddress);
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

    public ImovelEntity update(Long id, ImovelRequest imovel) {
        Optional<ImovelEntity> imovelOptional = repositoryImovel.findById(id);
        if (imovelOptional.isEmpty()) {
            throw new RuntimeException("Erro: ID do imóvel não encontrado!");
        }
        if(imovel.getNomeImovel() != null && imovel.getNomeImovel() != "") {
            imovelOptional.get().setNome_imovel(imovel.getNomeImovel());
        }
        if(imovel.getValueRegistration() != null) {
            imovelOptional.get().setValueRegistration(imovel.getValueRegistration());
            imovelOptional.get().setDate_Value(LocalDate.now());
        }
        if(imovel.getStreet() != null || imovel.getNeighborhood() != null || imovel.getNumber() != null || imovel.getCityId() != null || imovel.getCep() != null) {
            AddressEntity address = imovelOptional.get().getAdress();
            if(imovel.getCityId() != null) {
                CityEntity city = cityService.getById(imovel.getCityId()).orElseThrow(() -> new RuntimeException("Cidade não encontrada"));
                address.setCity(city);
            }
            if(imovel.getStreet() != null) {
                address.setStreet(imovel.getStreet());
            }
            if(imovel.getNeighborhood() != null){
                address.setNeighborhood(imovel.getNeighborhood());
            }
            if(imovel.getNumber() != null){
                address.setNumber(imovel.getNumber());
            }
            if (imovel.getCep() != null){
                address.setCep(imovel.getCep());
            }
            addressService.update(address.getId(), address);
        }
        ImovelEntity imovelUpdate = imovelOptional.get();
        return repositoryImovel.save(imovelUpdate);
    }

    public String delete(Long id) {
        Optional<ImovelEntity> imovel = repositoryImovel.findById(id);
        if(imovel.isEmpty()) {
            throw new RuntimeException("Erro: ID inexistente!");
        }
        repositoryImovel.deleteById(id);
        addressService.delete(imovel.get().getAdress().getId());
        return "Imovel removido com sucesso!";
    }





}
