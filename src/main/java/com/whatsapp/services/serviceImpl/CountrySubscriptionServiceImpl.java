package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.CountrySubscriptionRequest;
import com.whatsapp.dto.response.CountryResponse;
import com.whatsapp.dto.response.CountrySubscriptionResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.Country;
import com.whatsapp.entity.CountrySubscription;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.CountrySubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountrySubscriptionServiceImpl implements CountrySubscriptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountrySubscriptionServiceImpl.class);

    @Override
    public ResponseDto<CountrySubscriptionResponse> addCountrySubscription(CountrySubscriptionRequest countrySubscriptionRequest) {
        LOGGER.info("[CountrySubscriptionServiceImpl >> addCountrySubscription] Adding new country-subscription: {}", countrySubscriptionRequest.getId());
        ResponseDto<CountrySubscriptionResponse> response = new ResponseDto<>();
        try{
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if(optionalUser.isEmpty()){
                response.setCode(0);
                response.setMessage("country-subscription not found.");
                return response;
            }
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if(optionalCompany.isEmpty()){
                response.setCode(0);
                response.setMessage("Company not found.");
                return response;
            }
            Optional<Country> optionalCountry = RepositoryAccessor.getCountryRepository().findByIdAndIsActive(countrySubscriptionRequest.getCountryId(), true);
            if(optionalCountry.isEmpty()){
                response.setCode(0);
                response.setMessage("Country not found.");
                return response;
            }
            CountrySubscription countrySubscription = CountrySubscription.builder()
                    .user(optionalUser.get())
                    .company(optionalCompany.get())
                    .country(optionalCountry.get())
                    .marketingCost(countrySubscriptionRequest.getMarketingCost())
                    .userCost(countrySubscriptionRequest.getUserCost())
                    .utilityCost(countrySubscriptionRequest.getUtilityCost())
                    .authenticationCost(countrySubscriptionRequest.getAuthenticationCost())
                    .build();
            countrySubscription.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getCountrySubscriptionRepository().save(countrySubscription);
            response.setData(CountrySubscriptionResponse.builder()
                    .id(countrySubscription.getId())
                    .countryId(countrySubscription.getCountry().getId())
                    .countryName(countrySubscription.getCountry().getName())
                    .companyId(countrySubscription.getCompany().getId())
                    .companyName(countrySubscription.getCompany().getName())
                    .userId(countrySubscription.getUser().getId())
                    .marketingCost(countrySubscription.getMarketingCost())
                    .userCost(countrySubscription.getUserCost())
                    .utilityCost(countrySubscription.getUtilityCost())
                    .authenticationCost(countrySubscription.getAuthenticationCost())
                    .build());
            response.setCode(1);
            response.setMessage("country-subscription added successfully");
            return response;

        }catch (Exception e){
            LOGGER.error("[CountrySubscriptionServiceImpl >> addCountrySubscription] Exception occurred while adding country-subscription", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<CountrySubscriptionResponse> getCountrySubscriptionById(Long id) {
        LOGGER.info("[CountrySubscriptionServiceImpl >> getCountrySubscriptionById] Fetching country-subscription by id: {}", id);
        ResponseDto<CountrySubscriptionResponse> response = new ResponseDto<>();
        try{
            Optional<CountrySubscription> optionalCountrySubscription = RepositoryAccessor.getCountrySubscriptionRepository().findByIdAndIsActive(id, true);
            if(optionalCountrySubscription.isEmpty()){
                response.setCode(0);
                response.setMessage("country-subscription not found.");
                return response;
            }
            CountrySubscription countrySubscription = optionalCountrySubscription.get();
            response.setData(CountrySubscriptionResponse.builder()
                    .id(countrySubscription.getId())
                    .countryId(countrySubscription.getCountry().getId())
                    .countryName(countrySubscription.getCountry().getName())
                    .companyId(countrySubscription.getCompany().getId())
                    .companyName(countrySubscription.getCompany().getName())
                    .userId(countrySubscription.getUser().getId())
                    .marketingCost(countrySubscription.getMarketingCost())
                    .userCost(countrySubscription.getUserCost())
                    .utilityCost(countrySubscription.getUtilityCost())
                    .authenticationCost(countrySubscription.getAuthenticationCost())
                    .build());
            response.setCode(1);
            response.setMessage("country-subscription fetched successfully");
            return response;

        }catch (Exception e){
            LOGGER.error("[CountrySubscriptionServiceImpl >> getCountrySubscriptionById] Exception occurred while fetching country-subscription", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<List<CountrySubscriptionResponse>> getAllCountry() {
        LOGGER.info("[CountrySubscriptionServiceImpl >> getAllCountry] Fetching all country-subscription");
        ResponseDto<List<CountrySubscriptionResponse>> response = new ResponseDto<>();
        try{
            List<CountrySubscription> countrySubscriptionList = RepositoryAccessor.getCountrySubscriptionRepository().findAll();
            response.setData(countrySubscriptionList.stream().map(countrySubscription -> CountrySubscriptionResponse.builder()
                    .id(countrySubscription.getId())
                    .countryId(countrySubscription.getCountry().getId())
                    .countryName(countrySubscription.getCountry().getName())
                    .companyId(countrySubscription.getCompany().getId())
                    .companyName(countrySubscription.getCompany().getName())
                    .userId(countrySubscription.getUser().getId())
                    .marketingCost(countrySubscription.getMarketingCost())
                    .userCost(countrySubscription.getUserCost())
                    .utilityCost(countrySubscription.getUtilityCost())
                    .authenticationCost(countrySubscription.getAuthenticationCost())
                    .build()).toList());
            response.setCode(1);
            response.setMessage("country-subscription fetched successfully");
            return response;

        }catch (Exception e){
            LOGGER.error("[CountrySubscriptionServiceImpl >> getAllCountry] Exception occurred while fetching country-subscription", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<CountrySubscriptionResponse> updateCountrySubscription(CountrySubscriptionRequest countrySubscriptionRequest) {
        LOGGER.info("[CountrySubscriptionServiceImpl >> updateCountrySubscription] Update country-subscription");
        ResponseDto<CountrySubscriptionResponse> response = new ResponseDto<>();
        try {
            Optional<CountrySubscription> optionalCountrySubscription = RepositoryAccessor.getCountrySubscriptionRepository().findByIdAndIsActive(countrySubscriptionRequest.getId(), true);
            if (optionalCountrySubscription.isEmpty()) {
                response.setCode(0);
                response.setMessage("country-subscription not found.");
                return response;
            }
            CountrySubscription countrySubscription = optionalCountrySubscription.get();
            Optional<Country> optionalCountry = RepositoryAccessor.getCountryRepository().findByIdAndIsActive(countrySubscriptionRequest.getCountryId(), true);
            if (optionalCountry.isEmpty()) {
                response.setCode(0);
                response.setMessage("Country not found.");
                return response;
            }
            countrySubscription.setCountry(optionalCountry.get());
            countrySubscription.setMarketingCost(countrySubscriptionRequest.getMarketingCost());
            countrySubscription.setUserCost(countrySubscriptionRequest.getUserCost());
            countrySubscription.setUtilityCost(countrySubscriptionRequest.getUtilityCost());
            countrySubscription.setAuthenticationCost(countrySubscriptionRequest.getAuthenticationCost());
            RepositoryAccessor.getCountrySubscriptionRepository().save(countrySubscription);
            response.setData(CountrySubscriptionResponse.builder()
                    .id(countrySubscription.getId())
                    .countryId(countrySubscription.getCountry().getId())
                    .countryName(countrySubscription.getCountry().getName())
                    .companyId(countrySubscription.getCompany().getId())
                    .companyName(countrySubscription.getCompany().getName())
                    .userId(countrySubscription.getUser().getId())
                    .marketingCost(countrySubscription.getMarketingCost())
                    .userCost(countrySubscription.getUserCost())
                    .utilityCost(countrySubscription.getUtilityCost())
                    .authenticationCost(countrySubscription.getAuthenticationCost())
                    .build());
            response.setCode(1);
            response.setMessage("country-subscription updated successfully");
            return response;
        } catch (Exception e) {
            LOGGER.error("[CountrySubscriptionServiceImpl >> updateCountrySubscription] Exception occurred while updating country-subscription", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

        @Override
        public ResponseDto<String> deleteCountrySubscription (Long id){
            LOGGER.info("[CountrySubscriptionServiceImpl >> deleteCountrySubscription] Deleting country-subscription by id: {}", id);
            ResponseDto<String> response = new ResponseDto<>();
            try {
                Optional<CountrySubscription> optionalCountrySubscription = RepositoryAccessor.getCountrySubscriptionRepository().findByIdAndIsActive(id, true);
                if (optionalCountrySubscription.isEmpty()) {
                    response.setCode(0);
                    response.setMessage("country-subscription not found.");
                    return response;
                }
                CountrySubscription countrySubscription = optionalCountrySubscription.get();
                countrySubscription.setActive(false);
                RepositoryAccessor.getCountrySubscriptionRepository().save(countrySubscription);
                response.setCode(1);
                response.setMessage("country-subscription deleted successfully");
                return response;

            } catch (Exception e) {
                LOGGER.error("[CountrySubscriptionServiceImpl >> deleteCountrySubscription] Exception occurred while deleting country-subscription", e);
                response.setCode(0);
                response.setMessage("Internal server error");
                return response;
            }
        }
}
