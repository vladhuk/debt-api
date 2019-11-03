package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.RepaymentRequest;
import com.vladhuk.dept.api.service.DebtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/repayment-requests")
public class RepaymentRequestsController {

    private DebtService debtService;

    public RepaymentRequestsController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping("/sent")
    public List<RepaymentRequest> getSentRepaymentRequests(@RequestParam(value = "page", required = false, defaultValue = "0")
                                                                   Integer pageNumber,
                                                           @RequestParam(value = "size", required = false, defaultValue = "9999")
                                                                   Integer pageSize) {
        return debtService.getSentRepaymentRequests(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<RepaymentRequest> getReceivedRepaymentRequests(@RequestParam(value = "page", required = false, defaultValue = "0")
                                                                       Integer pageNumber,
                                                               @RequestParam(value = "size", required = false, defaultValue = "9999")
                                                                       Integer pageSize) {
        return debtService.getReceivedRepaymentRequests(pageNumber, pageSize);
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
