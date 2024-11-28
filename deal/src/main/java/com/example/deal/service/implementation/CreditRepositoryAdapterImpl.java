package com.example.deal.service.implementation;

import com.example.deal.exception.DatabaseOperationException;
import com.example.deal.mapper.CreditMapper;
import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.entity.CreditEntity;
import com.example.deal.model.entity.CreditStatusEntity;
import com.example.deal.model.enums.CreditStatusEnum;
import com.example.deal.repository.CreditRepository;
import com.example.deal.repository.CreditStatusRepository;
import com.example.deal.service.CreditRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditRepositoryAdapterImpl implements CreditRepositoryAdapter {
    private final CreditRepository repository;
    private final CreditStatusRepository statusRepository;
    private final CreditMapper creditMapper;

    @Override
    public CreditEntity saveCredit(CreditResponse creditResponse) {
        CreditEntity creditEntity = creditMapper.toEntity(creditResponse);
        creditEntity.setStatus(findStatusByTitle(CreditStatusEnum.CALCULATED));
        repository.save(creditEntity);
        return creditEntity;
    }

    @Override
    public CreditStatusEntity findStatusByTitle(CreditStatusEnum statusEnum) {
        return statusRepository.findByTitle(statusEnum)
                .orElseThrow(() -> new DatabaseOperationException("Статус для кредита не найден: ", List.of(statusEnum.toString())));

    }
}
