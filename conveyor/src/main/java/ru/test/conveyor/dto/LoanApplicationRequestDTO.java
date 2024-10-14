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
public class LoanApplicationRequestDTO {
    @Schema
    private BigDecimal amount;

    @Schema
    private Integer term;

    @Schema
    private String firstName;

    @Schema
    private String lastName;

    @Schema
    private String middleName;

    @Schema
    private String email;

    @Schema
    private LocalDate birthdate;

    @Schema
    private String passportSeries;

    @Schema
    private String passportNumber;
}
