package com.example.deal.service.implementation;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
ApplicationService: реализует логику обработки и сохранения сущности Application,
включая создание новой заявки, присвоение статуса, и обновление истории статусов.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository repository;


    @Override
    public Long createApplication(LoanApplicationRequest loanApplicationRequest, Long clientId) {
        return 0L;
    }
}
