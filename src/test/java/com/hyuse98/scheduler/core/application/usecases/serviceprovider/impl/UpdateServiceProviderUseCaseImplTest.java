package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.dto.UpdateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderNotFoundException;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateServiceProviderUseCaseImplTest {

    @Mock
    private ServiceProviderRepository repository;

    @InjectMocks
    private UpdateServiceProviderUseCaseImpl updateServiceProviderUseCase;

    private ServiceProvider buildServiceProvider(UUID id) {
        return ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com", "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35),
                "Av. Paulista, 100", "Psicologia", "CRP-06/12345", new Date(), true);
    }

    @Test
    void shouldUpdateServiceProviderSuccessfully() {
        UUID id = UUID.randomUUID();
        ServiceProvider existingProvider = buildServiceProvider(id);

        Date newBirthday = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 40);
        UpdateServiceProviderRequest request = new UpdateServiceProviderRequest(
                "Dra. Ana Maria", "11777777777", newBirthday,
                "Rua C, 300", "Neuropsicologia", "CRP-06/99999");

        when(repository.findById(id)).thenReturn(Optional.of(existingProvider));
        when(repository.save(any(ServiceProvider.class))).thenAnswer(inv -> inv.getArgument(0));

        ServiceProvider result = updateServiceProviderUseCase.execute(id, request);

        assertNotNull(result);
        assertEquals("Dra. Ana Maria", result.getName());
        assertEquals("11777777777", result.getPhoneNumber());
        assertEquals(newBirthday, result.getBirthday());
        assertEquals("Rua C, 300", result.getAddress());
        assertEquals("Neuropsicologia", result.getExpertise());
        assertEquals("CRP-06/99999", result.getRegistry());
        verify(repository).findById(id);
        verify(repository).save(existingProvider);
    }

    @Test
    void shouldThrowServiceProviderNotFoundExceptionWhenProviderDoesNotExist() {
        UUID id = UUID.randomUUID();
        UpdateServiceProviderRequest request = new UpdateServiceProviderRequest(
                "Dra. Ana Maria", "11777777777", new Date(),
                "Rua C, 300", "Neuropsicologia", "CRP-06/99999");

        when(repository.findById(id)).thenReturn(Optional.empty());

        ServiceProviderNotFoundException ex = assertThrows(ServiceProviderNotFoundException.class,
                () -> updateServiceProviderUseCase.execute(id, request));

        assertNotNull(ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldValidateAfterUpdating() {
        UUID id = UUID.randomUUID();
        ServiceProvider existingProvider = buildServiceProvider(id);

        // Request com todos os campos nulos: não deve alterar os dados existentes
        UpdateServiceProviderRequest nullRequest = new UpdateServiceProviderRequest(
                null, null, null, null, null, null);

        when(repository.findById(id)).thenReturn(Optional.of(existingProvider));
        when(repository.save(any(ServiceProvider.class))).thenAnswer(inv -> inv.getArgument(0));

        // O provider já tem dados válidos, então validate() deve passar sem erros
        assertDoesNotThrow(() -> updateServiceProviderUseCase.execute(id, nullRequest));
    }
}
