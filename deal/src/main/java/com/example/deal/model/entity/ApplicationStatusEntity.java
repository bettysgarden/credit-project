package com.example.deal.model.entity;

import com.example.deal.model.enums.ApplicationStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Table(name = "application_status")
public class ApplicationStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ApplicationStatusEnum title;

}
