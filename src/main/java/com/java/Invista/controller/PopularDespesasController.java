package com.java.Invista.controller;

import com.java.Invista.dto.request.ExpenseRequest;
import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/despesas")
public class PopularDespesasController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/popular")
    public List<ExpenseEntity> popularDespesas(@RequestBody List<ExpenseRequest> expenseRequests) {
        return expenseService.createMany(expenseRequests);
    }
}
