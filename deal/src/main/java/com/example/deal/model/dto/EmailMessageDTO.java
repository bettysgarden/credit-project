package com.example.deal.model.dto;

import com.example.deal.model.enums.ThemeEnum;
import lombok.Data;

@Data
public class EmailMessageDTO {
    private String address;
    private ThemeEnum theme;
    private Integer applicationId;
}
