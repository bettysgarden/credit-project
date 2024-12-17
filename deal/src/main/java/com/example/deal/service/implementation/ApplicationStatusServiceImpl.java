package com.example.deal.service.implementation;

import com.example.deal.exception.DatabaseOperationException;
import com.example.deal.exception.StateTransitionException;
import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ApplicationStatusEntity;
import com.example.deal.model.entity.jsonb.StatusHistory;
import com.example.deal.model.enums.ApplicationStatusEnum;
import com.example.deal.model.enums.ChangeTypeEnum;
import com.example.deal.repository.ApplicationStatusRepository;
import com.example.deal.service.ApplicationStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationStatusServiceImpl implements ApplicationStatusService {
    private final ApplicationStatusRepository statusRepository;

    private static List<StatusHistory> updateStatusHistory(ApplicationEntity applicationEntity) {
        log.debug("Обновление истории статусов для заявки ID: {}", applicationEntity.getId());
        List<StatusHistory> history = applicationEntity.getStatusHistory();
        if (history == null) {
            history = new ArrayList<>();
        }

        boolean statusAlreadyExists = history.stream()
                .anyMatch(entry -> entry.getStatus().equals(applicationEntity.getStatus().getTitle()));

        if (statusAlreadyExists) {
            log.warn("Статус {} уже существует в истории для заявки ID: {}", applicationEntity.getStatus().getTitle(), applicationEntity.getId());
            throw new StateTransitionException("Некорректное состояние для перехода",
                    List.of("Статус " + applicationEntity.getStatus().getTitle() + " уже существует в истории статусов заявок."));
        }

        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setStatus(applicationEntity.getStatus().getTitle());
        statusHistory.setTime(LocalDateTime.now());
        statusHistory.setChangeType(ChangeTypeEnum.AUTO);
        history.add(statusHistory);

        log.debug("История статусов обновлена для заявки ID: {}", applicationEntity.getId());
        return history;
    }

    @Override
    public void updateStatus(ApplicationEntity applicationEntity, ApplicationStatusEnum applicationStatusEnum) {
        log.info("Обновление статуса заявки ID: {} на {}", applicationEntity.getId(), applicationStatusEnum);
        ApplicationStatusEntity status = findStatus(applicationStatusEnum);
        updateCurrentStatus(applicationEntity, status);
        applicationEntity.setStatusHistory(updateStatusHistory(applicationEntity));
    }

    private ApplicationStatusEntity findStatus(ApplicationStatusEnum statusEnum) {
        return statusRepository.findByTitle(statusEnum)
                .orElseThrow(() -> {
                    log.error("Статус {} не найден.", statusEnum);
                    return new DatabaseOperationException("Статус не найден: ", List.of(statusEnum.toString()));
                });
    }

    private void updateCurrentStatus(ApplicationEntity applicationEntity, ApplicationStatusEntity status) {
        applicationEntity.setStatus(status);
        log.info("Текущий статус заявки ID {} обновлен на {}", applicationEntity.getId(), status.getTitle());
    }
}
