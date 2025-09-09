package com.whatsapp.controller;

import com.whatsapp.dto.request.CompanyRequest;
import com.whatsapp.dto.request.CountryRequest;
import com.whatsapp.dto.response.CompanyResponse;
import com.whatsapp.dto.response.CountryResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("country")
public class CountryController {
    @PostMapping("add")
    ResponseDto<CountryResponse> addCountry(@RequestBody CountryRequest countryRequest) {
        return ServiceAccessor.getCountryService().addCountry(countryRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<CountryResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getCountryService().getCountryById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<CountryResponse>> getAll() {
        return ServiceAccessor.getCountryService().getAllCountry();
    }

    @PutMapping("update")
    ResponseDto<CountryResponse> updateDepartment(@RequestBody CountryRequest countryRequest) {
        return ServiceAccessor.getCountryService().updateCountry(countryRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteDepartment(@PathVariable Long id) {
        return ServiceAccessor.getCountryService().deleteCountry(id);
    }
}
