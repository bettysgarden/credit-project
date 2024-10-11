package ru.test.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfferDTO {
    @Schema
    private Long applicationId;

    @Schema
    private BigDecimal requestedAmount;

    @Schema
    private BigDecimal totalAmount;

    @Schema
    private Integer term;

    @Schema
    private BigDecimal monthlyPayment;

    @Schema
    private BigDecimal rate;

    @Schema
    private Boolean isInsuranceEnabled;

    @Schema
    private Boolean isSalaryClient;
}
