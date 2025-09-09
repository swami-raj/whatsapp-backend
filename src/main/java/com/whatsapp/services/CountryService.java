package com.whatsapp.services;

import com.whatsapp.dto.request.CountryRequest;
import com.whatsapp.dto.response.CountryResponse;
import com.whatsapp.dto.response.ResponseDto;

import java.util.List;

public interface CountryService {
    ResponseDto<CountryResponse> addCountry(CountryRequest countryRequest);

    ResponseDto<CountryResponse> getCountryById(Long id);

    ResponseDto<List<CountryResponse>> getAllCountry();

    ResponseDto<CountryResponse> updateCountry(CountryRequest countryRequest);

    ResponseDto<String> deleteCountry(Long id);
}
