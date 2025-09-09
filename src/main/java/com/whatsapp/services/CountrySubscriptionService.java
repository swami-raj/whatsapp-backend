package com.whatsapp.services;

import com.whatsapp.dto.request.CountrySubscriptionRequest;
import com.whatsapp.dto.response.CountrySubscriptionResponse;
import com.whatsapp.dto.response.ResponseDto;

import java.util.List;

public interface CountrySubscriptionService {
    ResponseDto<CountrySubscriptionResponse> addCountrySubscription(CountrySubscriptionRequest countrySubscriptionRequest);

    ResponseDto<CountrySubscriptionResponse> getCountrySubscriptionById(Long id);

    ResponseDto<List<CountrySubscriptionResponse>> getAllCountry();

    ResponseDto<CountrySubscriptionResponse> updateCountrySubscription(CountrySubscriptionRequest countrySubscriptionRequest);

    ResponseDto<String> deleteCountrySubscription(Long id);
}
