package com.example.deal.service.implementation;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.repository.ClientRepository;
import com.example.deal.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository repository;

    @Override
    public Long createClient(LoanApplicationRequest loanApplicationRequest) {

        return 0L;
    }
}
