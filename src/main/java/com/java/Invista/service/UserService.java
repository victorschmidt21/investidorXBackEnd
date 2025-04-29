package com.java.Invista.service;

import com.java.Invista.entity.UserEntity;
import com.java.Invista.repository.RepositoryUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    RepositoryUser repositoryUser;
    public UserService(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
    }

    public UserEntity create(UserEntity user) {

        return repositoryUser.save(user);
    }

}
