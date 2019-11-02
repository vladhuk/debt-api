package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.entity.Debt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debts")
public class DebtController {

    @GetMapping
    public List<Debt> getDebts(@RequestParam(value = "page", required = false) Integer pageNumber,
                               @RequestParam(value = "size", required = false) Integer pageSize) {
        return null;
    }

}
