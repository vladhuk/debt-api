package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.Debt;
import com.vladhuk.dept.api.service.DebtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debt-management/debts")
public class DebtController {

    private DebtService debtService;

    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping
    public List<Debt> getAllDebts() {
        return debtService.getAllDebts();
    }

    @GetMapping(params = {"page", "size"})
    public List<Debt> getDebtsPage(@RequestParam(value = "page") Integer pageNumber,
                                   @RequestParam(value = "size") Integer pageSize) {
        return debtService.getDebtsPage(pageNumber, pageSize);
    }

}
