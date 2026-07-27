package com.hyuse98.scheduler.core.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private UUID id;
    private UUID clientId;
    private UUID serviceProviderId;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        clientId = UUID.randomUUID();
        serviceProviderId = UUID.randomUUID();
        futureDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24); // amanhã
    }

    @Test
    void shouldCreateScheduleWithPendingStatus() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Descrição", futureDate);

        assertNotNull(schedule);
        assertEquals(id, schedule.getId());
        assertEquals(clientId, schedule.getClientId());
        assertEquals(serviceProviderId, schedule.getServiceProviderId());
        assertEquals("Consulta", schedule.getServiceType());
        assertEquals("Descrição", schedule.getDescription());
        assertEquals(futureDate, schedule.getScheduledAt());
        assertEquals(ScheduleStatus.PENDING, schedule.getStatus());
    }

    @Test
    void shouldReconstituteScheduleWithGivenStatus() {
        Schedule schedule = Schedule.reconstitute(id, clientId, serviceProviderId,
                "Consulta", "Descrição", futureDate, "CONFIRMED");

        assertNotNull(schedule);
        assertEquals(ScheduleStatus.CONFIRMED, schedule.getStatus());
    }

    @Test
    void shouldReconstitutWithNullStatusDefaultsToPending() {
        Schedule schedule = Schedule.reconstitute(id, clientId, serviceProviderId,
                "Consulta", "Descrição", futureDate, null);

        assertEquals(ScheduleStatus.PENDING, schedule.getStatus());
    }

    @Test
    void shouldReconstituteWithCompletedStatus() {
        Schedule schedule = Schedule.reconstitute(id, clientId, serviceProviderId,
                "Fisioterapia", "Sessão 3", futureDate, "COMPLETED");

        assertEquals(ScheduleStatus.COMPLETED, schedule.getStatus());
    }

    @Test
    void shouldReconstituteWithCancelledStatus() {
        Schedule schedule = Schedule.reconstitute(id, clientId, serviceProviderId,
                "Fisioterapia", "Sessão 3", futureDate, "CANCELLED");

        assertEquals(ScheduleStatus.CANCELLED, schedule.getStatus());
    }

    @Test
    void shouldValidateSuccessfully() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Desc", futureDate);

        assertDoesNotThrow(schedule::validate);
    }

    @Test
    void shouldThrowWhenClientIdIsNullOnValidate() {
        Schedule schedule = Schedule.create(id, null, serviceProviderId, "Consulta", "Desc", futureDate);

        assertThrows(NullPointerException.class, schedule::validate);
    }

    @Test
    void shouldThrowWhenServiceProviderIdIsNullOnValidate() {
        Schedule schedule = Schedule.create(id, clientId, null, "Consulta", "Desc", futureDate);

        assertThrows(NullPointerException.class, schedule::validate);
    }

    @Test
    void shouldThrowWhenScheduledAtIsInThePast() {
        Date pastDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24); // ontem
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Desc", pastDate);

        assertThrows(IllegalArgumentException.class, schedule::validate,
                "Deve lançar exceção para agendamentos no passado");
    }

    @Test
    void shouldUpdateDetailsSuccessfully() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Descrição inicial", futureDate);

        schedule.updateDetails("Psicologia", "Sessão de terapia");

        assertEquals("Psicologia", schedule.getServiceType());
        assertEquals("Sessão de terapia", schedule.getDescription());
    }

    @Test
    void shouldNotUpdateDetailsWhenNullOrBlank() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Descrição original", futureDate);

        schedule.updateDetails(null, "  ");

        assertEquals("Consulta", schedule.getServiceType());
        assertEquals("Descrição original", schedule.getDescription());
    }

    @Test
    void shouldChangeStatusSuccessfully() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Desc", futureDate);

        assertEquals(ScheduleStatus.PENDING, schedule.getStatus());

        schedule.changeStatus(ScheduleStatus.CONFIRMED);

        assertEquals(ScheduleStatus.CONFIRMED, schedule.getStatus());
    }

    @Test
    void shouldThrowWhenChangingToNullStatus() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Desc", futureDate);

        assertThrows(NullPointerException.class, () -> schedule.changeStatus(null));
    }

    @Test
    void shouldChangeStatusThroughAllValues() {
        Schedule schedule = Schedule.create(id, clientId, serviceProviderId, "Consulta", "Desc", futureDate);

        schedule.changeStatus(ScheduleStatus.CONFIRMED);
        assertEquals(ScheduleStatus.CONFIRMED, schedule.getStatus());

        schedule.changeStatus(ScheduleStatus.COMPLETED);
        assertEquals(ScheduleStatus.COMPLETED, schedule.getStatus());

        schedule.changeStatus(ScheduleStatus.CANCELLED);
        assertEquals(ScheduleStatus.CANCELLED, schedule.getStatus());
    }
}
