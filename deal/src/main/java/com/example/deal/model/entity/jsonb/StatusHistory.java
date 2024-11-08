package com.example.deal.model.entity.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory implements Serializable {
    private String status;
    private Date time;
    private Integer changeTypeId;
}
