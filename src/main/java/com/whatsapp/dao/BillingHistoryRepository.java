package com.whatsapp.dao;

import com.whatsapp.entity.BillingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingHistoryRepository extends JpaRepository<BillingHistory, Long> {

    Optional<BillingHistory> findByUserIdAndIsActive(Long id, boolean isActive);

    Optional<BillingHistory> findByIdAndIsActive(Long id, boolean isActive);

    List<BillingHistory> findByIsActive(boolean b);
}
