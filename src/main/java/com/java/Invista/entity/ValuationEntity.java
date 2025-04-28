package com.java.Invista.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "valuation")
public class ValuationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameResponsible;
    private Date date;
    private String description;
    private String rotaImage;
    private double value;

    @ManyToOne
    ImovelEntity imovel;

    public ValuationEntity() {
    }

    public ValuationEntity(String nameResponsible, String description, Date date, String rotaImage, ImovelEntity imovel, double value) {
        this.nameResponsible = nameResponsible;
        this.description = description;
        this.date = date;
        this.rotaImage = rotaImage;
        this.imovel = imovel;
        this.value = value;
    }

    public String getNameResponsible() {
        return nameResponsible;
    }

    public void setNameResponsible(String nameResponsible) {
        this.nameResponsible = nameResponsible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public ImovelEntity getImovel() {
        return imovel;
    }

    public void setImovel(ImovelEntity imovel) {
        this.imovel = imovel;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
