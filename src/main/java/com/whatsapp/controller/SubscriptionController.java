package com.whatsapp.controller;

import com.whatsapp.dto.request.CompanyRequest;
import com.whatsapp.dto.request.SubscriptionRequest;
import com.whatsapp.dto.response.CompanyResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubscriptionResponse;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subscription")
public class SubscriptionController {

    @PostMapping("add")
    ResponseDto<SubscriptionResponse> addSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        return ServiceAccessor.getSubscriptionService().addSubscription(subscriptionRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<SubscriptionResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getSubscriptionService().getSubscriptionById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<SubscriptionResponse>> getAll() {
        return ServiceAccessor.getSubscriptionService().getAllSubscription();
    }

    @PutMapping("update")
    ResponseDto<SubscriptionResponse> updateSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        return ServiceAccessor.getSubscriptionService().updateSubscription(subscriptionRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteSubscription(@PathVariable Long id) {
        return ServiceAccessor.getSubscriptionService().deleteSubscription(id);
    }
}
