package com.example.deal.repository;

import com.example.deal.model.entity.ClientEntity;
import com.example.deal.model.entity.PassportEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testSaveAndFindById() {
        ClientEntity savedClient = getSavedClientEntity();
        assertNotNull(savedClient.getId());

        Optional<ClientEntity> foundClient = clientRepository.findById(Long.valueOf(savedClient.getId()));
        assertTrue(foundClient.isPresent());
        assertEquals("John", foundClient.get().getFirstName());
    }

    @Test
    void testUpdateEntity() {
        ClientEntity savedClient = getSavedClientEntity();

        savedClient.setEmail("kkkkkk@mail.ru");
        ClientEntity updatedClient = clientRepository.save(savedClient);

        assertEquals(savedClient.getEmail(), updatedClient.getEmail());
    }

    @Test
    void testDeleteEntity() {
        ClientEntity savedClient = getSavedClientEntity();

        Long clientId = Long.valueOf(savedClient.getId());

        clientRepository.deleteById(clientId);
        Optional<ClientEntity> deletedClient = clientRepository.findById(clientId);

        assertFalse(deletedClient.isPresent());
    }

    private ClientEntity getSavedClientEntity() {
        ClientEntity client = new ClientEntity();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client.setPassport(new PassportEntity());

        return clientRepository.save(client);
    }
}
