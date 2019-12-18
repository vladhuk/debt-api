package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.RepaymentRequest;
import com.vladhuk.debt.api.service.RepaymentRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repayment-requests")
public class RepaymentRequestController {

    private RepaymentRequestService repaymentRequestService;

    public RepaymentRequestController(RepaymentRequestService repaymentRequestService) {
        this.repaymentRequestService = repaymentRequestService;
    }

    @GetMapping("/sent")
    public List<RepaymentRequest> getAllSentRepaymentRequests() {
        return repaymentRequestService.getAllSentRepaymentRequests();
    }

    @GetMapping(value = "/sent", params = {"page", "size"})
    public List<RepaymentRequest> getSentRepaymentRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                               @RequestParam(value = "size") Integer pageSize) {
        return repaymentRequestService.getSentRepaymentRequestsPage(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<RepaymentRequest> getAllReceivedRepaymentRequests() {
        return repaymentRequestService.getAllReceivedRepaymentRequests();
    }

    @GetMapping(value = "/received", params = {"page", "size"})
    public List<RepaymentRequest> getReceivedRepaymentRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                                   @RequestParam(value = "size") Integer pageSize) {
        return repaymentRequestService.getReceivedRepaymentRequestsPage(pageNumber, pageSize);
    }

    @GetMapping("/received/new/count")
    public Long countNewReceivedDebtRequests() {
        return repaymentRequestService.countNewReceivedRepaymentRequests();
    }

    @DeleteMapping("/sent/{id}")
    public void deleteSentRepaymentRequestIfNotAcceptedOrRejected(@PathVariable Long id) {
        repaymentRequestService.deleteSentRepaymentRequestIfNotAcceptedOrRejected(id);
    }

    @PostMapping
    public RepaymentRequest sendRepaymentRequest(@RequestBody RepaymentRequest repaymentRequest) {
        return repaymentRequestService.sendRepaymentRequest(repaymentRequest);
    }

    @PostMapping("/{requestId}/accept")
    public RepaymentRequest acceptRepaymentRequest(@PathVariable Long requestId) {
        return repaymentRequestService.acceptRepaymentRequestAndUpdateBalance(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public RepaymentRequest rejectRepaymentRequest(@PathVariable Long requestId) {
        return repaymentRequestService.rejectRepaymentRequest(requestId);
    }

}
