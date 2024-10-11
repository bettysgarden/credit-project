package ru.test.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {
    @Schema
    private BigDecimal amount;

    @Schema
    private Integer term;

    @Schema
    private BigDecimal monthlyPayment;

    @Schema
    private BigDecimal rate;

    @Schema
    private BigDecimal psk;

    @Schema
    private Boolean isInsuranceEnabled;

    @Schema
    private Boolean isSalaryClient;

    @Schema
    private List<PaymentScheduleElement> paymentSchedule;

}
