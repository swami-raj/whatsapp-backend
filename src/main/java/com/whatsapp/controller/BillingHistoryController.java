package com.whatsapp.controller;

import com.whatsapp.dto.request.BillingHistoryRequest;
import com.whatsapp.dto.request.CountrySubscriptionRequest;
import com.whatsapp.dto.response.BillingHistoryResponse;
import com.whatsapp.dto.response.CountrySubscriptionResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("billing-history")
public class BillingHistoryController {
    @PostMapping("add")
    ResponseDto<BillingHistoryResponse> addBillingHistory(@RequestBody BillingHistoryRequest billingHistoryRequest) {
        return ServiceAccessor.getBillingHistoryService().addBillingHistory(billingHistoryRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<BillingHistoryResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getBillingHistoryService().getBillingHistoryById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<BillingHistoryResponse>> getAll() {
        return ServiceAccessor.getBillingHistoryService().getAllBillingHistory();
    }

    @PutMapping("update")
    ResponseDto<BillingHistoryResponse> updateBillingHistory(@RequestBody BillingHistoryRequest billingHistoryRequest) {
        return ServiceAccessor.getBillingHistoryService().updateBillingHistory(billingHistoryRequest);
    }
}
