package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.DebtRequest;
import com.vladhuk.dept.api.service.DebtRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debt-management/debt-requests")
public class DebtRequestController {

    private DebtRequestService debtRequestService;

    public DebtRequestController(DebtRequestService debtRequestService) {
        this.debtRequestService = debtRequestService;
    }

    @GetMapping("/sent")
    public List<DebtRequest> getAllSentDebtRequests() {
        return debtRequestService.getAllSentDebtRequests();
    }

    @GetMapping(value = "/sent", params = {"page", "size"})
    public List<DebtRequest> getSentDebtRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                     @RequestParam(value = "size") Integer pageSize) {
        return debtRequestService.getSentDebtRequestsPage(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<DebtRequest> getAllReceivedDebtRequests() {
        return debtRequestService.getAllReceivedDebtRequests();
    }

    @GetMapping(value = "/received", params = {"page", "size"})
    public List<DebtRequest> getReceivedDebtRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                         @RequestParam(value = "size") Integer pageSize) {
        return debtRequestService.getReceivedDebtRequestsPage(pageNumber, pageSize);
    }

    @PostMapping
    public DebtRequest sendDebtRequest(@RequestBody DebtRequest debtRequest) {
        return debtRequestService.sendDebtRequest(debtRequest);
    }

    @PostMapping("/{requestId}/accept")
    public DebtRequest acceptDebtRequest(@PathVariable Long requestId) {
        return debtRequestService.acceptDebtRequest(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public DebtRequest rejectDebtRequest(@PathVariable Long requestId) {
        return debtRequestService.rejectDebtRequest(requestId);
    }

}