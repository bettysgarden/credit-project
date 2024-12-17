package com.example.deal.model.entity;

import com.example.deal.model.enums.ThemeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String address;
    private ThemeEnum theme;
    private Integer applicationId;
}
