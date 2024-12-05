package com.example.deal.repository;

import com.example.deal.model.entity.CreditStatusEntity;
import com.example.deal.model.enums.CreditStatusEnum;
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
public class CreditStatusRepositoryTest {

    @Autowired
    private CreditStatusRepository creditStatusRepository;

    @Test
    void shouldFindByTitle() {
        Optional<CreditStatusEntity> foundEntity = creditStatusRepository.findByTitle(CreditStatusEnum.CALCULATED);

        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getTitle()).isEqualTo(CreditStatusEnum.CALCULATED);
    }

    @Test
    void shouldFindById() {
        Optional<CreditStatusEntity> foundByTitleEntity = creditStatusRepository.findByTitle(CreditStatusEnum.CALCULATED);

        CreditStatusEntity foundEntity = creditStatusRepository.findById(Long.valueOf(foundByTitleEntity.get().getId()))
                .orElseThrow(() -> new AssertionError("Entity not found"));

        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getTitle()).isIn(CreditStatusEnum.CALCULATED, CreditStatusEnum.ISSUED);
    }
}
