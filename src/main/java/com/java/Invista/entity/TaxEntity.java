package com.java.Invista.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tax")
public class TaxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private double value;
    private Date date;

    @ManyToOne
    ImovelEntity imovel;

    public TaxEntity(String title, String description, double value, Date date, ImovelEntity imovel) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.date = date;
        this.imovel = imovel;
    }

    public TaxEntity() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ImovelEntity getImovel() {
        return imovel;
    }

    public void setImovel(ImovelEntity imovel) {
        this.imovel = imovel;
    }
}
