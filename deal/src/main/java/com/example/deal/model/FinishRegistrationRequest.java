package com.example.deal.model;

import com.example.deal.model.enums.GenderEnum;
import com.example.deal.model.enums.MaritalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FinishRegistrationRequest {
    private GenderEnum gender;
    private MaritalStatusEnum maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private Employment employment;

    private String account;
}