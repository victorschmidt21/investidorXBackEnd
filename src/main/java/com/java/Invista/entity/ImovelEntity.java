package com.java.Invista.entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "imoveis")
public class ImovelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_imovel;
    private String nome_imovel;
    private Double valueRegistration;
    private LocalDate date_Value;
    private Boolean ativo = true;
    @ManyToOne
    AddressEntity adress;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @ManyToOne
    OwnerEntity owner;

    public ImovelEntity() {}


    public ImovelEntity(String nome_imovel, Number valueRegistration, LocalDate date_Value, AddressEntity adress, UserEntity user, OwnerEntity owner) {
        this.nome_imovel = nome_imovel;
        this.valueRegistration = (Double) valueRegistration;
        this.date_Value = date_Value;
        this.adress = adress;
        this.user = user;
        this.owner = owner;
    }

    public String getNome_imovel() {
        return nome_imovel;
    }

    public void setNome_imovel(String nome_imovel) {
        this.nome_imovel = nome_imovel;
    }

    public Number getValueRegistration() {
        return valueRegistration;
    }

    public void setValueRegistration(Number valueRegistration) {
        this.valueRegistration = (Double) valueRegistration;
    }

    public LocalDate getDate_Value() {
        return date_Value;
    }

    public void setDate_Value(LocalDate date_Value) {
        this.date_Value = date_Value;
    }

    public AddressEntity getAdress() {
        return adress;
    }

    public void setAdress(AddressEntity adress) {
        this.adress = adress;
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

    public Long getId_imovel() {
        return id_imovel;
    }

    public void setId_imovel(Long id_imovel) {
        this.id_imovel = id_imovel;
    }

    public void setAtivo(boolean b) {

    }
}

