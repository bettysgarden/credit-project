package com.example.deal.model.entity;

import com.example.deal.model.enums.PositionEnum;
import jakarta.persistence.*;

@Table(name = "position")
public class PositionEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PositionEnum title;

}