package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetServiceProviderUseCaseImplTest {

    @Mock
    private ServiceProviderRepository repository;

    @InjectMocks
    private GetServiceProviderUseCaseImpl getServiceProviderUseCase;

    private ServiceProvider buildServiceProvider(UUID id, String email) {
        return ServiceProvider.create(id, "Dr. Ana", email, "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35),
                "Av. Paulista, 100", "Psicologia", "CRP-06/12345", new Date(), true);
    }

    @Test
    void shouldGetServiceProviderByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        ServiceProvider expected = buildServiceProvider(id, "ana@clinic.com");

        when(repository.findById(id)).thenReturn(Optional.of(expected));

        ServiceProvider result = getServiceProviderUseCase.execute(id);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(repository).findById(id);
    }

    @Test
    void shouldThrowServiceProviderNotFoundExceptionWhenIdDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ServiceProviderNotFoundException.class,
                () -> getServiceProviderUseCase.execute(id));
    }

    @Test
    void shouldGetServiceProviderByEmailSuccessfully() {
        UUID id = UUID.randomUUID();
        String email = "ana@clinic.com";
        ServiceProvider expected = buildServiceProvider(id, email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(expected));

        ServiceProvider result = getServiceProviderUseCase.execute(email);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(repository).findByEmail(email);
    }

    @Test
    void shouldThrowServiceProviderNotFoundExceptionWhenEmailDoesNotExist() {
        String email = "notfound@clinic.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ServiceProviderNotFoundException.class,
                () -> getServiceProviderUseCase.execute(email));
    }
}
