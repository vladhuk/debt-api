package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.DebtRequest;
import com.vladhuk.debt.api.service.DebtRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debt-requests")
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

    @GetMapping("/received/new/count")
    public Long countNewReceivedDebtRequests() {
        return debtRequestService.countNewReceivedDebtRequests();
    }

    @DeleteMapping("/sent/{id}")
    public void deleteSentDebtRequestIfNoAcceptedOrRejectedOrders(@PathVariable Long id) {
        debtRequestService.deleteSentDebtRequestIfNoAcceptedOrRejectedOrders(id);
    }

    @PostMapping
    public DebtRequest sendDebtRequest(@RequestBody DebtRequest debtRequest) {
        return debtRequestService.sendDebtRequest(debtRequest);
    }

    @PutMapping("/{requestId}/accept")
    public DebtRequest acceptDebtRequest(@PathVariable Long requestId) {
        return debtRequestService.acceptDebtRequestAndUpdateBalance(requestId);
    }

    @PutMapping("/{requestId}/reject")
    public DebtRequest rejectDebtRequest(@PathVariable Long requestId) {
        return debtRequestService.rejectDebtRequest(requestId);
    }

}
