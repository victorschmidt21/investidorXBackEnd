package com.java.Invista.controller;

import com.java.Invista.dto.request.ExpenseRequest;
import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    public ExpenseController(ExpenseService expenseService) {}

    @PostMapping
    ExpenseEntity create(@RequestBody ExpenseRequest expense) {
        return expenseService.create(expense);
    }

    @GetMapping("/imovel/{id}")
    Map<String, Object> getByIdImovel(@PathVariable long id){
        return expenseService.getValueTotal(id);
    }

    @DeleteMapping("{id}")
    String delete(@PathVariable Long id){
        return expenseService.deleteById(id);
    }

    @PutMapping("{id}")
    ExpenseEntity update(@PathVariable Long id, @RequestBody ExpenseRequest expense){
        return expenseService.update(id, expense);
    }
    @PostMapping("/popular")
    public List<ExpenseEntity> popularDespesas(@RequestBody List<ExpenseRequest> expenseRequests) {
        return expenseService.createMany(expenseRequests);
    }
}
