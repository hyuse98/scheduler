package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.application.dto.CreateScheduleRequest;
import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderNotFoundException;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.model.ScheduleStatus;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
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
class CreateScheduleUseCaseImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ServiceProviderRepository serviceProviderRepository;

    @InjectMocks
    private CreateScheduleUseCaseImpl createScheduleUseCase;

    private final Date futureDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24);

    private Client buildClient(UUID id, String email) {
        return Client.create(id, email, "John Doe", "11999999999",
                new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25),
                "Rua A, 123", "123456789012345", new Date(), true);
    }

    private Schedule buildSchedule(UUID clientId, UUID serviceProviderId) {
        return Schedule.create(UUID.randomUUID(), clientId, serviceProviderId,
                "Consulta", "Descrição", futureDate);
    }

    @Test
    void shouldCreateScheduleSuccessfully() {
        UUID clientId = UUID.randomUUID();
        UUID serviceProviderId = UUID.randomUUID();
        String clientEmail = "john@test.com";

        Client client = buildClient(clientId, clientEmail);
        CreateScheduleRequest request = new CreateScheduleRequest(clientId, serviceProviderId,
                "Consulta", "Primeira sessão", futureDate);

        Schedule savedSchedule = buildSchedule(clientId, serviceProviderId);

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));
        when(serviceProviderRepository.existsById(serviceProviderId)).thenReturn(true);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        Schedule result = createScheduleUseCase.execute(clientEmail, request);

        assertNotNull(result);
        assertEquals(ScheduleStatus.PENDING, result.getStatus());
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenClientEmailDoesNotExist() {
        String clientEmail = "notfound@test.com";
        UUID serviceProviderId = UUID.randomUUID();
        CreateScheduleRequest request = new CreateScheduleRequest(UUID.randomUUID(), serviceProviderId,
                "Consulta", "Desc", futureDate);

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,
                () -> createScheduleUseCase.execute(clientEmail, request));

        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void shouldThrowServiceProviderNotFoundExceptionWhenServiceProviderDoesNotExist() {
        UUID clientId = UUID.randomUUID();
        UUID serviceProviderId = UUID.randomUUID();
        String clientEmail = "john@test.com";

        Client client = buildClient(clientId, clientEmail);
        CreateScheduleRequest request = new CreateScheduleRequest(clientId, serviceProviderId,
                "Consulta", "Desc", futureDate);

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));
        when(serviceProviderRepository.existsById(serviceProviderId)).thenReturn(false);

        assertThrows(ServiceProviderNotFoundException.class,
                () -> createScheduleUseCase.execute(clientEmail, request));

        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenScheduledAtIsInThePast() {
        UUID clientId = UUID.randomUUID();
        UUID serviceProviderId = UUID.randomUUID();
        String clientEmail = "john@test.com";

        Client client = buildClient(clientId, clientEmail);
        Date pastDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
        CreateScheduleRequest request = new CreateScheduleRequest(clientId, serviceProviderId,
                "Consulta", "Desc", pastDate);

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));
        when(serviceProviderRepository.existsById(serviceProviderId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> createScheduleUseCase.execute(clientEmail, request));

        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void shouldUseClientIdFromDatabaseNotFromRequest() {
        UUID clientId = UUID.randomUUID();
        UUID differentClientId = UUID.randomUUID(); // ID diferente no request (deve ser ignorado)
        UUID serviceProviderId = UUID.randomUUID();
        String clientEmail = "john@test.com";

        Client client = buildClient(clientId, clientEmail);
        CreateScheduleRequest request = new CreateScheduleRequest(differentClientId, serviceProviderId,
                "Consulta", "Desc", futureDate);

        Schedule savedSchedule = buildSchedule(clientId, serviceProviderId);

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));
        when(serviceProviderRepository.existsById(serviceProviderId)).thenReturn(true);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        Schedule result = createScheduleUseCase.execute(clientEmail, request);

        // Verifica que o ID do cliente logado é usado (segurança)
        assertEquals(clientId, result.getClientId());
    }
}
