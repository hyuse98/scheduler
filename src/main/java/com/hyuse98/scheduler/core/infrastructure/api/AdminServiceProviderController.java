package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.DeleteServiceProviderUseCase;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hyuse98.scheduler.core.infrastructure.api.advice.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin - Service Provider", description = "Endpoints administrativos para prestadores de serviços")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/service-provider")
public class AdminServiceProviderController {

    private final GetServiceProviderUseCase getServiceProviderUseCase;
    private final DeleteServiceProviderUseCase deleteServiceProviderUseCase;
    private final ServiceProviderEntityMapper mapper;

    public AdminServiceProviderController(
            GetServiceProviderUseCase getServiceProviderUseCase,
            DeleteServiceProviderUseCase deleteServiceProviderUseCase,
            ServiceProviderEntityMapper mapper) {
        this.getServiceProviderUseCase = getServiceProviderUseCase;
        this.deleteServiceProviderUseCase = deleteServiceProviderUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Obter prestador por ID", description = "Busca os detalhes de um prestador específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceProviderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderResponse> getById(@PathVariable UUID id) {
        var provider = getServiceProviderUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(provider));
    }

    @Operation(summary = "Excluir um prestador de serviço", description = "Deleta fisicamente um prestador do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prestador excluído com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteServiceProvider(@PathVariable UUID id) {
        deleteServiceProviderUseCase.execute(id);
    }

    //TODO(Enable and Disable Endpoints)
}