package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.CountryRequest;
import com.whatsapp.dto.response.CountryResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Country;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Override
    public ResponseDto<CountryResponse> addCountry(CountryRequest countryRequest) {
        LOGGER.info("[CountryServiceImpl >> addCountry] Adding new country: {}", countryRequest.getName());
        ResponseDto<CountryResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            Optional<Country> optionalCountry = RepositoryAccessor.getCountryRepository().findByNameAndIsActive(countryRequest.getName(), true);
            if (optionalCountry.isPresent()) {
                response.setCode(0);
                response.setMessage("country already exists.");
                return response;
            }
            Country country =
                    Country.builder()
                            .name(countryRequest.getName())
                            .code(countryRequest.getCode())
                            .build();
            country.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getCountryRepository().save(country);
            response.setData(mapToResponse(country));
            response.setCode(1);
            response.setMessage("country added successfully");
        } catch (Exception e) {
            LOGGER.error("[CountryServiceImpl >> addCountry] Exception occurred while adding country", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<CountryResponse> getCountryById(Long id) {
        LOGGER.info("[CountryServiceImpl >> getCountryById] Fetching country by id: {}", id);
        ResponseDto<CountryResponse> response = new ResponseDto<>();
        try {
            Optional<Country> optionalCountry = RepositoryAccessor.getCountryRepository().findByIdAndIsActive(id, true);
            if (optionalCountry.isEmpty()) {
                response.setCode(0);
                response.setMessage("country not found.");
                return response;
            }
            response.setData(mapToResponse(optionalCountry.get()));
            response.setCode(1);
            response.setMessage("country fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[CountryServiceImpl >> getCountryById] Exception occurred while fetching country", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<CountryResponse>> getAllCountry() {
        LOGGER.info("[CountryServiceImpl >> getAllCountry] Fetching all country");
        ResponseDto<List<CountryResponse>> response = new ResponseDto<>();
        try {
            List<Country> countryList = RepositoryAccessor.getCountryRepository().findAll();
            response.setData(countryList.stream().map(this::mapToResponse).toList());
            response.setCode(1);
            response.setMessage("country fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[CountryServiceImpl >> getAllCountry] Exception occurred while fetching country", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return  response;

    }

    @Override
    public ResponseDto<CountryResponse> updateCountry(CountryRequest countryRequest) {
        LOGGER.info("[CountryServiceImpl >> updateCountry] Updating country: {}", countryRequest.getName());
        ResponseDto<CountryResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            Optional<Country> optionalCountry = RepositoryAccessor.getCountryRepository().findByIdAndIsActive(countryRequest.getId(), true);
            if (optionalCountry.isEmpty()) {
                response.setCode(0);
                response.setMessage("country not exists.");
                return response;
            }
            Country country = optionalCountry.get();
            country.setName(countryRequest.getName());
            country.setCode(countryRequest.getCode());
            country.setUpdatedBy(optionalUser.get());
            RepositoryAccessor.getCountryRepository().save(country);
            response.setData(mapToResponse(country));
            response.setCode(1);
            response.setMessage("country updated successfully");
        } catch (Exception e) {
            LOGGER.error("[CountryServiceImpl >> updateCountry] Exception occurred while updating country", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteCountry(Long id) {
        LOGGER.info("[CountryServiceImpl >> deleteCountry] Deleting country by id: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<Country> optionalCountry = RepositoryAccessor.getCountryRepository().findByIdAndIsActive(id, true);
            if (optionalCountry.isEmpty()) {
                response.setCode(0);
                response.setMessage("country not found.");
                return response;
            }
            Country country = optionalCountry.get();
            country.setActive(false);
            RepositoryAccessor.getCountryRepository().save(country);
            response.setCode(1);
            response.setMessage("country deleted successfully");
        } catch (Exception e) {
            LOGGER.error("[CountryServiceImpl >> deleteCountry] Exception occurred while deleting country", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }
    private CountryResponse mapToResponse(Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .code(country.getCode())
                .build();
    }
}
