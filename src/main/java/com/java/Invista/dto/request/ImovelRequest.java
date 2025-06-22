package com.java.Invista.dto.request;
import com.java.Invista.entity.*;
import com.java.Invista.repository.RepositoryAddress;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.repository.RepositoryOwner;
import com.java.Invista.repository.RepositoryUser;

import java.time.LocalDate;

public class ImovelRequest {
    private String nomeImovel;
    private String street;
    private Integer number;
    private String neighborhood;
    private Double valueRegistration;
    private LocalDate dateValue;
    private Long cityId;
    private Integer cep;
    private String userId;
    private Long ownerId;

    public ImovelRequest(String nomeImovel, String street, Integer number, String neighborhood, Double valueRegistration, Long cityId, String userId, Long ownerId, Integer cep, LocalDate dateValue) {
        this.nomeImovel = nomeImovel;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.valueRegistration = valueRegistration;
        this.dateValue = dateValue;
        this.cityId = cityId;
        this.userId = userId;
        this.ownerId = ownerId;
        this.cep = cep;

    }

    public ImovelRequest() {

    }

    public ImovelEntity toModel(RepositoryUser repositoryUser, RepositoryCity repositoryCity, RepositoryOwner repositoryOwner, RepositoryAddress repositoryAddress){
        UserEntity user = repositoryUser.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        OwnerEntity owner = repositoryOwner.findById(ownerId).orElseThrow(() -> new RuntimeException("Proprietário não encontrado!"));
        CityEntity city = repositoryCity.findById(cityId).orElseThrow(() -> new RuntimeException("Cidade não encontrada!"));
        AddressEntity address = new AddressEntity(street,number, neighborhood, city, cep);
        repositoryAddress.save(address);
        return new ImovelEntity(nomeImovel, valueRegistration, dateValue, address ,user, owner);
    }

    public String getNomeImovel() {
        return nomeImovel;
    }

    public void setNomeImovel(String nomeImovel) {
        this.nomeImovel = nomeImovel;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getValueRegistration() {
        return valueRegistration;
    }

    public void setValueRegistration(Double valueRegistration) {
        this.valueRegistration = valueRegistration;
    }

    public LocalDate getDateValue() {
        return dateValue;
    }

    public void setDateValue(LocalDate dateValue) {
        this.dateValue = dateValue;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
