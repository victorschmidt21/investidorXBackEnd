package com.java.Invista.controller;

import com.java.Invista.dto.request.CityRequest;
import com.java.Invista.entity.CityEntity;
import com.java.Invista.repository.RepositoryCity;
import com.java.Invista.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("city")
public class CityController {
    @Autowired
    private CityService cityService;
    @Autowired
    private RepositoryCity repositoryCity;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody CityRequest cityRequest){
        cityService.create(cityRequest);
        return ResponseEntity.ok("Cidade cadastrada com sucesso!");
    }

    @PostMapping("/all")
    public ResponseEntity<String> createAll(@RequestBody List<CityRequest> citysRequest){
        cityService.createAll(citysRequest);
        return ResponseEntity.ok("Cidades cadastrada com sucesso!");
    }

    @GetMapping("/{stateId}")
    public List<CityEntity> getCityByState(@PathVariable Long stateId){
        return repositoryCity.findByStateId(stateId);
    }

}
