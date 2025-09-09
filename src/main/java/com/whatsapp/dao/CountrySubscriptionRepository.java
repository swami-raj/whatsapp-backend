package com.whatsapp.dao;

import com.whatsapp.entity.CountrySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountrySubscriptionRepository extends JpaRepository<CountrySubscription,Long> {

    Optional<CountrySubscription> findByIdAndIsActive(Long id, boolean isActive);
}
