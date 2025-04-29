package com.java.Invista.controller;

import com.java.Invista.dto.request.OwnerRequest;
import com.java.Invista.entity.OwnerEntity;
import com.java.Invista.service.OwnerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    OwnerService ownerService;
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    public OwnerEntity createOwner(@RequestBody OwnerRequest ownerRequest) {
        return ownerService.create(ownerRequest);
    }

    @GetMapping
    public List<OwnerEntity> getOwners() {
        return ownerService.getOwners();
    }

    @PutMapping("/{id}")
    OwnerEntity update(@PathVariable("id") Long id, @RequestBody OwnerRequest ownerRequest) {
        return ownerService.update(id, ownerRequest);
    }

    @GetMapping("/{id}")
    List<OwnerEntity> getOwner(@PathVariable("id") String id) {
        return ownerService.getByUserId(id);
    }

    @DeleteMapping("/{id}")
    String delete(@PathVariable("id") Long id) {
        return ownerService.delete(id);
    }
}
