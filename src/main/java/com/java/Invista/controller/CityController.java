package com.java.Invista.controller;

import com.java.Invista.entity.CityEntity;
import com.java.Invista.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("city")
public class CityController {
    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    String createAll(@RequestBody List<CityEntity> citys){
        return cityService.createAll(citys);
    }
}
