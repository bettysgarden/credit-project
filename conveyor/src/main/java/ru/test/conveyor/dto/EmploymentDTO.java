package ru.test.conveyor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.test.conveyor.enums.EmploymentStatus;
import ru.test.conveyor.enums.Position;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {
    @Schema
    private EmploymentStatus employmentStatus;

    @Schema
    private String employerINN;

    @Schema
    private BigDecimal salary;

    @Schema
    private Position position;

    @Schema
    private Integer workExperienceTotal;

    @Schema
    private Integer workExperienceCurrent;

}
