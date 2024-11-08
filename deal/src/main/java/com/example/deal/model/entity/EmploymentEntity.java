package com.example.deal.model.entity;

import jakarta.persistence.*;
        import java.math.BigDecimal;

@Table(name = "employment")
public class EmploymentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private EmploymentStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position", referencedColumnName = "id", nullable = false)
    private PositionEntity position;

    @Column(name = "employer_inn", length = 12)
    private String employerInn;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;

    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;

}