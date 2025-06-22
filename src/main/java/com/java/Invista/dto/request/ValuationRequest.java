package com.java.Invista.dto.request;

import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.entity.ValuationEntity;
import com.java.Invista.repository.RepositoryImovel;
import java.time.LocalDate;

public class ValuationRequest {
    private String nameResponsible;
    private LocalDate date;
    private String description;
    private String rotaImage;
    private double value;
    private Long imovelId;

    public ValuationRequest(String nameResponsible, LocalDate date, String description, String rotaImage, double value, Long imovelId) {
        this.nameResponsible = nameResponsible;
        this.date = date;
        this.description = description;
        this.rotaImage = rotaImage;
        this.value = value;
        this.imovelId = imovelId;
    }

    public ValuationRequest() {

    }

    public ValuationEntity toModel(RepositoryImovel repositoryImovel) {
        ImovelEntity imovel = repositoryImovel.findById(imovelId).orElseThrow(() -> new RuntimeException("Imovel não encontrado!"));
        return new ValuationEntity(nameResponsible, description, date, rotaImage, imovel, value);
    }

    public String getNameResponsible() {
        return nameResponsible;
    }

    public void setNameResponsible(String nameResponsible) {
        this.nameResponsible = nameResponsible;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRotaImage() {
        return rotaImage;
    }

    public void setRotaImage(String rotaImage) {
        this.rotaImage = rotaImage;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getImovelId() {
        return imovelId;
    }

    public void setImovelId(Long imovelId) {
        this.imovelId = imovelId;
    }
}
