package com.example.deal.model.entity.jsonb;

import com.example.deal.model.enums.ApplicationStatusEnum;
import com.example.deal.model.enums.ChangeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory implements Serializable {
    private ApplicationStatusEnum status;
    private LocalDateTime time;
    private ChangeTypeEnum changeType;
}
