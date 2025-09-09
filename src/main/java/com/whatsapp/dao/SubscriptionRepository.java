package com.whatsapp.dao;

import com.whatsapp.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    Optional<Subscription> findByIdAndIsActive(Long id, boolean isActive);

    List<Subscription> findAllByIsActive(boolean isActive);
}
