package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientResponse;
import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.UpdateClientUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hyuse98.scheduler.core.infrastructure.api.advice.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Client Profile", description = "Endpoints para gerenciamento do perfil do cliente")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/v1/client/me")
public class ClientProfileController {

    private final GetClientUseCase getClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final ClientEntityMapper clientEntityMapper;

    public ClientProfileController(GetClientUseCase getClientUseCase, UpdateClientUseCase updateClientUseCase, ClientEntityMapper clientEntityMapper) {
        this.getClientUseCase = getClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.clientEntityMapper = clientEntityMapper;
    }

    @Operation(summary = "Obter Perfil", description = "Retorna os detalhes completos do perfil do cliente logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ClientResponse> getMyProfile(Principal principal) {
        String loggedInEmail = principal.getName();
        var response = getClientUseCase.execute(loggedInEmail);
        return ResponseEntity.ok(clientEntityMapper.toResponse(response));
    }

    @Operation(summary = "Atualizar Perfil", description = "Atualiza os dados do perfil do cliente logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<ClientResponse> updateMyProfile(Principal principal, @Valid @RequestBody UpdateClientRequest request) {
        String loggedInEmail = principal.getName();
        var loggedInClient = getClientUseCase.execute(loggedInEmail);
        var response = updateClientUseCase.execute(loggedInClient.getId(), request);
        return ResponseEntity.ok(clientEntityMapper.toResponse(response));
    }
}