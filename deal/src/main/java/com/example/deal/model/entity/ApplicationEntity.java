package com.example.deal.model.entity;

import com.example.deal.model.entity.jsonb.AppliedLoanOffer;
import com.example.deal.model.entity.jsonb.StatusHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application")
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private ClientEntity clientId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private CreditEntity creditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private ApplicationStatusEntity status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer")
    private AppliedLoanOffer appliedOffer;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private Integer sessionCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history", nullable = false)
    private List<StatusHistory> statusHistory;
}
