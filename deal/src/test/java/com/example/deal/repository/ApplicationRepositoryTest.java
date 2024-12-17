package com.example.deal.repository;

import com.example.deal.model.entity.ApplicationEntity;
import com.example.deal.model.entity.ApplicationStatusEntity;
import com.example.deal.model.entity.ClientEntity;
import com.example.deal.model.entity.PassportEntity;
import com.example.deal.model.enums.ApplicationStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void testSaveAndFindById() {
        ApplicationEntity savedApplication = getSavedApplicationEntity();
        assertNotNull(savedApplication.getId());

        Optional<ApplicationEntity> foundApplication = applicationRepository.findById(Long.valueOf(savedApplication.getId()));
        assertTrue(foundApplication.isPresent());
        assertEquals("John", foundApplication.get().getClient().getFirstName());
    }

    @Test
    void testUpdateEntity() {
        ApplicationEntity savedApplication = getSavedApplicationEntity();

        savedApplication.setSignDate(LocalDateTime.now().plusDays(1));
        ApplicationEntity updatedApplication = applicationRepository.save(savedApplication);

        assertEquals(savedApplication.getSignDate(), updatedApplication.getSignDate());
    }

    @Test
    void testDeleteEntity() {
        ApplicationEntity savedApplication = getSavedApplicationEntity();

        Long applicationId = Long.valueOf(savedApplication.getId());

        applicationRepository.deleteById(applicationId);
        Optional<ApplicationEntity> deletedApplication = applicationRepository.findById(applicationId);

        assertFalse(deletedApplication.isPresent());
    }

    private ApplicationEntity getSavedApplicationEntity() {
        ClientEntity client = new ClientEntity();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client.setPassport(new PassportEntity());

        ApplicationEntity application = new ApplicationEntity();
        application.setClient(client);
        application.setCreationDate(LocalDateTime.now());
        application.setStatusHistory(Collections.emptyList());
        application.setStatus(new ApplicationStatusEntity(1, ApplicationStatusEnum.CC_APPROVED));

        return applicationRepository.save(application);
    }

}

