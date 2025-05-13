package com.java.Invista.controller;

import com.java.Invista.dto.request.ImovelRequest;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.repository.RepositoryImovel;
import com.java.Invista.service.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imovel")
public class ImovelController {
    private ImovelService imovelService;

    @Autowired
    RepositoryImovel repositoryImovel;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @PostMapping
    ImovelEntity create(@RequestBody ImovelRequest imovel) {
        return imovelService.create(imovel);
    }

    @PutMapping("/{id}")
    ImovelEntity update(@PathVariable("id")Long id, @RequestBody ImovelRequest imovel) {
        return imovelService.update(id, imovel);
    }
    @GetMapping
    List<ImovelEntity> list() {
        return imovelService.list();
    }
    @GetMapping("/nomes")
    List<String> listNomes() {
        return imovelService.listImoveis();
    }

    @GetMapping("/{id}")
    List<ImovelEntity> getByUser(@PathVariable String id) {
        return repositoryImovel.findByUser(id);
    }
    @DeleteMapping("/{id}")
    String delete(@PathVariable("id") Long id) {
        return imovelService.delete(id);
    }
}

