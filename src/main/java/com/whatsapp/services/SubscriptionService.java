package com.whatsapp.services;

import com.whatsapp.dto.request.SubscriptionRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    ResponseDto<SubscriptionResponse> addSubscription(SubscriptionRequest subscriptionRequest);

    ResponseDto<SubscriptionResponse> getSubscriptionById(Long id);

    ResponseDto<List<SubscriptionResponse>> getAllSubscription();

    ResponseDto<SubscriptionResponse> updateSubscription(SubscriptionRequest subscriptionRequest);

    ResponseDto<String> deleteSubscription(Long id);
}
