package com.java.Invista.service;

import com.java.Invista.entity.StateEntity;
import com.java.Invista.repository.RepositoryState;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String creates(List<StateEntity> states) {
        repositoryState.saveAll(states);
        return "Cadastrado com sucesso!";
    }
    public List<StateEntity> getAll() {
        return repositoryState.findAll();
    }

}
