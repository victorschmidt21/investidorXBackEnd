package com.java.Invista.service;

import com.java.Invista.entity.AddressEntity;
import com.java.Invista.repository.RepositoryAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private RepositoryAddress repositoryAddress;

    public AddressEntity update(Long id, AddressEntity addressEntity) {
        Optional<AddressEntity> addressOptional = repositoryAddress.findById(id);
        if(addressEntity.getCity() != null) {
            addressOptional.get().setCity(addressEntity.getCity());
        }
        if(!addressEntity.getStreet().isEmpty()) {
            addressOptional.get().setStreet(addressEntity.getStreet());
        }
        if(!addressEntity.getNeighborhood().isEmpty()){
            addressOptional.get().setNeighborhood(addressEntity.getNeighborhood());
        }
        if(addressEntity.getNumber() != null){
            addressOptional.get().setNumber(addressEntity.getNumber());
        }
        AddressEntity addressUpdate = addressOptional.get();
        repositoryAddress.save(addressUpdate);
        return addressUpdate;
    }

    public void delete(Long id) {
        if(repositoryAddress.findById(id).isEmpty()) {
            throw new RuntimeException("Endereço não encontrado");
        }
        repositoryAddress.deleteById(id);
    }
}
