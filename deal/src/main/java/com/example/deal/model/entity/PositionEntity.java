package com.example.deal.model.entity;

import com.example.deal.model.enums.PositionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "position")
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PositionEnum title;

}