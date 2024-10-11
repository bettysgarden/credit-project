package ru.test.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElement {
    @Schema
    private Integer number;

    @Schema
    private LocalDate date;

    @Schema
    private BigDecimal totalPayment;

    @Schema
    private BigDecimal interestPayment;

    @Schema
    private BigDecimal debtPayment;

    @Schema
    private BigDecimal remainingDebt;
}
