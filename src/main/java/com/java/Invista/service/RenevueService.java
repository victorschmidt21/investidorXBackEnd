package com.java.Invista.service;

import com.java.Invista.dto.request.ExpenseRequest;
import com.java.Invista.dto.request.RenevueRequest;
import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.entity.RenevueEntity;
import com.java.Invista.repository.RepositoryImovel;
import com.java.Invista.repository.RepositoryRenevue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RenevueService {
    @Autowired
    RepositoryRenevue repositoryRenevue;
    @Autowired
    RepositoryImovel repositoryImovel;

    public RenevueEntity create(RenevueRequest renevueRequest){
        RenevueEntity renevue = renevueRequest.toModel(repositoryImovel);
        return repositoryRenevue.save(renevue);
    }

    public Double getValueTotal() {
        Double total = repositoryRenevue.sumTotalValue();
        return total != null ? total : 0.0;  // evita null
    }

    public  String deleteById(Long id){
        repositoryRenevue.deleteById(id);
        return "Deletado com sucesso!";
    }

    public RenevueEntity update(Long id, RenevueRequest renevueRequest){
        RenevueEntity renevue = repositoryRenevue.findById(id).orElseThrow(()-> new RuntimeException("Despesa não encontrada!"));
        if(renevueRequest.getTitle() != null){
            renevue.setTitle(renevueRequest.getTitle());
        }
        if(renevueRequest.getDescription() != null){
            renevue.setDescription(renevueRequest.getDescription());
        }
        if(renevueRequest.getValue() != 0){
            renevue.setValue(renevueRequest.getValue());
        }
        return repositoryRenevue.save(renevue);
    }

}
