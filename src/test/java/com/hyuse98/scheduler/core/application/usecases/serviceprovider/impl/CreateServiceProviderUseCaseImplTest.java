package com.hyuse98.scheduler.core.application.usecases.serviceprovider.impl;

import com.hyuse98.scheduler.core.application.dto.CreateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderAlreadyExistException;
import com.hyuse98.scheduler.core.domain.model.ServiceProvider;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
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

    @Mock
    private ServiceProviderEntityMapper mapper;

    @InjectMocks
    private CreateServiceProviderUseCaseImpl createServiceProviderUseCase;

    private ServiceProvider buildServiceProvider(UUID id, String email) {
        return ServiceProvider.create(id, "Dr. Ana", email, "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35),
                "Av. Paulista, 100", "Psicologia", "CRP-06/12345", new Date(), true);
    }

    @Test
    void shouldCreateServiceProviderSuccessfully() {
        UUID id = UUID.randomUUID();
        CreateServiceProviderRequest request = new CreateServiceProviderRequest(
                id, "ana@clinic.com", "Dr. Ana", "11999999999",
                new Date(), "Av. Paulista, 100", "Psicologia", "CRP-06/12345",
                new Date(), true);

        ServiceProvider mappedProvider = buildServiceProvider(id, "ana@clinic.com");
        ServiceProvider savedProvider = buildServiceProvider(id, "ana@clinic.com");

        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(mapper.toDomain(request)).thenReturn(mappedProvider);
        when(repository.save(mappedProvider)).thenReturn(savedProvider);

        ServiceProvider result = createServiceProviderUseCase.execute(request);

        assertNotNull(result);
        assertEquals("ana@clinic.com", result.getEmail());
        verify(repository).findByEmail("ana@clinic.com");
        verify(mapper).toDomain(request);
        verify(repository).save(mappedProvider);
    }

    @Test
    void shouldThrowServiceProviderAlreadyExistExceptionWhenEmailAlreadyRegistered() {
        UUID id = UUID.randomUUID();
        CreateServiceProviderRequest request = new CreateServiceProviderRequest(
                id, "existing@clinic.com", "Dr. Carlos", "11888888888",
                new Date(), "Rua B, 200", "Fisioterapia", "CREFITO-3/12345",
                new Date(), true);

        ServiceProvider existingProvider = buildServiceProvider(id, "existing@clinic.com");

        when(repository.findByEmail(request.email())).thenReturn(Optional.of(existingProvider));

        assertThrows(ServiceProviderAlreadyExistException.class,
                () -> createServiceProviderUseCase.execute(request));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldCallValidateOnNewProvider() {
        UUID id = UUID.randomUUID();
        CreateServiceProviderRequest request = new CreateServiceProviderRequest(
                id, "ana@clinic.com", "Dr. Ana", "11999999999",
                new Date(), "Av. Paulista, 100", "Psicologia", "CRP-06/12345",
                new Date(), true);

        // Mapper retorna um provider com name=null para forçar falha na validate()
        ServiceProvider invalidProvider = ServiceProvider.create(id, null, "ana@clinic.com",
                "11999999999", new Date(), "Av. Paulista, 100",
                null, null, new Date(), true);

        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(mapper.toDomain(request)).thenReturn(invalidProvider);

        // validate() deve lançar NullPointerException pois name é null
        assertThrows(NullPointerException.class,
                () -> createServiceProviderUseCase.execute(request));

        verify(repository, never()).save(any());
    }
}
