package com.java.Invista.controller;

import com.java.Invista.dto.request.ExpenseRequest;
import com.java.Invista.dto.request.RenevueRequest;
import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.entity.RenevueEntity;
import com.java.Invista.service.ExpenseService;
import com.java.Invista.service.RenevueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/renevue")
public class RenevueController {
    @Autowired
    RenevueService renevueService;

    @PostMapping
    RenevueEntity create(@RequestBody RenevueRequest request) {
        return renevueService.create(request);
    }

    @GetMapping("/imovel/{id}")
    Map<String, Object> getByIdImovel(@PathVariable long id){
        return renevueService.getValueTotal(id);
    }

    @DeleteMapping("{id}")
    String delete(@PathVariable Long id){
        return renevueService.deleteById(id);
    }

    @PutMapping("{id}")
    RenevueEntity update(@PathVariable Long id, @RequestBody RenevueRequest request){
        return renevueService.update(id, request);
    }
}
