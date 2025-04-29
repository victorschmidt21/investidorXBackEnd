package com.java.Invista.controller;

import com.java.Invista.entity.StateEntity;
import com.java.Invista.service.StateService;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<String> create(@RequestBody StateEntity state) {
        stateService.create(state);
        return ResponseEntity.ok("Imovel cadastrado com sucesso!");
    }
    @PostMapping("/all")
    String creates(@RequestBody List<StateEntity> states) {
        return  stateService.creates(states);
    }
    @GetMapping
    List<StateEntity> getAll() {
        return stateService.getAll();
    }
}
