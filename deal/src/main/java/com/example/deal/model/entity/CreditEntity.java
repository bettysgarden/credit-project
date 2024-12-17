package com.example.deal.model.entity;

import com.example.deal.model.entity.jsonb.PaymentScheduleElement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credit")
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "term", nullable = false)
    private Integer term;

    @Column(name = "monthly_payment", nullable = false)
    private BigDecimal monthlyPayment;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "psk", nullable = false)
    private BigDecimal psk;

    @Column(name = "insurance_enable", nullable = false)
    private Boolean isInsuranceEnabled;

    @Column(name = "salary_client", nullable = false)
    private Boolean isSalaryClient;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule", columnDefinition = "jsonb", nullable = false)
    private List<PaymentScheduleElement> paymentSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private CreditStatusEntity status;
}
