package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ScheduleResponse;
import com.hyuse98.scheduler.core.application.usecases.schedule.ChangeScheduleStatusUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.ListSchedulesUseCase;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ScheduleEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import com.hyuse98.scheduler.core.infrastructure.api.advice.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Service Provider Schedules", description = "Endpoints para gerenciamento dos agendamentos do prestador de serviços")
@PreAuthorize("hasRole('SERVICE_PROVIDER')")
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

    @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos do prestador de serviços logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getMySchedules(Principal principal) {
        String loggedInEmail = principal.getName();

        var provider = getServiceProviderUseCase.execute(loggedInEmail);

        var schedules = listSchedulesUseCase.findByServiceProviderId(provider.getId());

        var response = schedules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mudar status do agendamento", description = "Altera o status de um agendamento específico (ex: CONFIRMED, CANCELED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{scheduleId}/status")
    public ResponseEntity<ScheduleResponse> changeStatus(
            Principal principal,
            @PathVariable UUID scheduleId,
            @RequestParam String status) {

        var updatedSchedule = changeScheduleStatusUseCase.execute(scheduleId, status);
        return ResponseEntity.ok(mapper.toResponse(updatedSchedule));
    }
}