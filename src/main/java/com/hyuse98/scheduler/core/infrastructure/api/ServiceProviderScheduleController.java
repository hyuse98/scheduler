package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ScheduleResponse;
import com.hyuse98.scheduler.core.application.usecases.schedule.ChangeScheduleStatusUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.ListSchedulesUseCase;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ScheduleEntityMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/provider/me/schedules")
public class ServiceProviderScheduleController {

    private final ListSchedulesUseCase listSchedulesUseCase;
    private final ChangeScheduleStatusUseCase changeScheduleStatusUseCase;
    private final GetServiceProviderUseCase getServiceProviderUseCase;
    private final ScheduleEntityMapper mapper;

    public ServiceProviderScheduleController(
            ListSchedulesUseCase listSchedulesUseCase,
            ChangeScheduleStatusUseCase changeScheduleStatusUseCase,
            GetServiceProviderUseCase getServiceProviderUseCase,
            ScheduleEntityMapper mapper) {
        this.listSchedulesUseCase = listSchedulesUseCase;
        this.changeScheduleStatusUseCase = changeScheduleStatusUseCase;
        this.getServiceProviderUseCase = getServiceProviderUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getMySchedules(Principal principal) {
        String loggedInEmail = principal.getName();
        // Recupera o ID do prestador logado para listar os agendamentos dele
        var provider = getServiceProviderUseCase.execute(loggedInEmail);

        var schedules = listSchedulesUseCase.findByServiceProviderId(provider.getId());

        var response = schedules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{scheduleId}/status")
    public ResponseEntity<ScheduleResponse> changeStatus(
            Principal principal,
            @PathVariable UUID scheduleId,
            @RequestParam String status) {

        // MVP: Atualiza o estado do agendamento (CONFIRMED, COMPLETED, CANCELLED)
        var updatedSchedule = changeScheduleStatusUseCase.execute(scheduleId, status);
        return ResponseEntity.ok(mapper.toResponse(updatedSchedule));
    }
}