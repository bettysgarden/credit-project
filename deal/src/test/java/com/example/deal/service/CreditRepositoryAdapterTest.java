package com.example.deal.service;

import com.example.deal.exception.DatabaseOperationException;
import com.example.deal.mapper.CreditMapper;
import com.example.deal.model.dto.CreditResponse;
import com.example.deal.model.entity.CreditEntity;
import com.example.deal.model.entity.CreditStatusEntity;
import com.example.deal.model.enums.CreditStatusEnum;
import com.example.deal.repository.CreditRepository;
import com.example.deal.repository.CreditStatusRepository;
import com.example.deal.service.implementation.CreditRepositoryAdapterImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditRepositoryAdapterTest {
    @InjectMocks
    private CreditRepositoryAdapterImpl creditRepositoryAdapter;
    @Mock
    private CreditRepository repository;
    @Mock
    private CreditStatusRepository statusRepository;
    @Mock
    private CreditMapper creditMapper;

    @Test
    void saveCredit_Success() {
        CreditResponse creditResponse = new CreditResponse();
        CreditEntity creditEntity = new CreditEntity();
        CreditStatusEntity statusEntity = new CreditStatusEntity();
        statusEntity.setTitle(CreditStatusEnum.CALCULATED);

        when(creditMapper.toEntity(creditResponse)).thenReturn(creditEntity);
        when(statusRepository.findByTitle(CreditStatusEnum.CALCULATED)).thenReturn(Optional.of(statusEntity));
        when(repository.save(creditEntity)).thenReturn(creditEntity);

        CreditEntity result = creditRepositoryAdapter.saveCredit(creditResponse);

        assertNotNull(result);
        assertEquals(statusEntity, result.getStatus());
        verify(creditMapper).toEntity(creditResponse);
        verify(statusRepository).findByTitle(CreditStatusEnum.CALCULATED);
        verify(repository).save(creditEntity);
    }

    @Test
    void saveCredit_StatusNotFound() {
        CreditResponse creditResponse = new CreditResponse();
        CreditEntity creditEntity = new CreditEntity();

        when(creditMapper.toEntity(creditResponse)).thenReturn(creditEntity);
        when(statusRepository.findByTitle(CreditStatusEnum.CALCULATED)).thenReturn(Optional.empty());

        DatabaseOperationException exception = assertThrows(
                DatabaseOperationException.class,
                () -> creditRepositoryAdapter.saveCredit(creditResponse)
        );

        assertTrue(exception.getMessage().contains("Статус для кредита не найден"));
        verify(creditMapper).toEntity(creditResponse);
        verify(statusRepository).findByTitle(CreditStatusEnum.CALCULATED);
        verifyNoInteractions(repository);
    }

    @Test
    void findStatusByTitle_Success() {
        CreditStatusEnum statusEnum = CreditStatusEnum.CALCULATED;
        CreditStatusEntity statusEntity = new CreditStatusEntity();
        statusEntity.setTitle(statusEnum);

        when(statusRepository.findByTitle(statusEnum)).thenReturn(Optional.of(statusEntity));

        CreditStatusEntity result = creditRepositoryAdapter.findStatusByTitle(statusEnum);

        assertNotNull(result);
        assertEquals(statusEnum, result.getTitle());
        verify(statusRepository).findByTitle(statusEnum);
    }

    @Test
    void findStatusByTitle_NotFound() {
        CreditStatusEnum statusEnum = CreditStatusEnum.CALCULATED;

        when(statusRepository.findByTitle(statusEnum)).thenReturn(Optional.empty());

        DatabaseOperationException exception = assertThrows(
                DatabaseOperationException.class,
                () -> creditRepositoryAdapter.findStatusByTitle(statusEnum)
        );

        assertTrue(exception.getMessage().contains("Статус для кредита не найден"));
        verify(statusRepository).findByTitle(statusEnum);
    }

}
