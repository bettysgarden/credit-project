package com.example.deal.model.entity;

import com.example.deal.model.enums.PositionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Table(name = "marital_status")
public class MaritalStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PositionEnum title;
}
