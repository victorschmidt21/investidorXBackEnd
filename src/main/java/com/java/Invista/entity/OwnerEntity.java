package com.java.Invista.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "owner")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Name;
    private String cpf;
    private String cnpj;
    private Number Phone;
    private Number number;
    private String street;
    private String neighborhood;

    @ManyToOne
    CityEntity city;
    public OwnerEntity() {}

    public OwnerEntity(String name, String cpf, String cnpj, Number phone, Number number, String street, String neighborhood, CityEntity city) {
        Name = name;
        this.cpf = cpf;
        this.cnpj = cnpj;
        Phone = phone;
        this.number = number;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Number getPhone() {
        return Phone;
    }

    public void setPhone(Number phone) {
        Phone = phone;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }
}
