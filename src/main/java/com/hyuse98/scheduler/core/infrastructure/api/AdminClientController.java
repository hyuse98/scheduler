package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientResponse;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.ListClientUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import com.hyuse98.scheduler.core.infrastructure.api.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Admin - Client", description = "Endpoints administrativos para clientes")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/client")
public class AdminClientController {

    private final ListClientUseCase listClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final ClientEntityMapper clientEntityMapper;

    public AdminClientController(
            ListClientUseCase listClientUseCase,
            GetClientUseCase getClientUseCase,
            ClientEntityMapper clientEntityMapper) {
        this.listClientUseCase = listClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.clientEntityMapper = clientEntityMapper;
    }

    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        var responseList = listClientUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(
                responseList.stream()
                        .map(clientEntityMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Obter cliente por ID", description = "Busca os detalhes de um cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable UUID id) {
        var client = getClientUseCase.execute(id);
        return ResponseEntity.ok(clientEntityMapper.toResponse(client));
    }
}