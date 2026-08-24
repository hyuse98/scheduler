package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderAlreadyExistException;
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
class CreateServiceProviderUseCaseImplTest {

    @Mock
    private ServiceProviderRepository repository;

    @InjectMocks
    private CreateServiceProviderUseCaseImpl createServiceProviderUseCase;

    private ServiceProvider buildServiceProvider(UUID id, String email) {
        return ServiceProvider.create(
                id,
                "PENDENTE",
                email,
                "PENDENTE",
                null,
                "PENDENTE",
                "PENDENTE",
                "PENDENTE",
                new Date(),
                true
        );
    }

    @Test
    void shouldCreateServiceProviderSuccessfully() {
        UUID id = UUID.randomUUID();
        String email = "ana@clinic.com";

        ServiceProvider savedProvider = buildServiceProvider(id, email);

        when(repository.findByEmail(email)).thenReturn(Optional.empty());
        when(repository.save(any(ServiceProvider.class))).thenReturn(savedProvider);

        ServiceProvider result = createServiceProviderUseCase.execute(id, email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(repository).findByEmail(email);
        verify(repository).save(any(ServiceProvider.class));
    }

    @Test
    void shouldThrowServiceProviderAlreadyExistExceptionWhenEmailAlreadyRegistered() {
        UUID id = UUID.randomUUID();
        String email = "existing@clinic.com";

        ServiceProvider existingProvider = buildServiceProvider(id, email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(existingProvider));

        assertThrows(ServiceProviderAlreadyExistException.class,
                () -> createServiceProviderUseCase.execute(id, email));

        verify(repository, never()).save(any());
    }
}
