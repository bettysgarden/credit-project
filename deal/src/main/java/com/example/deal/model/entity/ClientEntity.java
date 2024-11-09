package com.example.deal.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Table(name = "client")
public class ClientEntity extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "dependent_amount", nullable = false)
    private Integer dependentAmount;

    @Column(name = "account")
    private Integer account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender", referencedColumnName = "id", nullable = false)
    private GenderEntity gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marital_status", referencedColumnName = "id", nullable = false)
    private MaritalStatusEntity maritalStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id", referencedColumnName = "id", nullable = false)
    private EmploymentEntity employment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "id", nullable = false)
    private PassportEntity passport;

}
