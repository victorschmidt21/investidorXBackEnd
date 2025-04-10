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
    private Long id_cidade;
    private Long id_state;
    private Long id_user;
    private Long id_owner;
    private Long assessment;
    private Date date_Value;
    private Number valueRegistration;

    public Date getDate_Value() {
        return date_Value;
    }

    public void setDate_Value(Date date_Value) {
        this.date_Value = date_Value;
    }

    public Number getValueRegistration() {
        return valueRegistration;
    }

    public void setValueRegistration(Number valueRegistration) {
        this.valueRegistration = valueRegistration;
    }

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

    public Long getId_state() {
        return id_state;
    }

    public void setId_state(Long id_state) {
        this.id_state = id_state;
    }

    public Long getId_cidade() {
        return id_cidade;
    }

    public void setId_cidade(Long id_cidade) {
        this.id_cidade = id_cidade;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_owner() {
        return id_owner;
    }

    public void setId_owner(Long id_owner) {
        this.id_owner = id_owner;
    }

    public Long getAssessment() {
        return assessment;
    }

    public void setAssessment(Long assessment) {
        this.assessment = assessment;
    }
}

