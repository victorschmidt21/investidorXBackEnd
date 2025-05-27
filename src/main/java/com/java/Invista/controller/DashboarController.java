package com.java.Invista.controller;

import com.java.Invista.entity.ImovelEntity;
import com.java.Invista.service.ExpenseService;
import com.java.Invista.service.ImovelService;
import com.java.Invista.service.RenevueService;
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

    @GetMapping("/imovel/{id}/resumo-despesa")
    public Map<String, Object> getResumoImovel(@PathVariable Long id) {
        return expenseService.getResumoDespesasPorImovel(id);
    }

    @GetMapping("/imovel/quantidade")
    public Long getQuantidadeImoveis() {
        return imovelService.contarImoveis();
    }

    @GetMapping("/valor-total")
    public Double getValorTotalImoveis() {
        return imovelService.calcularValorTotalImoveis();
    }

}

