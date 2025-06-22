package com.java.Invista.dto.request;

import com.java.Invista.entity.AddressEntity;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.OwnerEntity;
import com.java.Invista.entity.UserEntity;
import com.java.Invista.repository.RepositoryAddress;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.repository.RepositoryUser;


public class OwnerRequest {

    private String name;
    private String cpf_cnpj;
    private String phone;
    private String email;
    private Long cityId;
    private String street;
    private Integer number;
    private String neighborhood;
    private String userId;
    private Integer cep;

    public OwnerRequest(String name, String cpf_cnpj, String phone, String email, Long cityid, String street, Integer number, String neighborhood, String userId, Integer cep) {
        this.name = name;
        this.cpf_cnpj = cpf_cnpj;
        this.phone = phone;
        this.email = email;
        this.cityId = cityid;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.userId = userId;
        this.cep = cep;
    }

    public OwnerRequest() {

    }

    public OwnerEntity toModel(RepositoryAddress repositoryAddress, RepositoryCity repositoryCity, RepositoryUser repositoryUser){
        UserEntity user = repositoryUser.findById(userId).orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        CityEntity city = repositoryCity.findById(cityId).orElseThrow(() -> new RuntimeException("Cidade não encontrada!"));
        AddressEntity address = new AddressEntity(street, number, neighborhood, city, cep);
        repositoryAddress.save(address);
        return new OwnerEntity(name, cpf_cnpj, phone.toString(), email, address, user);
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
        return phone.toString();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
