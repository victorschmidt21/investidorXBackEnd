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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {
    @Autowired
    RepositoryOwner repositoryOwner;

    @Autowired
    RepositoryCity repositoryCity;

    @Autowired
    RepositoryAddress repositoryAddress;

    @Autowired
    RepositoryUser repositoryUser;

    @Autowired
    AddressService addressService;
    public OwnerService(RepositoryOwner repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
    }

    public OwnerEntity create(OwnerRequest ownerRequest) {
        OwnerEntity owner = ownerRequest.toModel( repositoryAddress, repositoryCity, repositoryUser);
        return repositoryOwner.save(owner);
    }

    public List<OwnerEntity> getOwners() {
        return repositoryOwner.findAll();
    }

    public String delete(Long id) {
        Optional<OwnerEntity> owner = repositoryOwner.findById(id);
        if(owner.isEmpty()) {
            throw new RuntimeException("Proprietario não encontrado!");
        }
        owner.get().setAtivo(false);
        repositoryOwner.save(owner.get());
        return "Proprietario deletado com sucesso!";
    }


    public OwnerEntity update(Long id, OwnerRequest ownerRequest) {
        Optional<OwnerEntity> ownerOptional = repositoryOwner.findById(id);
        if (ownerOptional.isEmpty()) {
            throw new RuntimeException("Proprietário não encontrado!");
        }
        if(ownerRequest.getStreet() != null || ownerRequest.getNeighborhood() != null || ownerRequest.getNumber() != null || ownerRequest.getCityId() != null || ownerRequest.getCep() != null) {
            AddressEntity address = ownerOptional.get().getAddress();
            if(ownerRequest.getCityId() != null) {
                CityEntity city = repositoryCity.findById(ownerRequest.getCityId()).orElseThrow(() -> new RuntimeException("Cidade não encontrada!"));
                address.setCity(city);
            }
            if(ownerRequest.getStreet() != null) {
                address.setStreet(ownerRequest.getStreet());
            }
            if(ownerRequest.getNeighborhood() != null){
                address.setNeighborhood(ownerRequest.getNeighborhood());
            }
            if(ownerRequest.getNumber() != null){
                address.setNumber(ownerRequest.getNumber());
            }
            if(ownerRequest.getCep() != null) {
                address.setCep(ownerRequest.getCep());
            }
            addressService.update(address.getId(), address);
        }
        if(ownerRequest.getName() != null) {
            ownerOptional.get().setName(ownerRequest.getName());
        }
        if(ownerRequest.getPhone() != null) {
            ownerOptional.get().setPhone(ownerRequest.getPhone());
        }
        if(ownerRequest.getEmail() != null) {
            ownerOptional.get().setEmail(ownerRequest.getEmail());
        }
        if(ownerRequest.getCpf_cnpj() != null) {
            ownerOptional.get().setCpf_cnpj(ownerRequest.getCpf_cnpj());
        }
        if (ownerRequest.getUserId() != null){
            UserEntity user = repositoryUser.findById(ownerRequest.getUserId()).orElseThrow(()->
                    new RuntimeException("Usuário não encontrado"));
            ownerOptional.get().setUser(user);
        }
        OwnerEntity ownerUpdate = ownerOptional.get();
        repositoryOwner.save(ownerUpdate);
        return ownerUpdate;
    }

    public List<OwnerEntity> getByUserId(String userId) {
        return repositoryOwner.findByAtivoTrue(userId);
    }

}
