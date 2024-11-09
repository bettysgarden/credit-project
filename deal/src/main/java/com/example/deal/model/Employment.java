package com.example.deal.model;

import com.example.deal.model.enums.EmploymentStatusEnum;
import com.example.deal.model.enums.PositionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment {
    private EmploymentStatusEnum employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private PositionEnum position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}
