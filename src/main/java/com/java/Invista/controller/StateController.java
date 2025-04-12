package com.java.Invista.controller;

import com.java.Invista.entity.StateEntity;
import com.java.Invista.service.StateService;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/state")
public class StateController {
    private StateService stateService;
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping
    String create(@RequestBody StateEntity state) {
        return stateService.create(state);
    }
    @PostMapping("/all")
    String creates(@RequestBody List<StateEntity> states) {
        return  stateService.creates(states);
    }
    @GetMapping("/all")
    List<StateEntity> getAll() {
        return stateService.getAll();
    }
}
