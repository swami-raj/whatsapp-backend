package com.whatsapp.controller;

import com.whatsapp.dto.request.CountrySubscriptionRequest;
import com.whatsapp.dto.response.CountrySubscriptionResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("country-subscription")
public class CountrySubscriptionController {
    @PostMapping("add")
    ResponseDto<CountrySubscriptionResponse> addCountrySubscription(@RequestBody CountrySubscriptionRequest countrySubscriptionRequest) {
        return ServiceAccessor.getCountrySubscriptionService().addCountrySubscription(countrySubscriptionRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<CountrySubscriptionResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getCountrySubscriptionService().getCountrySubscriptionById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<CountrySubscriptionResponse>> getAll() {
        return ServiceAccessor.getCountrySubscriptionService().getAllCountry();
    }

    @PutMapping("update")
    ResponseDto<CountrySubscriptionResponse> updateCountrySubscription(@RequestBody CountrySubscriptionRequest countrySubscriptionRequest) {
        return ServiceAccessor.getCountrySubscriptionService().updateCountrySubscription(countrySubscriptionRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteCountrySubscription(@PathVariable Long id) {
        return ServiceAccessor.getCountrySubscriptionService().deleteCountrySubscription(id);
    }
}
