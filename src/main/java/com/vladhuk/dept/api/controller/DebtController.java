package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.Debt;
import com.vladhuk.dept.api.service.DebtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-management/debts")
public class DebtController {

    private DebtService debtService;

    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping
    public List<Debt> getDebts(@RequestParam(value = "page", required = false, defaultValue = "0")
                                       Integer pageNumber,
                               @RequestParam(value = "size", required = false, defaultValue = "9999")
                                       Integer pageSize) {
        return debtService.getDebts(pageNumber, pageSize);
    }

}
