package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListSchedulesUseCaseImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ListSchedulesUseCaseImpl listSchedulesUseCase;

    private final Date futureDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24);

    private Schedule buildSchedule(UUID clientId, UUID serviceProviderId) {
        return Schedule.create(UUID.randomUUID(), clientId, serviceProviderId,
                "Consulta", "Desc", futureDate);
    }

    @Test
    void shouldReturnSchedulesByClientIdSuccessfully() {
        UUID clientId = UUID.randomUUID();
        UUID serviceProviderId = UUID.randomUUID();

        List<Schedule> schedules = List.of(
                buildSchedule(clientId, serviceProviderId),
                buildSchedule(clientId, serviceProviderId)
        );

        when(scheduleRepository.findByClientId(clientId)).thenReturn(schedules);

        List<Schedule> result = listSchedulesUseCase.findByClientId(clientId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleRepository).findByClientId(clientId);
    }

    @Test
    void shouldReturnEmptyListWhenNoSchedulesForClient() {
        UUID clientId = UUID.randomUUID();

        when(scheduleRepository.findByClientId(clientId)).thenReturn(Collections.emptyList());

        List<Schedule> result = listSchedulesUseCase.findByClientId(clientId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnSchedulesByServiceProviderIdSuccessfully() {
        UUID clientId = UUID.randomUUID();
        UUID serviceProviderId = UUID.randomUUID();

        List<Schedule> schedules = List.of(
                buildSchedule(clientId, serviceProviderId),
                buildSchedule(UUID.randomUUID(), serviceProviderId)
        );

        when(scheduleRepository.findByServiceProviderId(serviceProviderId)).thenReturn(schedules);

        List<Schedule> result = listSchedulesUseCase.findByServiceProviderId(serviceProviderId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduleRepository).findByServiceProviderId(serviceProviderId);
    }

    @Test
    void shouldReturnEmptyListWhenNoSchedulesForServiceProvider() {
        UUID serviceProviderId = UUID.randomUUID();

        when(scheduleRepository.findByServiceProviderId(serviceProviderId)).thenReturn(Collections.emptyList());

        List<Schedule> result = listSchedulesUseCase.findByServiceProviderId(serviceProviderId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
