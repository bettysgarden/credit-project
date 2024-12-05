package com.example.deal.repository;

import com.example.deal.model.entity.ApplicationStatusEntity;
import com.example.deal.model.enums.ApplicationStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationStatusRepositoryTest {

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Test
    void shouldFindByTitle() {
        Optional<ApplicationStatusEntity> foundEntity = applicationStatusRepository.findByTitle(ApplicationStatusEnum.CC_APPROVED);

        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getTitle()).isEqualTo(ApplicationStatusEnum.CC_APPROVED);
    }

    @Test
    void shouldFindById() {
        Optional<ApplicationStatusEntity> foundByTitleEntity = applicationStatusRepository.findByTitle(ApplicationStatusEnum.CC_APPROVED);

        ApplicationStatusEntity foundEntity = applicationStatusRepository.findById(Long.valueOf(foundByTitleEntity.get().getId()))
                .orElseThrow(() -> new AssertionError("Entity not found"));

        assertThat(foundEntity).isNotNull();
    }
}
