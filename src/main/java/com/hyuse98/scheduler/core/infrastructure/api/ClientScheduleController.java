package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.CreateScheduleRequest;
import com.hyuse98.scheduler.core.application.dto.ScheduleResponse;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.CreateScheduleUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.DeleteScheduleUseCase;
import com.hyuse98.scheduler.core.application.usecases.schedule.ListSchedulesUseCase;
import com.hyuse98.scheduler.core.infrastructure.api.advice.ErrorResponse;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ScheduleEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;
import java.util.UUID;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Client Schedules", description = "Endpoints para agendamentos do cliente")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/v1/client/me/schedules")
public class ClientScheduleController {

    private final CreateScheduleUseCase createScheduleUseCase;
    private final ListSchedulesUseCase listSchedulesUseCase;
    private final GetClientUseCase getClientUseCase;
    private final DeleteScheduleUseCase deleteScheduleUseCase;
    private final ScheduleEntityMapper mapper;

    public ClientScheduleController(
            CreateScheduleUseCase createScheduleUseCase,
            ListSchedulesUseCase listSchedulesUseCase,
            GetClientUseCase getClientUseCase,
            DeleteScheduleUseCase deleteScheduleUseCase,
            ScheduleEntityMapper mapper) {
        this.createScheduleUseCase = createScheduleUseCase;
        this.listSchedulesUseCase = listSchedulesUseCase;
        this.getClientUseCase = getClientUseCase;
        this.deleteScheduleUseCase = deleteScheduleUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Criar agendamento", description = "Permite que o cliente logado crie um novo agendamento com um prestador de serviços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(Principal principal, @Valid @RequestBody CreateScheduleRequest request) {
        String loggedInEmail = principal.getName();
        var savedSchedule = createScheduleUseCase.execute(loggedInEmail, request);

        ScheduleResponse response = mapper.toResponse(savedSchedule);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos realizados pelo cliente logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getMySchedules(Principal principal) {
        String loggedInEmail = principal.getName();
        var client = getClientUseCase.execute(loggedInEmail);

        var schedules = listSchedulesUseCase.findByClientId(client.getId());

        var response = schedules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir agendamento", description = "Deleta fisicamente um agendamento do banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Excluído com sucesso"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchedule(@PathVariable UUID id) {
        deleteScheduleUseCase.execute(id);
    }
}