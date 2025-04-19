package com.java.Invista.dto.request;

import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.entity.OwnerEntity;
import com.java.Invista.entity.UserEntity;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.repository.RepositoryOwner;
import com.java.Invista.repository.RepositoryUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImovelRequest {
    private String nomeImovel;
    private String street;
    private Integer number;
    private String neighboard;
    private Double valueRegistration;
    private Date dateValue;
    private Long cityId;
    private Long userId;
    private Long ownerId;

    RepositoryCity repositoryCity;
    RepositoryUser repositoryUser;
    RepositoryOwner repositoryOwner;

    public ImovelRequest(RepositoryUser repositoryUser, RepositoryCity repositoryCity, RepositoryOwner repositoryOwner) {
        this.repositoryUser = repositoryUser;
        this.repositoryCity = repositoryCity;
        this.repositoryOwner = repositoryOwner;
    }

    public ImovelRequest(String nomeImovel, String street, Integer number, String neighboard, Double valueRegistration, Date dateValue, Long cityId, Long userId, Long ownerId) {
        this.nomeImovel = nomeImovel;
        this.street = street;
        this.number = number;
        this.neighboard = neighboard;
        this.valueRegistration = valueRegistration;
        this.dateValue = dateValue;
        this.cityId = cityId;
        this.userId = userId;
        this.ownerId = ownerId;
        validarCamposObrigatorios();

    }

    private void validarCamposObrigatorios() {
        List<String> camposFaltantes = new ArrayList<>();

        if (this.nomeImovel == null) {
            camposFaltantes.add("nome_imovel");
        }
        if(this.ownerId == null) {
            camposFaltantes.add("owner");
        }
        if (this.cityId == null) {
            camposFaltantes.add("city");
        }
        if (this.userId == null) {
            camposFaltantes.add("user");
        }

        if (!camposFaltantes.isEmpty()) {
            String mensagem = "Faltam dados para o cadastro do imóvel: " + String.join(", ", camposFaltantes);
            throw new RuntimeException(mensagem);
        }

    }

    public ImovelEntity toModel(){
        UserEntity user = repositoryUser.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        OwnerEntity owner = repositoryOwner.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        CityEntity city = repositoryCity.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));
        return new ImovelEntity(nomeImovel, street, number, neighboard, valueRegistration, dateValue, city, user, owner);
    }


}
