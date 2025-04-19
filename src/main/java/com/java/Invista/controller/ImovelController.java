package com.java.Invista.controller;

import com.java.Invista.dto.request.ImovelRequest;
import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.service.ImovelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {
    private ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }
    @PostMapping
    String create(@RequestBody ImovelRequest imovel) {
        return imovelService.create(imovel);
    }
    @PutMapping("{id}")
    ImovelEntity update(@PathVariable("id")Long id, @RequestBody ImovelEntity imovel) {
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
    @DeleteMapping("{id}")
    String delete(@PathVariable("id") Long id) {
        return imovelService.delete(id);
    }
}

