package com.example.deal.repository;

import com.example.deal.model.entity.ApplicationStatusEntity;
import com.example.deal.model.enums.ApplicationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatusEntity, Long> {
    Optional<ApplicationStatusEntity> findByTitle(ApplicationStatusEnum title);
}
