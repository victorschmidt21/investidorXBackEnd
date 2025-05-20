package com.java.Invista.controller;

import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.service.ExpenseService;
import com.java.Invista.service.ImovelService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboarController {

    private final ImovelService imovelService;
    private final ExpenseService expenseService;

    public DashboarController(ImovelService imovelService, ExpenseService expenseService) {
        this.imovelService = imovelService;
        this.expenseService = expenseService;
    }

    @GetMapping("/imovel/{id}")
    public ImovelEntity getByIdImovel(@PathVariable long id){
        return imovelService.buscarImovelPorId(id);
    }

    @GetMapping("/imovel/{id}/resumo")
    public Map<String, Object> getResumoImovel(@PathVariable Long id) {
        return expenseService.getResumoDespesasPorImovel(id);
    }
}

