package com.example.deal.repository;

import com.example.deal.model.entity.CreditStatusEntity;
import com.example.deal.model.enums.CreditStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface CreditStatusRepository extends JpaRepository<CreditStatusEntity, Long> {
    Optional<CreditStatusEntity> findByTitle(CreditStatusEnum title);

}