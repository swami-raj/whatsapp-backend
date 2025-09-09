package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.BillingHistoryRequest;
import com.whatsapp.dto.response.BillingHistoryResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.BillingHistory;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.User;
import com.whatsapp.enums.Enums;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.BillingHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillingHistoryServiceImpl implements BillingHistoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingHistoryServiceImpl.class);

    @Override
    public ResponseDto<BillingHistoryResponse> addBillingHistory(BillingHistoryRequest billingHistoryRequest) {
        LOGGER.info("[BillingHistoryServiceImpl >> addBillingHistory] Adding new billing history for user: {}", billingHistoryRequest.getUserId());
        ResponseDto<BillingHistoryResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("User not found.");
                return response;
            }

            Optional<User> optionalBillingUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(billingHistoryRequest.getUserId(), true);
            if (optionalBillingUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("Billing user not found.");
                return response;
            }

            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(optionalBillingUser.get().getCompany().getId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("Company not found.");
                return response;
            }

            User billingUser = optionalBillingUser.get();

            double currentBalance = RepositoryAccessor.getBillingHistoryRepository().findByUserIdAndIsActive(billingUser.getId(),true)
                    .stream().mapToDouble(bh -> {
                        double amt = bh.getAmount();
                        return bh.getPaymentType() == Enums.PaymentType.CREDIT ? amt : -amt;
                    }).sum();

            Double updatedBalance = currentBalance;

            if (Enums.PaymentType.CREDIT.name().equalsIgnoreCase(billingHistoryRequest.getPaymentType())) {
                updatedBalance += billingHistoryRequest.getAmount();
            } else if (Enums.PaymentType.DEBIT.name().equalsIgnoreCase(billingHistoryRequest.getPaymentType())) {
                updatedBalance -= billingHistoryRequest.getAmount();
            } else {
                response.setCode(0);
                response.setMessage("Invalid payment type.");
                return response;
            }

            BillingHistory billingHistory = BillingHistory.builder()
                    .user(billingUser)
                    .company(optionalCompany.get())
                    .amount(billingHistoryRequest.getAmount())
                    .transactionId(UUID.randomUUID().toString())
                    .paymentType(Enums.PaymentType.valueOf(billingHistoryRequest.getPaymentType().toUpperCase()))
                    .build();

            billingHistory.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getBillingHistoryRepository().save(billingHistory);

            BillingHistoryResponse billingResponse = mapToResponse(billingHistory);

            response.setData(billingResponse);
            response.setCode(1);
            response.setMessage("Billing history added successfully");

        } catch (Exception e) {
            LOGGER.error("[BillingHistoryServiceImpl >> addBillingHistory] Exception occurred while adding billing history", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }



    @Override
    public ResponseDto<BillingHistoryResponse> getBillingHistoryById(Long id) {
        LOGGER.info("[BillingHistoryServiceImpl >> getBillingHistoryById] Fetching billing history by id: {}", id);
        ResponseDto<BillingHistoryResponse> response = new ResponseDto<>();
        try {
            Optional<BillingHistory> optionalBillingHistory = RepositoryAccessor.getBillingHistoryRepository().findByIdAndIsActive(id, true);
            if (optionalBillingHistory.isEmpty()) {
                response.setCode(0);
                response.setMessage("Billing history not found.");
                return response;
            }
            response.setData(mapToResponse(optionalBillingHistory.get()));
            response.setCode(1);
            response.setMessage("Billing history fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[BillingHistoryServiceImpl >> getBillingHistoryById] Exception occurred while fetching billing history", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<BillingHistoryResponse>> getAllBillingHistory() {
        LOGGER.info("[BillingHistoryServiceImpl >> getAllBillingHistory] Fetching all billing history");
        ResponseDto<List<BillingHistoryResponse>> response = new ResponseDto<>();
        try {
            List<BillingHistory> billingHistoryList = RepositoryAccessor.getBillingHistoryRepository().findByIsActive(true);
            response.setData(billingHistoryList.stream().map(this::mapToResponse).toList());
            response.setCode(1);
            response.setMessage("Billing history fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[BillingHistoryServiceImpl >> getAllBillingHistory] Exception occurred while fetching billing history", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<BillingHistoryResponse> updateBillingHistory(BillingHistoryRequest billingHistoryRequest) {
        LOGGER.info("[BillingHistoryServiceImpl >> updateBillingHistory] Updating billing history for id: {}", billingHistoryRequest.getId());
        ResponseDto<BillingHistoryResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("User not found.");
                return response;
            }
            Optional<BillingHistory> optionalBillingHistory = RepositoryAccessor.getBillingHistoryRepository().findByIdAndIsActive(billingHistoryRequest.getId(), true);
            if (optionalBillingHistory.isEmpty()) {
                response.setCode(0);
                response.setMessage("Billing history not found.");
                return response;
            }

            BillingHistory billingHistory = optionalBillingHistory.get();
            Optional<User> optionalBillingUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(billingHistoryRequest.getUserId(), true);
            if (optionalBillingUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("Billing user not found.");
                return response;
            }

            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(optionalBillingUser.get().getCompany().getId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("Company not found.");
                return response;
            }

            billingHistory.setUser(optionalBillingUser.get());
            billingHistory.setCompany(optionalCompany.get());
            billingHistory.setAmount(billingHistoryRequest.getAmount());
            billingHistory.setPaymentType(Enums.PaymentType.valueOf(billingHistoryRequest.getPaymentType().toUpperCase()));
            billingHistory.setUpdatedBy(optionalUser.get());

            RepositoryAccessor.getBillingHistoryRepository().save(billingHistory);
            double currentBalance = RepositoryAccessor.getBillingHistoryRepository()
                    .findByUserIdAndIsActive(optionalBillingUser.get().getId(), true)
                    .stream()
                    .mapToDouble(bh -> {
                        double amt = bh.getAmount();
                        return bh.getPaymentType() == Enums.PaymentType.CREDIT ? amt : -amt;
                    })
                    .sum();

            BillingHistoryResponse billingResponse = mapToResponse(billingHistory);

            response.setData(billingResponse);
            response.setCode(1);
            response.setMessage("Billing history updated successfully");

        } catch (Exception e) {
            LOGGER.error("[BillingHistoryServiceImpl >> updateBillingHistory] Exception occurred while updating billing history", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }
    private BillingHistoryResponse mapToResponse(BillingHistory billingHistory) {
        return BillingHistoryResponse.builder()
                .id(billingHistory.getId())
                .userId(billingHistory.getUser().getId())
                .amount(billingHistory.getAmount())
                .transactionId(billingHistory.getTransactionId())
                .paymentType(billingHistory.getPaymentType().name())
                .build();
    }

}
