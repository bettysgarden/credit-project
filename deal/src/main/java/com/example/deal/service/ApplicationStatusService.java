package com.example.deal.service;

import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.enums.ApplicationStatusEnum;

public interface ApplicationStatusService {
    void updateStatus(ApplicationEntity applicationEntity, ApplicationStatusEnum applicationStatusEnum);
}
