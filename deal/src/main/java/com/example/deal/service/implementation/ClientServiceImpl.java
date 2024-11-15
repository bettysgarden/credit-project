package com.example.deal.service.implementation;

import com.example.deal.model.dto.LoanApplicationRequest;
import com.example.deal.model.entity.ClientEntity;
import com.example.deal.model.entity.PassportEntity;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.PassportRepository;
import com.example.deal.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PassportRepository passportRepository;

    @Override
    public Long createClient(LoanApplicationRequest loanApplicationRequest) {

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(loanApplicationRequest.getFirstName());
        clientEntity.setMiddleName(loanApplicationRequest.getMiddleName());
        clientEntity.setLastName(loanApplicationRequest.getLastName());
        clientEntity.setEmail(loanApplicationRequest.getEmail());
        clientEntity.setBirthDate(loanApplicationRequest.getBirthdate());

        PassportEntity passport = new PassportEntity();
        passport.setSeries(loanApplicationRequest.getPassportSeries());
        passport.setNumber(loanApplicationRequest.getPassportNumber());
        passportRepository.save(passport);

        clientEntity.setPassport(passport);
        clientRepository.save(clientEntity);

        return clientEntity.getId();
    }

    @Override
    public ClientEntity getClient(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + clientId));
    }
}
