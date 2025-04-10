package com.java.Invista.service;

import com.java.Invista.entity.StateEntity;
import com.java.Invista.repository.RepositoryState;
import org.springframework.stereotype.Service;

@Service
public class StateService {
    private RepositoryState repositoryState;
    public StateService(RepositoryState repositoryState) {
        this.repositoryState = repositoryState;
    }
    public String create(StateEntity state) {
        repositoryState.save(state);
        return "Cadastrado com sucesso!";
    }
}
