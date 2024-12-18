package ru.test.dossier.model.dto;

import lombok.Data;
import ru.test.dossier.model.enums.ThemeEnum;

@Data
public class EmailMessageDTO {
    private String address;
    private ThemeEnum theme;
    private Integer applicationId;
}
