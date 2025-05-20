package com.java.Invista.service;

import com.java.Invista.dto.request.ExpenseRequest;
import com.java.Invista.entity.ExpenseEntity;
import com.java.Invista.repository.RepositoryExpense;
import com.java.Invista.repository.RepositoryImovel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {
    @Autowired
    RepositoryExpense repositoryExpense;
    @Autowired
    RepositoryImovel repositoryImovel;


    public ExpenseEntity create(ExpenseRequest expenseRequest){
        ExpenseEntity expense = expenseRequest.toModel(repositoryImovel);
        return repositoryExpense.save(expense);
    }

    public Map<String, Object> getValueTotal(Long id) {
        Double value = 0.0;
        Map<String, Object> map = new HashMap<>();
        List<ExpenseEntity> expenses = repositoryExpense.findByImovelId(id);
        for(ExpenseEntity expense: expenses){
            value = value + expense.getValue();
        }
        map.put("Número de despesas", expenses.size());
        map.put("Ticket Médio", value/expenses.size());
        map.put("valueTotal", value);
        return map;
    }

    public  String deleteById(Long id){
        repositoryExpense.deleteById(id);
        return "Deletado com sucesso!";
    }

    public ExpenseEntity update(Long id, ExpenseRequest expenseRequest){
        ExpenseEntity expense = repositoryExpense.findById(id).orElseThrow(()-> new RuntimeException("Despesa não encontrada!"));
        if(expenseRequest.getTitle() != null){
            expense.setTitle(expenseRequest.getTitle());
        }
        if(expenseRequest.getDescription() != null){
            expense.setDescription(expenseRequest.getDescription());
        }
        if(expenseRequest.getValue() != 0){
            expense.setValue(expenseRequest.getValue());
        }
        return repositoryExpense.save(expense);
    }

    public List<ExpenseEntity> createMany(List<ExpenseRequest> expenseRequests) {
        List<ExpenseEntity> expenses = expenseRequests.stream()
                .map(request -> request.toModel(repositoryImovel))
                .toList();

        return repositoryExpense.saveAll(expenses);
    }

    public ExpenseEntity createOne(ExpenseRequest expenseRequest) {
        ExpenseEntity expense = expenseRequest.toModel(repositoryImovel);
        return repositoryExpense.save(expense);
    }

    public Map<String, Object> getResumoDespesasPorImovel(Long imovelId) {
        var imovel = repositoryImovel.findById(imovelId)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        List<ExpenseEntity> despesas = repositoryExpense.findByImovelId(imovelId);

        List<Map<String, Object>> listaDespesas = despesas.stream().map(despesa -> {
            Map<String, Object> dados = new HashMap<>();
            dados.put("título", despesa.getTitle());
            dados.put("valor", despesa.getValue());
            dados.put("data", despesa.getDate());
            return dados;
        }).toList();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("nomeImovel", imovel.getNome_imovel());
        resposta.put("despesas", listaDespesas);

        return resposta;
    }


}
