package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.CreateScheduleRequest;
import com.hyuse98.scheduler.core.application.dto.ScheduleResponse;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.CreateScheduleUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.ListSchedulesUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ScheduleEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Client Schedules", description = "Endpoints para agendamentos do cliente")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/v1/client/me/schedules")
public class ClientScheduleController {

    private final CreateScheduleUseCase createScheduleUseCase;
    private final ListSchedulesUseCase listSchedulesUseCase;
    private final GetClientUseCase getClientUseCase;
    private final ScheduleEntityMapper mapper;

    public ClientScheduleController(
            CreateScheduleUseCase createScheduleUseCase,
            ListSchedulesUseCase listSchedulesUseCase,
            GetClientUseCase getClientUseCase,
            ScheduleEntityMapper mapper) {
        this.createScheduleUseCase = createScheduleUseCase;
        this.listSchedulesUseCase = listSchedulesUseCase;
        this.getClientUseCase = getClientUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Criar agendamento", description = "Permite que o cliente logado crie um novo agendamento com um prestador de serviços")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(Principal principal, @Valid @RequestBody CreateScheduleRequest request) {
        // Usa o email do token para identificar de forma segura quem está a agendar
        String loggedInEmail = principal.getName();
        var savedSchedule = createScheduleUseCase.execute(loggedInEmail, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(savedSchedule));
    }

    @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos realizados pelo cliente logado")
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getMySchedules(Principal principal) {
        String loggedInEmail = principal.getName();
        // Recupera o ID do cliente logado para procurar a sua agenda
        var client = getClientUseCase.execute(loggedInEmail);

        var schedules = listSchedulesUseCase.findByClientId(client.getId());

        var response = schedules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}