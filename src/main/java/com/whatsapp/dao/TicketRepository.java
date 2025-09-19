package com.whatsapp.dao;

import com.whatsapp.entity.Company;
import com.whatsapp.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    Optional<Ticket> findByIdAndIsActive(Long id, boolean isActive);

    List<Ticket> findAllByIsActive(boolean isActive);

    Optional<Ticket> findTopByCompanyOrderByIdDesc(Company company);

    List<Ticket> findByIsActive(boolean isActive);
}
