package com.example.deal.model.entity;

import com.example.deal.model.enums.GenderEnum;
import com.example.deal.model.enums.PositionEnum;
import jakarta.persistence.*;

@Table(name = "gender")
public class GenderEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private GenderEnum title;

}
