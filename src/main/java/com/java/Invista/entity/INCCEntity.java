package com.java.Invista.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "incc")
public class INCCEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double indice;
    @NotNull(message = "Data não pode seu nula!")
    private LocalDate date;

    public INCCEntity(Long id, double indice, LocalDate date) {
        this.indice = indice;
        this.date = date;
    }
    public INCCEntity() {}
}
