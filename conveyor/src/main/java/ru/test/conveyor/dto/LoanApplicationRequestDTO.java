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
    @Schema(description = "Requested loan amount")
    private BigDecimal amount;

    @Schema(description = "Loan term in months")
    private Integer term;

    @Schema(description = "Applicant's first name")
    private String firstName;

    @Schema(description = "Applicant's last name")
    private String lastName;

    @Schema(description = "Applicant's middle name")
    private String middleName;

    @Schema(description = "Applicant's email address")
    private String email;

    @Schema(description = "Applicant's birth date")
    private LocalDate birthdate;

    @Schema(description = "Passport series")
    private String passportSeries;

    @Schema(description = "Passport number")
    private String passportNumber;
}
