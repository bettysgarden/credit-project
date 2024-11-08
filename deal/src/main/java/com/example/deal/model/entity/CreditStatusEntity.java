package com.example.deal.model.entity;

import com.example.deal.model.enums.ApplicationStatusEnum;
import jakarta.persistence.*;

@Table(name = "application_status")
public class CreditStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CreditStatusEntity title;
}
