package com.whatsapp.services;

import com.whatsapp.dto.request.BillingHistoryRequest;
import com.whatsapp.dto.response.BillingHistoryResponse;
import com.whatsapp.dto.response.ResponseDto;

import java.util.List;

public interface BillingHistoryService {
    ResponseDto<BillingHistoryResponse> addBillingHistory(BillingHistoryRequest billingHistoryRequest);

    ResponseDto<BillingHistoryResponse> getBillingHistoryById(Long id);

    ResponseDto<List<BillingHistoryResponse>> getAllBillingHistory();

    ResponseDto<BillingHistoryResponse> updateBillingHistory(BillingHistoryRequest billingHistoryRequest);
}
