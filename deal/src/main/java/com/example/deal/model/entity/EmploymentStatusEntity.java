package com.example.deal.model.entity;

import com.example.deal.model.enums.EmploymentStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Table(name = "employment_status")
public class EmploymentStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EmploymentStatusEnum title;
}
