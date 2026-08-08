package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.application.dto.UpdateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.UpdateServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
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

@Tag(name = "Service Provider Profile", description = "Endpoints para gerenciamento do perfil do prestador de serviços")
@PreAuthorize("hasRole('SERVICE_PROVIDER')")
@RestController
@RequestMapping("/api/v1/provider/me")
public class ServiceProviderProfileController {

    private final GetServiceProviderUseCase getServiceProviderUseCase;
    private final UpdateServiceProviderUseCase updateServiceProviderUseCase;
    private final ServiceProviderEntityMapper mapper;

    public ServiceProviderProfileController(
            GetServiceProviderUseCase getServiceProviderUseCase,
            UpdateServiceProviderUseCase updateServiceProviderUseCase,
            ServiceProviderEntityMapper mapper) {
        this.getServiceProviderUseCase = getServiceProviderUseCase;
        this.updateServiceProviderUseCase = updateServiceProviderUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Obter Perfil do Prestador", description = "Retorna os detalhes completos do perfil do prestador de serviços logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceProviderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ServiceProviderResponse> getMyProfile(Principal principal) {
        String loggedInEmail = principal.getName();
        var provider = getServiceProviderUseCase.execute(loggedInEmail);
        return ResponseEntity.ok(mapper.toResponse(provider));
    }

    @Operation(summary = "Atualizar Perfil do Prestador", description = "Atualiza os dados do perfil do prestador de serviços logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceProviderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<ServiceProviderResponse> updateMyProfile(Principal principal, @Valid @RequestBody UpdateServiceProviderRequest request) {
        String loggedInEmail = principal.getName();
        var loggedInProvider = getServiceProviderUseCase.execute(loggedInEmail);

        var updatedProvider = updateServiceProviderUseCase.execute(loggedInProvider.getId(), request);
        return ResponseEntity.ok(mapper.toResponse(updatedProvider));
    }
}