package com.java.Invista.service;

import com.java.Invista.entity.CityEntity;
import com.java.Invista.repository.RepositoryCity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
public class CityService {
    private final RepositoryCity repositoryCity;
    public CityService(RepositoryCity repositoryCity) {
        this.repositoryCity = repositoryCity;
    }

    public String createAll(List<CityEntity> citys){
         repositoryCity.saveAll(citys);
         return "Cidades cadastrada com sucesso!";
    }

}

