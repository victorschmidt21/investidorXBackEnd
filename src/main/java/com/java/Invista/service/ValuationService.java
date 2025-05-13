package com.java.Invista.service;

import com.java.Invista.dto.request.ValuationRequest;
import com.java.Invista.entity.ValuationEntity;
import com.java.Invista.repository.RepositoryImovel;
import com.java.Invista.repository.RepositoryValuation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValuationService {
    @Autowired
    RepositoryValuation repositoryValuation;

    @Autowired
    RepositoryImovel repositoryImovel;

    public ValuationEntity create(ValuationRequest valuationRequest) {
        ValuationEntity valuationEntity = valuationRequest.toModel(repositoryImovel);
        return repositoryValuation.save(valuationEntity);
    }

    public ValuationEntity update(ValuationRequest valuationRequest, Long id) {
        ValuationEntity valuationUpdate = repositoryValuation.findById(id).orElseThrow(() ->
                new RuntimeException("Valorização não encontrada"));
        if (valuationRequest.getNameResponsible() != null) {
            valuationUpdate.setNameResponsible(valuationRequest.getNameResponsible());
        }
        if (valuationRequest.getDescription() != null) {
            valuationUpdate.setDescription(valuationRequest.getDescription());
        }
        if (valuationRequest.getDate() != null) {
            valuationUpdate.setDate(valuationRequest.getDate());
        }
        if (valuationRequest.getValue() != 0){
            valuationUpdate.setValue(valuationRequest.getValue());
        }
        return repositoryValuation.save(valuationUpdate);
    }
    public ValuationEntity getByIdImovel(Long id) {
        return repositoryValuation.findByImovelId(id);
    }

}
