package com.example.deal.model.dto;

import com.example.deal.model.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FinishRegistrationRequest {
    private GenderEnum gender;
    private Integer maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private Integer employment;
    private String account;
}