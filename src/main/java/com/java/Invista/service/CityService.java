package com.java.Invista.service;

import com.java.Invista.dto.request.CityRequest;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.repository.RepositoryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private RepositoryCity repositoryCity;
    @Autowired
    private RepositoryState repositoryState;


    public void create(CityRequest request){
         CityEntity city = request.toModel(repositoryState);
         repositoryCity.save(city);
    }
    public void createAll(List<CityRequest> requests){
        for(CityRequest request : requests){
            CityEntity city = request.toModel(repositoryState);
            repositoryCity.save(city);
        }
    }

}

