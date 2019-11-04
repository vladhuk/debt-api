package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.RepaymentRequest;
import com.vladhuk.dept.api.service.DebtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/repayment-requests")
public class RepaymentRequestController {

    private DebtService debtService;

    public RepaymentRequestController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping("/sent")
    public List<RepaymentRequest> getAllSentRepaymentRequests() {
        return debtService.getAllSentRepaymentRequests();
    }

    @GetMapping("/sent")
    public List<RepaymentRequest> getSentRepaymentRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                               @RequestParam(value = "size") Integer pageSize) {
        return debtService.getSentRepaymentRequestsPage(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<RepaymentRequest> getAllReceivedRepaymentRequests() {
        return debtService.getAllReceivedRepaymentRequests();
    }

    @GetMapping("/received")
    public List<RepaymentRequest> getReceivedRepaymentRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                                   @RequestParam(value = "size") Integer pageSize) {
        return debtService.getReceivedRepaymentRequestsPage(pageNumber, pageSize);
    }

    @PostMapping
    public RepaymentRequest sendRepaymentRequest(@RequestBody RepaymentRequest repaymentRequest) {
        return debtService.sendRepaymentRequest(repaymentRequest);
    }

    @PostMapping("/{requestId}/accept")
    public RepaymentRequest acceptRepaymentRequest(@PathVariable Long requestId) {
        return debtService.acceptRepaymentRequest(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public RepaymentRequest rejectRepaymentRequest(@PathVariable Long requestId) {
        return debtService.rejectRepaymentRequest(requestId);
    }

}
