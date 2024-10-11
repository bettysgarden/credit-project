package ru.test.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.test.conveyor.enums.Gender;
import ru.test.conveyor.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataDTO {

    @Schema
    private BigDecimal amount;

    @Schema
    private Integer term;

    @Schema
    private String firstName;

    @Schema
    private String lastName;

    @Schema
    private Gender gender;

    @Schema
    private LocalDate birthDate;

    @Schema
    private String passportSeries;

    @Schema
    private String passportNumber;

    @Schema
    private LocalDate passportIssueDate;

    @Schema
    private String passportIssueBranch;

    @Schema
    private MaritalStatus maritalStatus;

    @Schema
    private Integer dependentAmount;

    @Schema
    private EmploymentDTO employment;

    @Schema
    private String account;

    @Schema
    private Boolean isInsuranceEnabled;

    @Schema
    private Boolean isSalaryClient;
}
