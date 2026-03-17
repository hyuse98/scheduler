package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.application.exceptions.ScheduleNotFoundException;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.model.ScheduleStatus;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
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
class ChangeScheduleStatusUseCaseImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ChangeScheduleStatusUseCaseImpl changeScheduleStatusUseCase;

    private final Date futureDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24);

    private Schedule buildSchedule(UUID id) {
        return Schedule.create(id, UUID.randomUUID(), UUID.randomUUID(),
                "Consulta", "Desc", futureDate);
    }

    @Test
    void shouldChangeStatusToConfirmedSuccessfully() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = buildSchedule(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(inv -> inv.getArgument(0));

        Schedule result = changeScheduleStatusUseCase.execute(scheduleId, "CONFIRMED");

        assertNotNull(result);
        assertEquals(ScheduleStatus.CONFIRMED, result.getStatus());
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void shouldChangeStatusToCompletedSuccessfully() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = buildSchedule(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(inv -> inv.getArgument(0));

        Schedule result = changeScheduleStatusUseCase.execute(scheduleId, "COMPLETED");

        assertEquals(ScheduleStatus.COMPLETED, result.getStatus());
    }

    @Test
    void shouldChangeStatusToCancelledSuccessfully() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = buildSchedule(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(inv -> inv.getArgument(0));

        Schedule result = changeScheduleStatusUseCase.execute(scheduleId, "CANCELLED");

        assertEquals(ScheduleStatus.CANCELLED, result.getStatus());
    }

    @Test
    void shouldAcceptLowercaseStatusAndConvertToUppercase() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = buildSchedule(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(inv -> inv.getArgument(0));

        Schedule result = changeScheduleStatusUseCase.execute(scheduleId, "confirmed");

        assertEquals(ScheduleStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void shouldThrowScheduleNotFoundExceptionWhenScheduleDoesNotExist() {
        UUID scheduleId = UUID.randomUUID();

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        ScheduleNotFoundException ex = assertThrows(ScheduleNotFoundException.class,
                () -> changeScheduleStatusUseCase.execute(scheduleId, "CONFIRMED"));

        assertTrue(ex.getMessage().contains(scheduleId.toString()));
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStatusIsInvalid() {
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = buildSchedule(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        assertThrows(IllegalArgumentException.class,
                () -> changeScheduleStatusUseCase.execute(scheduleId, "INVALID_STATUS"));

        verify(scheduleRepository, never()).save(any());
    }
}
