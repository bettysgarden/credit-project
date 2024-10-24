package ru.test.conveyor.entity;

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
public class ScoringData {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private Employment employment;
    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}

