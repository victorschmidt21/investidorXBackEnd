package com.java.Invista.dto.request;

import com.java.Invista.entity.CityEntity;
import com.java.Invista.entity.StateEntity;
import com.java.Invista.repository.RepositoryState;

public class CityRequest {
    private String name;
    private Long stateId;

    public CityRequest(Long stateId, String name) {
        this.stateId = stateId;
        this.name = name;
    }

    public CityEntity toModel(RepositoryState repositoryState) {
        StateEntity state = repositoryState.findById(stateId)
                .orElseThrow(() -> new RuntimeException("Estado não encontrado!"));
        return new CityEntity(name, state);
    }

}
