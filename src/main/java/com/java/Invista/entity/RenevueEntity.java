package com.java.Invista.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "renevue")
public class RenevueEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String description;
        private double value;
        private LocalDate date;

        @ManyToOne
        ImovelEntity imovel;

        public RenevueEntity(String title, String description, double value, LocalDate date, ImovelEntity imovel) {
            this.title = title;
            this.description = description;
            this.value = value;
            this.date = date;
            this.imovel = imovel;
        }

        public RenevueEntity() {
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

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public ImovelEntity getImovel() {
            return imovel;
        }

        public void setImovel(ImovelEntity imovel) {
            this.imovel = imovel;
        }
}
