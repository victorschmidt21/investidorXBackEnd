package com.java.Invista.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Table(name = "imoveis")
public class ImovelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_imovel;
    private String nome_imovel;
    private String street;
    private Number number;
    private String neighboard;
    private Number valueRegistration;
    private Date date_Value;
    @ManyToOne
    CityEntity city;
    @ManyToOne
    UserEntity user;
    @ManyToOne
    OwnerEntity owner;

    public ImovelEntity() {}

    public Long getId_imovel() {
        return id_imovel;
    }

    public void setId_imovel(Long id_imovel) {
        this.id_imovel = id_imovel;
    }

    public String getNome_imovel() {
        return nome_imovel;
    }

    public void setNome_imovel(String nome_imovel) {
        this.nome_imovel = nome_imovel;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public String getNeighboard() {
        return neighboard;
    }

    public void setNeighboard(String neighboard) {
        this.neighboard = neighboard;
    }

    public Number getValueRegistration() {
        return valueRegistration;
    }

    public void setValueRegistration(Number valueRegistration) {
        this.valueRegistration = valueRegistration;
    }

    public Date getDate_Value() {
        return date_Value;
    }

    public void setDate_Value(Date date_Value) {
        this.date_Value = date_Value;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OwnerEntity getOwner() {
        return owner;
    }

    public void setOwner(OwnerEntity owner) {
        this.owner = owner;
    }

    public ImovelEntity(String nome_imovel, String street, Number number, String neighboard, Number valueRegistration, Date date_Value, CityEntity city, UserEntity user, OwnerEntity owner) {
        this.nome_imovel = nome_imovel;
        this.street = street;
        this.number = number;
        this.neighboard = neighboard;
        this.valueRegistration = valueRegistration;
        this.date_Value = date_Value;
        this.city = city;
        this.user = user;
        this.owner = owner;
    }
}

