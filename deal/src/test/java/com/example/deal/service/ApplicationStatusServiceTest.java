package com.example.deal.service;

import com.example.deal.exception.DatabaseOperationException;
import com.example.deal.exception.StateTransitionException;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ApplicationStatusEntity;
import com.example.deal.model.entity.jsonb.StatusHistory;
import com.example.deal.model.enums.ApplicationStatusEnum;
import com.example.deal.repository.ApplicationStatusRepository;
import com.example.deal.service.implementation.ApplicationStatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationStatusServiceTest {

    @InjectMocks
    private ApplicationStatusServiceImpl applicationStatusService;
    @Mock
    private ApplicationStatusRepository statusRepository;

    private ApplicationEntity applicationEntity;
    private ApplicationStatusEntity statusEntity;

    @BeforeEach
    void setUp() {
        applicationEntity = new ApplicationEntity();
        applicationEntity.setId(1);
        applicationEntity.setStatusHistory(new ArrayList<>());

        statusEntity = new ApplicationStatusEntity();
        statusEntity.setTitle(ApplicationStatusEnum.PREAPPROVAL);
    }


    @Test
    void testUpdateStatus_Success() {
        when(statusRepository.findByTitle(ApplicationStatusEnum.PREAPPROVAL)).thenReturn(Optional.of(statusEntity));

        applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.PREAPPROVAL);

        assertEquals(ApplicationStatusEnum.PREAPPROVAL, applicationEntity.getStatus().getTitle());
        assertNotNull(applicationEntity.getStatusHistory());
        assertEquals(1, applicationEntity.getStatusHistory().size());
        assertEquals(ApplicationStatusEnum.PREAPPROVAL, applicationEntity.getStatusHistory().get(0).getStatus());
        verify(statusRepository, times(1)).findByTitle(ApplicationStatusEnum.PREAPPROVAL);
    }


    @Test
    void testUpdateStatus_StatusAlreadyExistsInHistory() {
        StatusHistory existingHistory = new StatusHistory();
        existingHistory.setStatus(ApplicationStatusEnum.PREAPPROVAL);
        existingHistory.setTime(LocalDateTime.now());
        existingHistory.setChangeType(null);
        applicationEntity.setStatusHistory(List.of(existingHistory));
        when(statusRepository.findByTitle(ApplicationStatusEnum.PREAPPROVAL)).thenReturn(Optional.of(statusEntity));

        StateTransitionException exception = assertThrows(StateTransitionException.class,
                () -> applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.PREAPPROVAL));

        assertTrue(exception.getMessage().contains("Некорректное состояние для перехода"));
    }

    @Test
    void testUpdateStatus_StatusNotFound() {
        when(statusRepository.findByTitle(ApplicationStatusEnum.PREAPPROVAL)).thenReturn(Optional.empty());

        DatabaseOperationException exception = assertThrows(DatabaseOperationException.class,
                () -> applicationStatusService.updateStatus(applicationEntity, ApplicationStatusEnum.PREAPPROVAL));

        assertTrue(exception.getMessage().contains("Статус не найден"));
        verify(statusRepository, times(1)).findByTitle(ApplicationStatusEnum.PREAPPROVAL);
    }

    @Test
    void testFindStatus_Success() throws Exception {
        when(statusRepository.findByTitle(ApplicationStatusEnum.PREAPPROVAL)).thenReturn(Optional.of(statusEntity));

        Method method = ApplicationStatusServiceImpl.class.getDeclaredMethod("findStatus", ApplicationStatusEnum.class);
        method.setAccessible(true);

        ApplicationStatusEntity result = (ApplicationStatusEntity) method.invoke(applicationStatusService, ApplicationStatusEnum.PREAPPROVAL);

        assertNotNull(result);
        assertEquals(ApplicationStatusEnum.PREAPPROVAL, result.getTitle());
        verify(statusRepository, times(1)).findByTitle(ApplicationStatusEnum.PREAPPROVAL);
    }

}
