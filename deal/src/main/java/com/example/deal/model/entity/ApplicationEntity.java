package com.example.deal.model.entity;

import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import com.example.deal.model.entity.jsonb.StatusHistory;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Table(name = "application")
public class ApplicationEntity extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private ClientEntity clientId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "id", nullable = false)
    private CreditEntity creditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private ApplicationStatusEntity status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer")
    private AppliedLoanOffer appliedOffer;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "sign_date")
    private Date signDate;

    @Column(name = "ses_code")
    private Integer sessionCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history")
    private StatusHistory statusHistory;
}
