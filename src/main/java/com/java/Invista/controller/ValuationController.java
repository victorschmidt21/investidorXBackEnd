package com.java.Invista.controller;

import com.java.Invista.dto.request.ValuationRequest;
import com.java.Invista.entity.ValuationEntity;
import com.java.Invista.service.ValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/valuation")
public class ValuationController {
    @Autowired
    ValuationService valuationService;

    @PostMapping
    ValuationEntity create(@RequestBody ValuationRequest valuationRequest) {
        return valuationService.create(valuationRequest);
    }

    @PutMapping("/{id}")
    ValuationEntity update(@PathVariable Long id, @RequestBody ValuationRequest valuationRequest) {
        return valuationService.update(valuationRequest, id);
    }

    @GetMapping("/{id}")
    ValuationEntity get(@PathVariable Long id) {
        return valuationService.getByIdImovel(id);
    }
}
