package com.example.deal.model.dto;

import com.example.deal.model.enums.GenderEnum;
import com.example.deal.model.enums.MaritalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataRequest {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private GenderEnum gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatusEnum maritalStatus;
    private Integer dependentAmount;
    private EmploymentDTO employment;
    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}

