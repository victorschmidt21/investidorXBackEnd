package com.java.Invista.dto.request;

import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.entity.RenevueEntity;
import com.java.Invista.repository.RepositoryImovel;

import java.time.LocalDate;

public class RenevueRequest {
    private String title;
    private String description;
    private double value;
    private LocalDate date;
    private Long imovelId;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getImovelId() {
        return imovelId;
    }

    public RenevueRequest(String title, String description, double value, Long imovelId) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.imovelId = imovelId;
        this.date = LocalDate.now();
    }

    public RenevueEntity toModel(RepositoryImovel repositoryImovel) {
        if(value == 0 || imovelId == null || title == null || description == null){
            throw new RuntimeException("Faltam dados para o cadastro da receita");
        }
        ImovelEntity imovel = repositoryImovel.findById(imovelId).orElseThrow(()-> new RuntimeException("Imovel não encontrado"));
        return new RenevueEntity(title, description, value, date, imovel);
    }
}
