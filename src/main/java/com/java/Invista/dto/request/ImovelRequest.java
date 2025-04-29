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
    private String userId;
    private Long ownerId;
    private Integer cep;


    public Integer getCep() {
        return cep;
    }

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

    public String getStreet() {
        return street;
    }


    public Integer getNumber() {
        return number;
    }


    public Double getValueRegistration() {
        return valueRegistration;
    }


    public String getNeighborhood() {
        return neighborhood;
    }

    public Long getCityId() {
        return cityId;
    }
}
