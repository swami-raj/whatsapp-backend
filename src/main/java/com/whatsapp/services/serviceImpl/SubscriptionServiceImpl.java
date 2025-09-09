package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.SubscriptionRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubscriptionResponse;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.Subscription;
import com.whatsapp.entity.User;
import com.whatsapp.enums.Enums;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Override
    public ResponseDto<SubscriptionResponse> addSubscription(SubscriptionRequest subscriptionRequest) {
        LOGGER.info("[SubscriptionServiceImpl >> addSubscription] Adding new company: {}", subscriptionRequest.getUserId());
        ResponseDto<SubscriptionResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(optionalUser.get().getCompany().getId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not exists.");
                return response;
            }
            Optional<User> optionalSubscriptionUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(subscriptionRequest.getUserId(), true);
            if (optionalSubscriptionUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("Subscription user does not exist.");
                return response;
            }
            Subscription subscription = Subscription.builder()
                    .user(optionalSubscriptionUser.get())
                    .company(optionalCompany.get())
                    .cloudApiToken(subscriptionRequest.getCloudApiToken())
                    .cloudAppId(subscriptionRequest.getCloudAppId())
                    .cloudWabaId(subscriptionRequest.getCloudWabaId())
                    .detuctionFrom(Enums.DetuctionFrom.valueOf(subscriptionRequest.getDetuctionFrom()))
                    .gateway(Enums.Gateway.valueOf(subscriptionRequest.getGateway()))
                    .webhook(subscriptionRequest.isWebhook())
                    .webhookUrl(subscriptionRequest.getWebhookUrl())
                    .webhookHeaderEnable(subscriptionRequest.isWebhookHeaderEnable())
                    .webhookHeaderValue(subscriptionRequest.getWebhookHeaderValue())
                    .whatsappNumber(subscriptionRequest.getWhatsappNumber())
                    .cloudPhoneNumberId(subscriptionRequest.getCloudPhoneNumberId())
                    .maxCampSize(subscriptionRequest.getMaxCampSize())
                    .expiryDate(subscriptionRequest.getExpiryDate())
                    .build();
            Subscription savedSubscription = RepositoryAccessor.getSubscriptionRepository().save(subscription);

            SubscriptionResponse subscriptionResponse = SubscriptionResponse.builder()
                    .id(savedSubscription.getId())
                    .userId(savedSubscription.getUser().getId())
                    .companyId(savedSubscription.getCompany().getId())
                    .userName(savedSubscription.getUser().getName())
                    .companyName(savedSubscription.getCompany().getName())
                    .detuctionFrom(savedSubscription.getDetuctionFrom().name())
                    .gateway(String.valueOf(savedSubscription.getGateway()))
                    .webhook(savedSubscription.isWebhook())
                    .webhookUrl(savedSubscription.getWebhookUrl())
                    .webhookHeaderEnable(savedSubscription.isWebhookHeaderEnable())
                    .webhookHeaderValue(savedSubscription.getWebhookHeaderValue())
                    .cloudApiToken(savedSubscription.getCloudApiToken())
                    .whatsappNumber(savedSubscription.getWhatsappNumber())
                    .cloudAppId(savedSubscription.getCloudAppId())
                    .cloudWabaId(savedSubscription.getCloudWabaId())
                    .cloudPhoneNumberId(savedSubscription.getCloudPhoneNumberId())
                    .maxCampSize(savedSubscription.getMaxCampSize())
                    .expiryDate(savedSubscription.getExpiryDate())
                    .build();

            response.setCode(1);
            response.setMessage("Subscription created successfully.");
            response.setData(subscriptionResponse);
            return response;

        }catch (Exception e){
            LOGGER.error("[SubscriptionServiceImpl >> addSubscription] Exception occurred while adding company", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<SubscriptionResponse> getSubscriptionById(Long id) {
        ResponseDto<SubscriptionResponse> response = new ResponseDto<>();
        try {
            Optional<Subscription> optionalSubscription = RepositoryAccessor.getSubscriptionRepository().findByIdAndIsActive(id, true);
            if (optionalSubscription.isEmpty()) {
                response.setCode(0);
                response.setMessage("Subscription not found.");
                return response;
            }

            response.setCode(1);
            response.setMessage("Subscription fetched successfully.");
            response.setData(mapToResponse(optionalSubscription.get()));
            return response;

        } catch (Exception e) {
            LOGGER.error("[SubscriptionServiceImpl >> getSubscriptionById] Exception occurred", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<List<SubscriptionResponse>> getAllSubscription() {
        ResponseDto<List<SubscriptionResponse>> response = new ResponseDto<>();
        try {
            List<Subscription> subscriptions = RepositoryAccessor.getSubscriptionRepository().findAllByIsActive(true);
            if (subscriptions.isEmpty()) {
                response.setCode(0);
                response.setMessage("No subscriptions found.");
                response.setData(Collections.emptyList());
                return response;
            }

            List<SubscriptionResponse> subscriptionResponses = subscriptions.stream()
                    .map(this::mapToResponse)
                    .toList();

            response.setCode(1);
            response.setMessage("Subscriptions fetched successfully.");
            response.setData(subscriptionResponses);
            return response;

        } catch (Exception e) {
            LOGGER.error("[SubscriptionServiceImpl >> getAllSubscription] Exception occurred", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<SubscriptionResponse> updateSubscription(SubscriptionRequest subscriptionRequest) {
        ResponseDto<SubscriptionResponse> response = new ResponseDto<>();
        try {
            Optional<Subscription> optionalSubscription = RepositoryAccessor.getSubscriptionRepository().findByIdAndIsActive(subscriptionRequest.getId(), true);
            if (optionalSubscription.isEmpty()) {
                response.setCode(0);
                response.setMessage("Subscription not found.");
                return response;
            }
            Optional<User> optionalSubscriptionUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(subscriptionRequest.getUserId(), true);
            if (optionalSubscriptionUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("Subscription user does not exist.");
                return response;
            }

            Subscription subscription = optionalSubscription.get();
            subscription.setUser(optionalSubscriptionUser.get());
            subscription.setCloudApiToken(subscriptionRequest.getCloudApiToken());
            subscription.setCloudAppId(subscriptionRequest.getCloudAppId());
            subscription.setCloudWabaId(subscriptionRequest.getCloudWabaId());
            subscription.setDetuctionFrom(Enums.DetuctionFrom.valueOf(subscriptionRequest.getDetuctionFrom()));
            subscription.setGateway(Enums.Gateway.valueOf(subscriptionRequest.getGateway()));
            subscription.setWebhook(subscriptionRequest.isWebhook());
            subscription.setWebhookUrl(subscriptionRequest.getWebhookUrl());
            subscription.setWebhookHeaderEnable(subscriptionRequest.isWebhookHeaderEnable());
            subscription.setWebhookHeaderValue(subscriptionRequest.getWebhookHeaderValue());
            subscription.setWhatsappNumber(subscriptionRequest.getWhatsappNumber());
            subscription.setCloudPhoneNumberId(subscriptionRequest.getCloudPhoneNumberId());
            subscription.setMaxCampSize(subscriptionRequest.getMaxCampSize());
            subscription.setExpiryDate(subscriptionRequest.getExpiryDate());

            Subscription updatedSubscription = RepositoryAccessor.getSubscriptionRepository().save(subscription);

            response.setCode(1);
            response.setMessage("Subscription updated successfully.");
            response.setData(mapToResponse(updatedSubscription));
            return response;

        } catch (Exception e) {
            LOGGER.error("[SubscriptionServiceImpl >> updateSubscription] Exception occurred", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }

    @Override
    public ResponseDto<String> deleteSubscription(Long id) {
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<Subscription> optionalSubscription = RepositoryAccessor.getSubscriptionRepository().findByIdAndIsActive(id, true);
            if (optionalSubscription.isEmpty()) {
                response.setCode(0);
                response.setMessage("Subscription not found.");
                return response;
            }

            Subscription subscription = optionalSubscription.get();
            subscription.setActive(false);
            RepositoryAccessor.getSubscriptionRepository().save(subscription);

            response.setCode(1);
            response.setMessage("Subscription deleted successfully.");
            response.setData("Deleted");
            return response;

        } catch (Exception e) {
            LOGGER.error("[SubscriptionServiceImpl >> deleteSubscription] Exception occurred", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return response;
        }
    }
    private SubscriptionResponse mapToResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .companyId(subscription.getCompany().getId())
                .userName(subscription.getUser().getName())
                .companyName(subscription.getCompany().getName())
                .detuctionFrom(subscription.getDetuctionFrom().name())
                .gateway(String.valueOf(subscription.getGateway()))
                .webhook(subscription.isWebhook())
                .webhookUrl(subscription.getWebhookUrl())
                .webhookHeaderEnable(subscription.isWebhookHeaderEnable())
                .webhookHeaderValue(subscription.getWebhookHeaderValue())
                .cloudApiToken(subscription.getCloudApiToken())
                .whatsappNumber(subscription.getWhatsappNumber())
                .cloudAppId(subscription.getCloudAppId())
                .cloudWabaId(subscription.getCloudWabaId())
                .cloudPhoneNumberId(subscription.getCloudPhoneNumberId())
                .maxCampSize(subscription.getMaxCampSize())
                .expiryDate(subscription.getExpiryDate())
                .build();
    }

}
