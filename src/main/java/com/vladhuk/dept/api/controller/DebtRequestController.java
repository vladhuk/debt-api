package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.DebtRequest;
import com.vladhuk.dept.api.service.DebtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/debt-requests")
public class DebtRequestController {

    private DebtService debtService;

    public DebtRequestController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping("/sent")
    public List<DebtRequest> getSentDebtRequests(@RequestParam(value = "page", required = false, defaultValue = "0")
                                                         Integer pageNumber,
                                                 @RequestParam(value = "size", required = false, defaultValue = "9999")
                                                         Integer pageSize) {
        return debtService.getSentDebtRequests(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<DebtRequest> getReceivedDebtRequests(@RequestParam(value = "page", required = false, defaultValue = "0")
                                                             Integer pageNumber,
                                                     @RequestParam(value = "size", required = false, defaultValue = "9999")
                                                             Integer pageSize) {
        return debtService.getReceivedDebtRequests(pageNumber, pageSize);
    }

    @PostMapping
    public DebtRequest sendDebtRequest(@RequestBody DebtRequest debtRequest) {
        return debtService.sendDebtRequest(debtRequest);
    }

    @PostMapping("/{requestId}/accept")
    public DebtRequest acceptDebtRequest(@PathVariable Long requestId) {
        return debtService.acceptDebtRequest(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public DebtRequest rejectDebtRequest(@PathVariable Long requestId) {
        return debtService.rejectDebtRequest(requestId);
    }

}
