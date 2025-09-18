package com.whatsapp.dao;

import com.whatsapp.entity.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatesRepository extends JpaRepository<Candidates,Long> {

    Optional<Candidates> findByIdAndIsActive(Long id, boolean isActive);
}
