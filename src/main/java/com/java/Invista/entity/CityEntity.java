package com.java.Invista.entity;

import jakarta.persistence.*;


@Entity
@Table(name= "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_city;
    private String nome;
    private Long id_state;
    public CityEntity() {

    }
    public CityEntity(String nome, Long id_state) {
        this.nome = nome;
        this.id_state = id_state;
    }

    public Long getId_city() {
        return id_city;
    }

    public void setId_city(Long id_city) {
        this.id_city = id_city;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId_state() {
        return id_state;
    }

    public void setId_state(Long id_state) {
        this.id_state = id_state;
    }
}
