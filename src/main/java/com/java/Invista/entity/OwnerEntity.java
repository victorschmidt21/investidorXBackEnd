package com.java.Invista.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "owner")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf_cnpj;
    private String phone;
    private String email;
    private Boolean ativo = true;

    @ManyToOne
    AddressEntity address;

    @ManyToOne
    UserEntity user;

    public OwnerEntity() {}

    public OwnerEntity(String name, String cpf_cnpj, String phone, String email, AddressEntity address, UserEntity user) {
        this.name = name;
        this.cpf_cnpj = cpf_cnpj;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
