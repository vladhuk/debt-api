package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.payload.DebtResponse;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.DebtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debts")
public class DebtController {

    private final DebtService debtService;
    private final AuthenticationService authenticationService;

    public DebtController(DebtService debtService, AuthenticationService authenticationService) {
        this.debtService = debtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<DebtResponse> getAllDebts() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();

        return debtService.getAllDebts().stream()
                .map(debt -> DebtResponse.create(debt, currentUserId))
                .collect(Collectors.toList());
    }

    @GetMapping(params = {"page", "size"})
    public List<DebtResponse> getDebtsPage(@RequestParam(value = "page") Integer pageNumber,
                                   @RequestParam(value = "size") Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();

        return debtService.getDebtsPage(pageNumber, pageSize)
                .stream()
                .map(debt -> DebtResponse.create(debt, currentUserId))
                .collect(Collectors.toList());
    }

}
